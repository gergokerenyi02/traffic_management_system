# NECCESSARY LIBRARIES
import cv2
from fastapi import FastAPI, File, UploadFile, HTTPException
from fastapi.responses import JSONResponse
from ultralytics import YOLOv10 as YOLO
from paddleocr import PaddleOCR
import numpy as np
from contextlib import asynccontextmanager
import datetime
import uvicorn
from io import BytesIO
from PIL import Image, ImageOps
from fastapi.middleware.cors import CORSMiddleware
import re
from skimage.measure import regionprops
from skimage.measure import label as sk_label


# DEBUG LOG SETTINGS
green = '\033[32m'
red = '\033[31m'
yellow = '\033[33m'
default = '\033[0m'


# IMAGE TRANSFORMATION FOR OCR
class ImageTransformer:
    @staticmethod
    def transform(image, forceInvert=False):

        # IMAGE IS RGB
        # Convert to BGR
        # This step is only needed, because in ipython version code was written for BGR images
        image = cv2.cvtColor(image, cv2.COLOR_RGB2BGR)
        
        # STEP 1: GRAYSCLAE
        gray_image = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)


        

        # STEP 3: Binarize
        binary_image = cv2.adaptiveThreshold(gray_image, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY_INV, 15, 5) # prev 15, 10
        
        

        # Ezt még lehet kiszedem teljesen, mert az Invert nem sok példánál segített
        # Egyedül akkor, ha pl piros rendszámtáblán van szöveg
        if forceInvert:
            # Force inverting image
            binary_image = cv2.bitwise_not(binary_image)


        # STEP 4: Crop (lightweight)
        height, width = binary_image.shape
        cropped_image = binary_image[int(height * 0.05):int(height * 0.95), 0:width]


        # STEP 5: GET REGIONS
        labeled_image = sk_label(cropped_image)
        regions = regionprops(labeled_image)

        # FILTER: STEP 1: Filter out too small/big regions for better mean/std calculation
        
        min_width, max_width = 3, 100
        min_height, max_height = 8, 150

        # FILTER: EXTRA FIX, If width is bigger than height is probably a false positive for license plates
        regions = [region for region in regions if min_width < (region.bbox[3] - region.bbox[1]) < max_width and min_height < (region.bbox[2] - region.bbox[0]) < max_height and (region.bbox[3] - region.bbox[1]) < (region.bbox[2] - region.bbox[0])]

        areas = [region.area for region in regions]
        widths = [region.bbox[3] - region.bbox[1] for region in regions] # x2 - x1
        heights = [region.bbox[2] - region.bbox[0] for region in regions] # y2 - y1

        mean_width = np.mean(widths)
        mean_height = np.mean(heights)
        mean_area = np.mean(areas)

        std_width = np.std(widths)
        std_height = np.std(heights)
        
        min_area = mean_area * 0.5  # Set min_area to be 50% of the mean area
        
        width_threshold_upper = mean_width + 3.5 * std_width  # Adjust based on your needs
        height_threshold_upper = mean_height + 3.5 * std_height  # Adjust based on your needs


        # PREPARING IMAGE(S) THAT WILL BE RETURNED

        # BINARIZED VISUALIZATION
        filtered_image = np.zeros_like(image)
        
        # BLURRED ORIGINAL IMAGE, sharp on bounding box areas
        filtered_image2 = np.random.randint(190, 220, (640,640,3), dtype=np.uint8)  # Random shades of light gray ez nem rossz!
        filtered_image2 = cv2.GaussianBlur(filtered_image2, (3, 3), 0)

        

        # FILTER: STEP 3: Filter out regions that fall outside the thresholds
        good_bboxes = []

        for region in regions:
            w = region.bbox[3] - region.bbox[1]
            h = region.bbox[2] - region.bbox[0]
        
            if region.area >= min_area and w <= width_threshold_upper and h <= height_threshold_upper:
                
                # GOOD REGION AREA DETECTED!
                
                # SAVING THE COORDS
                coords = region.coords
                good_bboxes.append(region.bbox)

                # MODIFYING THE BINARIZED IMAGE
                # Modify end image for random whiteish color, this is the best for good OCR results
                # Best option would be to fine-tune the OCR model, but we avoid this now.
                for x, y in coords:
                    filtered_image[x, y] = np.random.randint(210, 245, 1, dtype=np.uint8)

            

        # Draw bounding boxes area from original image onto the filtered image
        for bbox in good_bboxes:
            minr, minc, maxr, maxc = bbox
            # Add a small padding outwards
            minr = max(0, minr - 5)
            minc = max(0, minc - 5)
            maxr = min(height, maxr + 15)
            maxc = min(width, maxc + 5)

            filtered_image2[minr:maxr, minc:maxc] = image[minr:maxr, minc:maxc]
            # Increase contrast in the selected region
            alpha = 1.1  # Increase contrast (1.0 = original, >1.0 = more contrast)
            beta = 0     # Brightness offset (can be adjusted if needed)

            filtered_image2[minr:maxr, minc:maxc] = cv2.convertScaleAbs(image[minr:maxr, minc:maxc], alpha=alpha, beta=beta)

        # Add a small padding outwards
        padded_image = cv2.copyMakeBorder(filtered_image2, 40, 40, 40, 40, cv2.BORDER_CONSTANT, value=[255, 255, 255])

        # Return black/white image (blurred background + detected & sharp good bbox areas), and binarized image
        return cv2.cvtColor(padded_image, cv2.COLOR_BGR2GRAY), filtered_image


