CREATE DATABASE  IF NOT EXISTS `librarymanagementsystem` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `librarymanagementsystem`;
-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: localhost    Database: librarymanagementsystem
-- ------------------------------------------------------
-- Server version	8.0.22

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
-- Table structure for table `patrons`
--

DROP TABLE IF EXISTS `patrons`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patrons` (
  `patron_ID` int NOT NULL AUTO_INCREMENT,
  `firstName` varchar(225) DEFAULT NULL,
  `lastName` varchar(225) DEFAULT NULL,
  `address` varchar(225) DEFAULT NULL,
  `phoneNumber` varchar(225) DEFAULT NULL,
  `email` varchar(225) DEFAULT NULL,
  `numBooksOut` int DEFAULT '0',
  `maxBooks` int DEFAULT '10',
  `numHolds` int DEFAULT '0',
  `maxHolds` int DEFAULT '25',
  `totalFineAmount` float DEFAULT '0',
  PRIMARY KEY (`patron_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patrons`
--

LOCK TABLES `patrons` WRITE;
/*!40000 ALTER TABLE `patrons` DISABLE KEYS */;
INSERT INTO `patrons` VALUES (10,'Dan','Gallagher','New Jersey','123-456-7890','dtm@gmail.com',1,10,1,25,0),(11,'Justin','Sherman','Philadelphia','987-654-3210','psherman42@wallabyway.syd',2,10,0,25,3.5),(12,'Michelle','Obama','Chicago','555-555-5555','first_lady@hotmail.com',0,10,1,25,0),(13,'Barak','Obama','Chicago','999-999-9999','da_gr8est@yahoo.com',1,10,0,25,0),(14,'Booker','Washington','Alabama','181-181-1818','mr.peanut@gmail.com',0,10,1,25,0.5);
/*!40000 ALTER TABLE `patrons` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-12-03 23:00:28
