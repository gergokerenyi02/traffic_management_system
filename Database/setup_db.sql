CREATE DATABASE  IF NOT EXISTS `license_plate_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `license_plate_db`;
-- MySQL dump 10.13  Distrib 8.0.40, for macos14 (arm64)
--
-- Host: localhost    Database: license_plate_db
-- ------------------------------------------------------
-- Server version	9.0.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `client`
--

DROP TABLE IF EXISTS `client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `client` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client`
--

LOCK TABLES `client` WRITE;
/*!40000 ALTER TABLE `client` DISABLE KEYS */;
INSERT INTO `client` VALUES (24,'$2a$10$imfQskmgby/Nwq82HIMGUuG3k9k71oaVfNJrpcngCQv6KueiSLqf2','testuser1'),(25,'$2a$10$IeQgJqyruD7J5Z2SodmXee2NkK3Au00FNyeSqePDTVlkcS8s9phGq','asd'),(26,'$2a$10$M5njsFsigu0FDRrz3IOxAeSujCfo5at1l5mJJhu/T3M77wurudzAi','testuser2'),(27,'$2a$10$BA.3xiPPmtq0UjeEfoJcFuCjrjKqjwBECaC3pT3uOIzN5R7A4jDj.','asdasd'),(28,'$2a$10$5LqJQdeMpT.4nw/EQ8blseQhm6hR9HiBEtuMtr4wYT0ud31LIiE2u','testuser3'),(29,'$2a$10$4LB8rM0sX.tDpSuaRXTJe.jQDZ0SXIVqekF1zs/ysT7bbqKmYT.oG','testuser4'),(30,'$2a$10$BilD1ucXgVdM1h2.mmy9g.WV0WYnJCG5hwUwk/yw44J/9VqcoeznO','5testuser'),(31,'$2a$10$JZkzm9y72AAxTzLCiek9IuAGgh9wNL.cD8OSIWTWhOM9uujQX16fy','testuser6');
/*!40000 ALTER TABLE `client` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detection`
--