# TEXT TRANSFORMATION AFTER OCR (SIMPLE CLEANING)   
class TextTransformer:
    @staticmethod
    def clean_text(text):
        text = re.sub(r"[^a-zA-Z0-9]", "", text)
        return text

# WRAPPER CLASS FOR YOLO, OCR MODEL (Further improvements: make ImageTransformer non-static, inherit from ImageTransformer and use it in LicensePlateRecognizer)
class LicensePlateRecognizer:
    def __init__(self, model_path):
        self.model = YOLO(model_path)
        self.ocr_model = PaddleOCR(use_angle_cls=True, use_gpu=False)

    # PROCESS OCR RESULTS, IF FAILS ON FIRST TRY, RUN SECOND TRY ON INVERTED IMAGE
    # Improvement: Instead of invert, we can use the bitwise approach (currently implemented, but not used)
    def process_ocr_results(self, result, original_img):
        try:
            # Throws the following exception, if no text detected: 'NoneType' object is not iterable
            for record in result[0]:
                
                data = record[1]
                license_plate_text = data[0]
                confidence = float(data[1])

                if len(TextTransformer.clean_text(license_plate_text))  <= 2:
                    raise Exception(f'Text too short for valid license plate: {license_plate_text}')
                    
                # Alternative approach: instead of invert, we can use the bitwise approach

                if(confidence <= 0.8):
                    # Low confidence text detected, but we still return it, alternative approach bitwise
                    return license_plate_text, confidence

                if(confidence > 0.8):
                    return license_plate_text, confidence
        except Exception as e:
            print(f"{yellow}OCR failed to extract license plate text at first try! {e}{default}")
            print(f"{yellow}Running second try on inverted image...{e}{default}")

            # Alternative approach: instead of invert, we can use the bitwise approach
            
            transformed_img_grayScaled, binarized = ImageTransformer.transform(original_img, forceInvert=True)
            
            result = self.ocr_model.ocr(transformed_img_grayScaled, det=True, cls=False)
            try:
                # Throws the following exception, if no text detected: 'NoneType' object is not iterable
                for record in result[0]:
                    
                    data = record[1]
                    license_plate_text = data[0]
                    confidence = float(data[1])

                    if(confidence <= 0.8):
                        print(f"Low confidence text detected: {license_plate_text}, Confidence: {round(float(confidence * 100), 2)}%")
                        return license_plate_text, confidence

                    if(confidence > 0.8):
                        print(f"License plate detected: {license_plate_text}, Confidence: {round(float(confidence * 100), 2)}%")
                        return license_plate_text, confidence
            except Exception as e:
                print(f"{red}OCR failed to extract license plate text at second try! {e}{default}")
                print(f"Tip: try on binarized image.")

                return None, 0

    # RUN DETECTION ON IMAGE
    # PRECONDITION: Image is resized to 640x640
    def detect_license_plate(self, image):
        
        # Resize to 640x640
        if image.shape[0] > 640 or image.shape[1] > 640 or image.shape[0] < 640 or image.shape[1] < 640:
            image = cv2.resize(image, (640, 640))
        
        result = self.model.predict(image) # Run prediction on the image with my fine-tuned model
        
        # Check if license plate was detected
        if result[0].boxes.cls.size()[0] != 0:
            license_plate = self.crop_license_plate(image, result[0].boxes.xyxy[0])
            return license_plate, True
        else:
            print(f"{red}No license plate detected!{default}")
            return None, False

    # CROP LICENSE PLATE BBOX AREA FROM IMAGE
    def crop_license_plate(self, image, box):
        x1, y1, x2, y2 = map(int, box)
        return image[y1:y2, x1:x2]

    # RUN OCR ON IMAGE
    def extract_text(self, image, original_img):
        
        # Runs OCR on the image, if it fails on first try, "process_ocr_results" will run a second try on inverted image (needs original_img)
        result = self.ocr_model.ocr(image, det=True, cls=False)
        license_plate_text, confidence = self.process_ocr_results(result, original_img=original_img)

        
        return license_plate_text, confidence


