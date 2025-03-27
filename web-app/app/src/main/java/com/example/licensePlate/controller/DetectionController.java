package com.example.licensePlate.controller;

import com.example.licensePlate.model.Detection;
import com.example.licensePlate.service.DetectionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/detections")
public class DetectionController {

    @Autowired
    private DetectionService detectionService;

    @PostMapping("/process-license-plate")
    public ResponseEntity<?> processLicensePlate(@RequestParam("image") MultipartFile image) {

        Map<String, Object> response = new HashMap<>();

        System.out.println("Calling /process-license-plate Python API Endpoint for license plate detection...");

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", new ByteArrayResource(image.getBytes()) {
                @Override
                public String getFilename() {
                    return image.getOriginalFilename();
                }
            });

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            RestTemplate restTemplate = new RestTemplate();


            ResponseEntity<Map> pythonResponse = restTemplate.exchange(
                    "http://localhost:8000/process-license-plate",
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            if (pythonResponse.getStatusCode().is2xxSuccessful() && pythonResponse.getBody() != null) {

                System.out.println("Python API responded successfully");

                Map<String, Object> data = pythonResponse.getBody();
                response.put("status", "200");
                response.put("licensePlate", data.get("license_plate"));
                response.put("confidence", data.get("confidence"));
                response.put("detectionDate", data.get("date"));
                response.put("message", data.get("message"));

                System.out.println("License plate detected: " + data.get("license_plate"));

                return ResponseEntity.ok(response);
            } else {
                System.out.println("Python API responded with error");
                response.put("status", "error");
                response.put("message", "Failed to process license plate");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            System.out.println("Error in the API calling process: " + e.getMessage());
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }


    @PostMapping("/save")
    public ResponseEntity<?> saveDetection(@Validated @RequestBody Detection detection) {

        Map<Object, String> response = new HashMap<>();

        try {

            Detection savedDetection = detectionService.saveDetection(detection);

            if(savedDetection == null)
            {
                response.put("status", "error");
                response.put("message", "Failed to save detection!");
            }
            else {
                response.put("status", "success");
                response.put("licensePlate", savedDetection.getLicensePlate());
                response.put("message", "Detection saved successfully!");
            }

            return ResponseEntity.ok(response);

        } catch(Exception e)
        {
            response.put("status", "error");
            response.put("message", e.getMessage());

            return ResponseEntity.internalServerError().body(response);
        }
    }


    @GetMapping("/list")
    public ResponseEntity<?> listDetections(@RequestParam String numOfDetections) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        int numOfDetectionsInt = Integer.parseInt(numOfDetections);

        List<Detection> detections = detectionService.getNdetections(numOfDetectionsInt);
        List<Map<String, String>> detectionDetails = new ArrayList<>();

        for (Detection detection : detections) {
            Map<String, String> details = new HashMap<>();

            details.put("detectionId", String.valueOf(detection.getDetectionId()));
            details.put("detectionDate", detection.getDetectionDate().format(formatter));
            details.put("licensePlate", detection.getLicensePlate());
            details.put("confidence", String.valueOf(detection.getConfidence()));

            detectionDetails.add(details);
        }
        return ResponseEntity.ok(detectionDetails);

    }
}
