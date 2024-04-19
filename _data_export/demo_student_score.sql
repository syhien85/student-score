-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: demo_student_score
-- ------------------------------------------------------
-- Server version	8.2.0

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
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `update_at` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_4xqvdpkafb91tt3hsb67ga3fj` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES (1,'2024-04-17 20:21:58.530000','2024-04-17 20:21:58.530000','Tin học đại cương'),(2,'2024-04-17 20:22:01.901000','2024-04-17 20:22:01.901000','Kỹ thuật lập trình'),(3,'2024-04-17 20:22:04.407000','2024-04-17 20:22:04.407000','Toán cao cấp');
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `score`
--

DROP TABLE IF EXISTS `score`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `score` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `update_at` datetime(6) DEFAULT NULL,
  `score` double DEFAULT NULL,
  `course_id` int DEFAULT NULL,
  `student_user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UniqueCourseAndStudent` (`course_id`,`student_user_id`),
  KEY `FKam1xu8asim14vtgeruj0gb0n2` (`student_user_id`),
  CONSTRAINT `FK4r2i87mwev058q4nvnl36latl` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`),
  CONSTRAINT `FKam1xu8asim14vtgeruj0gb0n2` FOREIGN KEY (`student_user_id`) REFERENCES `student` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `score`
--

LOCK TABLES `score` WRITE;
/*!40000 ALTER TABLE `score` DISABLE KEYS */;
INSERT INTO `score` VALUES (1,'2024-04-17 20:24:18.093000','2024-04-17 20:24:18.093000',10,1,7),(2,'2024-04-17 20:24:33.716000','2024-04-17 20:24:33.716000',9,2,7),(3,'2024-04-17 20:24:39.362000','2024-04-17 20:24:39.362000',9,3,7),(4,'2024-04-17 20:25:09.785000','2024-04-17 20:25:09.785000',4,1,6),(5,'2024-04-17 20:25:12.445000','2024-04-17 20:25:12.445000',4,2,6),(6,'2024-04-17 20:25:19.139000','2024-04-17 20:25:19.139000',9,3,6);
/*!40000 ALTER TABLE `score` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
  `created_at` datetime(6) DEFAULT NULL,
  `update_at` datetime(6) DEFAULT NULL,
  `student_code` varchar(255) DEFAULT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `FKk5m148xqefonqw7bgnpm0snwj` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES ('2024-04-17 20:20:47.490000','2024-04-17 20:20:47.490000','SV1',5),('2024-04-17 20:21:00.148000','2024-04-17 20:21:00.148000','SV2',6),('2024-04-17 20:21:19.796000','2024-04-17 20:21:19.796000','SV3',7);
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `update_at` datetime(6) DEFAULT NULL,
  `age` int NOT NULL,
  `avatar_url` varchar(255) DEFAULT NULL,
  `birthdate` date DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `home_address` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'2024-04-17 20:12:16.523000','2024-04-17 20:12:16.523000',0,NULL,NULL,'syhien85@hotmail.com',NULL,'Admin','$2a$10$7VKmLJRmJLKK8VdlY41Z3O.ujhrB3GxXjWr7b1LJKg0E.rK573Hfe','admin'),(2,'2024-04-17 20:13:30.816000','2024-04-17 20:13:30.816000',33,'8609ee46-7382-4624-ab19-d278a98d5fff.png','2022-12-12','user1@gmail1.com','HN','User 1','$2a$10$J7pm7xwqmrU0DUvKJc85hOOZwKU/wGzyIa2AW8k0zlN6YfnsvS7T.','user1'),(3,'2024-04-17 20:14:37.853000','2024-04-17 20:14:37.853000',33,'ba26ca0d-451e-41a4-948f-2b6f17c71596.png','2022-12-12','user2@gmail1.com','HN','User 2','$2a$10$5aU8XaDc2GcxHR1CquZXge4SQ1igf1u15zj1boV9sFBOhuFCCIIUe','user2'),(4,'2024-04-17 20:14:52.725000','2024-04-17 20:14:52.725000',33,'f3c5c2c5-db5c-4373-8ea2-860987bc0008.png','2022-12-12','user3@gmail1.com','HN','User 3','$2a$10$QcGI.NvWfDD6wNLrwAfG6.7sGns0okUG3vEesoVembY/9gj6b6hzS','user3'),(5,'2024-04-17 20:20:47.488000','2024-04-17 20:20:47.488000',33,NULL,'2002-12-12','student1@gmail1.com','HN','Student 1','$2a$10$TRQk68TBrHKFyd0Kz1rone2JOcCM3dlnbYSUWuBff1OAfhOA1rqCm','student1'),(6,'2024-04-17 20:21:00.146000','2024-04-17 20:21:00.146000',33,NULL,'2002-12-12','student2@gmail1.com','HN','Student 2','$2a$10$tfL2vE6VzzNkKg/sUBwX1OOxLn7mPqg2v.kT19.V3ig.8gMpTupTi','student2'),(7,'2024-04-17 20:21:19.794000','2024-04-17 20:21:19.794000',33,NULL,'2002-12-12','student3@gmail1.com','HN','Student 3','$2a$10$RJokbcmwFloGG4qRxHCgt.jXvVMyCnHt6HrMKKVkfOH/5d2a4MArO','student3');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_role` (
  `user_id` int NOT NULL,
  `role` tinyint DEFAULT NULL,
  KEY `FK859n2jvi8ivhui0rl0esws6o` (`user_id`),
  CONSTRAINT `FK859n2jvi8ivhui0rl0esws6o` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `user_role_chk_1` CHECK ((`role` between 0 and 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (1,0),(2,1),(3,1),(4,1),(5,1),(6,1),(7,1);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-04-17 20:32:10