@asynccontextmanager
async def lifespan(app: FastAPI):
    global recognizer
    recognizer = LicensePlateRecognizer("./runs/detect/train5/weights/best.pt")
    yield
    del recognizer


app = FastAPI(lifespan=lifespan)
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:8080"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


@app.get("/")
async def root():
    return {"message": "License Plate Detection API is running!"}


@app.post("/process-license-plate")
async def process_license_plate(image: UploadFile = File(...)):
    current_date = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    print(f"{yellow}Incoming request on API Endpoint: /process-license-plate{default}")
    print(f"{default}--- DEBUG LOGGING BELOW ---{default}")
    try:
        received_img = BytesIO(await image.read())
        pil_image = Image.open(received_img)

        # Fixes: bad image rotation
        corrected_img = ImageOps.exif_transpose(pil_image) #RGB
        
        loaded_img = np.array(corrected_img)
    except:
        raise HTTPException(status_code=400, detail="Error receiving the on the server side!")
    

    print(f"{yellow}Image received!{default}")
    print(f"{yellow}Detecting license plate...{default}")
    image, status = recognizer.detect_license_plate(loaded_img)


    if status is False:
        print(f"{red}License plate detected: {status}{default}")
        return JSONResponse(status_code=200, content={"date": current_date, "message": "No license plate detected!", "license_plate": -1, "confidence": -1})

    print(f"{green}License plate detected: {status}{default}")

    # Returns: 2 versions of the image
        # 1. Blurred background with sharp bounding box areas drawn
        # 2. Binarized image
        # Conclusion: 1. option gets a better overall result, we use this during simulation (even at OCR second try)
    transformed_img_grayScaled, binarized = ImageTransformer.transform(image)
    
    print(f"{yellow}Extracting text...{default} (from: {green}transformed_img_grayScaled{default}/binarized)")
    extraction, confidence = recognizer.extract_text(transformed_img_grayScaled, original_img=image)

    
    if extraction is None:
        print(f"{red}OCR was unable to extract a correct license plate!{default}")
        return JSONResponse(status_code=200, content={"date": current_date, "message": "OCR was unable to extract a correct license plate!", "license_plate": -1, "confidence": 0})

    print(f"{green}License plate detected: {extraction}, Confidence: {confidence}{default}")
    cleaned_text = TextTransformer.clean_text(extraction)
    print(f"{yellow}Cleaning License Plate Text{default}: {yellow}{extraction} -> {green}{cleaned_text}{default}")
    return JSONResponse(status_code=200, content={"date": current_date, "message": "License plate detected!", "license_plate": cleaned_text, "confidence": round(float(confidence * 100), 2)})




def main():
    uvicorn.run(app, host="0.0.0.0", port=8000)

if __name__ == "__main__":
    main()