DROP TABLE IF EXISTS `detection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detection` (
  `detection_id` bigint NOT NULL AUTO_INCREMENT,
  `confidence` double NOT NULL,
  `detection_date` datetime DEFAULT NULL,
  `license_plate` varchar(255) NOT NULL,
  `message` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL,
  PRIMARY KEY (`detection_id`)
) ENGINE=InnoDB AUTO_INCREMENT=96 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detection`
--

LOCK TABLES `detection` WRITE;
/*!40000 ALTER TABLE `detection` DISABLE KEYS */;
INSERT INTO `detection` VALUES (1,97.79,'2024-12-31 18:35:01','LKM-259','License plate detected!','200'),(2,87.93,'2024-12-31 18:46:29','00LAZ','License plate detected!','200'),(3,98.51,'2024-12-31 18:47:42','ILUVK9S','License plate detected!','200'),(4,-1,'2024-12-31 18:49:15','-1','No license plate detected!','200'),(5,88.99,'2024-12-31 18:50:04','WAEI','License plate detected!','200'),(6,99.22,'2024-12-31 18:50:21','EDY539','License plate detected!','200'),(7,84.95,'2025-01-01 15:05:16','1232','License plate detected!','200'),(8,95.8,'2025-01-01 15:06:07','THRIFIY','License plate detected!','200'),(9,98.84,'2025-01-01 15:06:53','CAPNSAH','License plate detected!','200'),(10,97.2,'2025-01-01 15:59:09','AUGCALIFORNIR','License plate detected!','200'),(11,97.79,'2025-01-01 15:59:54','LKM-259','License plate detected!','200'),(12,99.57,'2025-01-01 16:06:48','BACK256','License plate detected!','200'),(13,-1,'2025-01-04 17:21:47','-1','No license plate detected!','200'),(14,98.16,'2025-01-04 17:26:34','2THMOVR','License plate detected!','200'),(15,97.79,'2025-01-05 16:24:51','LKM-259','License plate detected!','200'),(16,-1,'2025-01-05 16:26:20','-1','No license plate detected!','200'),(17,97.79,'2025-02-13 13:57:45','LKM-259','License plate detected!','200'),(18,100,'2025-03-13 14:26:08','MGT 346','License plate detected!','200'),(19,100,'2025-03-13 14:29:55','MGT 346','License plate detected!','200'),(20,100,'2025-03-13 14:34:57','MGT 346','License plate detected!','200'),(21,100,'2025-03-13 14:40:13','MGT 346','License plate detected!','200'),(22,100,'2025-03-13 14:40:30','MGT 346','License plate detected!','200'),(23,100,'2025-03-13 14:41:55','MGT 346','License plate detected!','200'),(24,100,'2025-03-13 14:46:50','51G 47281','License plate detected!','200'),(25,100,'2025-03-13 14:50:12','MGT 346','License plate detected!','200'),(26,100,'2025-03-13 14:56:40','MGT 346','License plate detected!','200'),(27,100,'2025-03-13 14:58:53','MGT 346','License plate detected!','200'),(28,100,'2025-03-13 16:12:01','SXY-007','License plate detected!','200'),(29,100,'2025-03-13 16:15:32','FTIVAXX','License plate detected!','200'),(30,100,'2025-03-13 16:18:24','fTIVAXX','License plate detected!','200'),(31,0,'2025-03-13 16:18:53','','OCR was unable to extract a correct license plate!','200'),(32,0,'2025-03-13 16:25:09','-1','OCR was unable to extract a correct license plate!','200'),(33,-1,'2025-03-13 16:40:37','-1','No license plate detected!','200'),(34,100,'2025-03-13 16:55:14','MGT 346','License plate detected!','200'),(35,100,'2025-03-13 17:08:14','MERCDEZ','License plate detected!','200'),(36,100,'2025-03-13 17:09:43','MERCDEZ','License plate detected!','200'),(37,97.5,'2025-03-13 17:22:59','MERCDEZ','License plate detected!','200'),(38,96.7,'2025-03-13 17:41:15','51F-220.29','License plate detected!','200'),(39,99.44,'2025-03-13 17:44:53','51F-220.29','License plate detected!','200'),(40,99.44,'2025-03-13 17:57:00','51F22029','License plate detected!','200'),(41,94.12,'2025-03-13 18:43:00','ALEABE','License plate detected!','200'),(42,98.23,'2025-03-21 13:16:09','2THMOVR','License plate detected!','200'),(43,87.93,'2025-03-21 13:17:07','UNMEZ','License plate detected!','200'),(44,91.23,'2025-03-21 13:17:23','TJMAXX','License plate detected!','200'),(45,88.24,'2025-03-21 13:17:30','SOIJAWU','License plate detected!','200'),(46,99.73,'2025-03-21 13:17:37','51F59011','License plate detected!','200'),(47,98.17,'2025-03-21 20:50:28','SOUP4U','License plate detected!','200'),(48,96.96,'2025-03-21 20:54:59','NANERR','License plate detected!','200'),(49,92.73,'2025-03-21 20:57:12','SEASIX','License plate detected!','200'),(50,92.47,'2025-03-21 20:57:38','SEASIX','License plate detected!','200'),(51,0,'2025-03-22 09:04:29','0','OCR was unable to extract a correct license plate!','200'),(52,98.82,'2025-03-22 09:05:03','MOONGLO','License plate detected!','200'),(53,90.66,'2025-03-22 09:07:30','NODIG','License plate detected!','200'),(54,0,'2025-03-22 09:07:53','0','OCR was unable to extract a correct license plate!','200'),(55,99.44,'2025-03-22 12:37:29','BACK256','License plate detected!','200'),(56,99.23,'2025-03-22 12:37:44','BACK256','License plate detected!','200'),(57,99.26,'2025-03-22 12:46:41','BACK256','License plate detected!','200'),(58,98.09,'2025-03-22 12:46:45','BACK256','License plate detected!','200'),(59,99.41,'2025-03-22 12:53:31','BACK256','License plate detected!','200'),(60,99.27,'2025-03-22 12:58:18','BACK256','License plate detected!','200'),(61,99.43,'2025-03-22 12:59:11','BACK256','License plate detected!','200'),(62,99.21,'2025-03-22 13:03:00','BACK256','License plate detected!','200'),(63,98.41,'2025-03-22 13:03:13','BACK256','License plate detected!','200'),(64,99.41,'2025-03-22 13:04:17','BACK256','License plate detected!','200'),(65,99.45,'2025-03-22 13:06:30','BACK256','License plate detected!','200'),(66,99.42,'2025-03-22 13:07:14','BACK256','License plate detected!','200'),(67,99.37,'2025-03-22 13:10:20','BACK256','License plate detected!','200'),(68,99.44,'2025-03-22 13:10:30','BACK256','License plate detected!','200'),(69,72.2,'2025-03-22 13:10:41','GOIS','License plate detected!','200'),(70,96.32,'2025-03-22 13:10:59','PMONEE','License plate detected!','200'),(71,0,'2025-03-22 13:11:27','0','OCR was unable to extract a correct license plate!','200'),(72,0,'2025-03-22 13:17:47','-1','OCR was unable to extract a correct license plate!','200'),(73,0,'2025-03-22 13:57:30','-1','OCR was unable to extract a correct license plate!','200'),(74,92.68,'2025-03-22 13:57:46','MGT346','License plate detected!','200'),(75,92.9,'2025-03-22 13:58:24','MGT346','License plate detected!','200'),(76,93.17,'2025-03-22 13:58:46','MGT346','License plate detected!','200'),(77,92.99,'2025-03-22 13:59:34','MGT346','License plate detected!','200'),(78,92.43,'2025-03-22 14:00:03','MGT346','License plate detected!','200'),(79,92.94,'2025-03-22 14:02:05','GAA478','License plate detected!','200'),(80,93.28,'2025-03-22 14:02:11','GAA478','License plate detected!','200'),(81,92.28,'2025-03-23 15:15:05','MGT346','License plate detected!','200'),(82,93.39,'2025-03-23 15:15:16','MGT346','License plate detected!','200'),(83,91.71,'2025-03-23 15:15:19','MGT346','License plate detected!','200'),(84,-1,'2025-03-23 15:18:00','-1','No license plate detected!','200'),(85,-1,'2025-03-23 15:18:05','-1','No license plate detected!','200'),(86,94.24,'2025-03-23 15:18:15','GAA478','License plate detected!','200'),(87,94.2,'2025-03-23 15:18:20','GAA478','License plate detected!','200'),(88,-1,'2025-03-23 15:19:09','-1','No license plate detected!','200'),(89,99,'2025-03-23 15:19:25','THREENME','License plate detected!','200'),(90,98.35,'2025-03-23 15:19:31','THREENME','License plate detected!','200'),(91,99.4,'2025-03-27 12:48:00','BACK256','License plate detected!','200'),(92,96.94,'2025-03-27 12:48:14','NANERR','License plate detected!','200'),(93,95.04,'2025-03-27 12:49:49','NANERR','License plate detected!','200'),(94,99.43,'2025-03-27 13:01:37','BACK256','License plate detected!','200'),(95,98.07,'2025-03-27 13:02:46','MASTER7','License plate detected!','200');
/*!40000 ALTER TABLE `detection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parking_sessions`
--

DROP TABLE IF EXISTS `parking_sessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parking_sessions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `license_plate` varchar(255) NOT NULL,
  `entry_time` datetime NOT NULL,
  `exit_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `license_plate` (`license_plate`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parking_sessions`
--

LOCK TABLES `parking_sessions` WRITE;
/*!40000 ALTER TABLE `parking_sessions` DISABLE KEYS */;
INSERT INTO `parking_sessions` VALUES (12,'BACK256','2025-03-22 14:10:31',NULL),(13,'GOIS','2025-03-22 14:10:43',NULL);
/*!40000 ALTER TABLE `parking_sessions` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-27 18:22:16
