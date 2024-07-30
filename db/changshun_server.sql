-- MySQL dump 10.13  Distrib 8.0.32, for Linux (x86_64)
--
-- Host: localhost    Database: changshun_server
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ai_pre_diagnosis`
--

DROP TABLE IF EXISTS `ai_pre_diagnosis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_pre_diagnosis` (
  `ai_id` bigint NOT NULL,
  `patient_uid` bigint NOT NULL,
  `gender` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `height` float DEFAULT NULL,
  `weight` float DEFAULT NULL,
  `blood_pressure_high` float DEFAULT NULL,
  `blood_pressure_low` float DEFAULT NULL,
  `pregnancy_history` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `diseases_list` text COLLATE utf8mb4_general_ci,
  `kidney_diseases` text CHARACTER SET utf32 COLLATE utf32_general_ci,
  `creatinine_level` float DEFAULT NULL,
  `heart_rate` int DEFAULT NULL,
  `blood_diseases` text COLLATE utf8mb4_general_ci,
  `total_cholesterol` float DEFAULT NULL,
  `be_diseases` text COLLATE utf8mb4_general_ci,
  `heart_diseases` text COLLATE utf8mb4_general_ci,
  `vessel_diseases` text COLLATE utf8mb4_general_ci,
  `early_cvd_family_history` tinyint(1) DEFAULT NULL,
  `genetic_disease_in_family` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `infectious_disease_history` text COLLATE utf8mb4_general_ci,
  `food_allergy_history` text COLLATE utf8mb4_general_ci,
  `smoking_status` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `smoking_duration` int DEFAULT NULL,
  `daily_smoking_amount` int DEFAULT NULL,
  `drinking_status` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `drinking_duration` int DEFAULT NULL,
  `daily_drinking_amount` float DEFAULT NULL,
  `danger_reason` int DEFAULT NULL,
  `is_clinical` tinyint DEFAULT NULL,
  PRIMARY KEY (`ai_id`),
  KEY `fk_patient_uid` (`patient_uid`),
  CONSTRAINT `fk_patient_uid` FOREIGN KEY (`patient_uid`) REFERENCES `patient_base` (`patient_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_pre_diagnosis`
--

LOCK TABLES `ai_pre_diagnosis` WRITE;
/*!40000 ALTER TABLE `ai_pre_diagnosis` DISABLE KEYS */;
INSERT INTO `ai_pre_diagnosis` VALUES (42,123,'男','2001-08-18',NULL,NULL,NULL,NULL,NULL,'血脂异常',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(63,120,'男','1986-02-07',159,100,28,43,'nostrud','血脂异常,脑血管病','45',85,13,'laborum ex do',95,NULL,NULL,'eiusmod dolor consectetur',1,NULL,'tempor dolor culpa ullamco minim','velit','是',95,63,NULL,62,62,3,1);
/*!40000 ALTER TABLE `ai_pre_diagnosis` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `common_question`
--

DROP TABLE IF EXISTS `common_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `common_question` (
  `qa_id` bigint NOT NULL COMMENT '问题及其对应的答案',
  `question_text` text COLLATE utf8mb4_general_ci COMMENT '问题内容',
  `answer_text` text COLLATE utf8mb4_general_ci,
  PRIMARY KEY (`qa_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `common_question`
--

LOCK TABLES `common_question` WRITE;
/*!40000 ALTER TABLE `common_question` DISABLE KEYS */;
INSERT INTO `common_question` VALUES (1,'what\'s your name?','Alice.'),(2,'How are you?','I\'m fine, thank you.'),(3,'What\'s your favorate color?','Black.'),(4,'Where are you from?','China.');
/*!40000 ALTER TABLE `common_question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctor_base`
--

DROP TABLE IF EXISTS `doctor_base`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctor_base` (
  `doctor_uid` bigint NOT NULL COMMENT '医生id',
  `doctor_name` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `doctor_phonenumber` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `position` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '医生职务（如主任医师、副主任医师等）',
  `affiliated_hospital` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '所属医疗机构名称',
  `permission` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '权限管理：医生、管理员',
  `department` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '所属科室',
  `username` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '登录系统的用户名，方便websocket使用，也是唯一的',
  PRIMARY KEY (`doctor_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor_base`
--

LOCK TABLES `doctor_base` WRITE;
/*!40000 ALTER TABLE `doctor_base` DISABLE KEYS */;
INSERT INTO `doctor_base` VALUES (123,'123','123',NULL,'水电费','',NULL,NULL),(1234,NULL,'234','11','wode','',NULL,NULL),(1813085631554011137,'光头强',NULL,NULL,'s','',NULL,NULL),(1813086790381527042,'测试名称','13512664377','主任医师','s','',NULL,'13512664377'),(1813138006243344385,'string','13000000','string','kkkk','','string','string');
/*!40000 ALTER TABLE `doctor_base` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `drug_eat_time`
--

DROP TABLE IF EXISTS `drug_eat_time`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `drug_eat_time` (
  `pep_id` bigint NOT NULL,
  `patient_uid` bigint NOT NULL,
  `eat_time` time DEFAULT NULL COMMENT '用药时间，24小时制',
  `pde_id` bigint NOT NULL COMMENT '用药管理表唯一id，依据此id可知该时间是哪个用药管理元素里面的',
  `last_eat_time` date DEFAULT '1999-01-01' COMMENT '最后一次服用时间，默认是1999年，记录当天药物是否已经服用只需查看这个字段是不是今天即可。',
  PRIMARY KEY (`pep_id`),
  KEY `drug_eat_time_patient_base_patient_uid_fk` (`patient_uid`),
  KEY `drug_eat_time_eat_drug_alert_pde_id_fk` (`pde_id`),
  CONSTRAINT `drug_eat_time_eat_drug_alert_pde_id_fk` FOREIGN KEY (`pde_id`) REFERENCES `eat_drug_alert` (`pde_id`),
  CONSTRAINT `drug_eat_time_patient_base_patient_uid_fk` FOREIGN KEY (`patient_uid`) REFERENCES `patient_base` (`patient_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用药时间对照表（因为一款药可能需要多个时间）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `drug_eat_time`
--

LOCK TABLES `drug_eat_time` WRITE;
/*!40000 ALTER TABLE `drug_eat_time` DISABLE KEYS */;
INSERT INTO `drug_eat_time` VALUES (123,123,'11:19:52',123,'2024-07-05'),(124,123,'08:36:23',123,'2024-07-03');
/*!40000 ALTER TABLE `drug_eat_time` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `eat_drug_alert`
--

DROP TABLE IF EXISTS `eat_drug_alert`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `eat_drug_alert` (
  `pde_id` bigint NOT NULL COMMENT '表唯一id，主键',
  `patient_uid` bigint NOT NULL,
  `drug_name` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `frequency` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用药频率：每天，每隔1天~每隔10天，每周，每2周~每8周',
  `unit` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '单位(毫克，毫升，微克，克，片，支，粒，包，瓶)',
  `dose` int DEFAULT NULL COMMENT '服用剂量',
  `is_active` tinyint(1) DEFAULT NULL COMMENT '是否激活该用药计划',
  PRIMARY KEY (`pde_id`),
  KEY `eat_drug_alert_patient_base_patient_uid_fk` (`patient_uid`),
  CONSTRAINT `eat_drug_alert_patient_base_patient_uid_fk` FOREIGN KEY (`patient_uid`) REFERENCES `patient_base` (`patient_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `eat_drug_alert`
--

LOCK TABLES `eat_drug_alert` WRITE;
/*!40000 ALTER TABLE `eat_drug_alert` DISABLE KEYS */;
INSERT INTO `eat_drug_alert` VALUES (123,123,'测试药物','每天','毫克',1,1);
/*!40000 ALTER TABLE `eat_drug_alert` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gpt_message`
--

DROP TABLE IF EXISTS `gpt_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gpt_message` (
  `message_id` bigint NOT NULL COMMENT '消息唯一标识符（主键）',
  `patient_uid` bigint DEFAULT NULL COMMENT '所属患者唯一id',
  `sender_type` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发送者的类型，''USER'' 代表用户，''GPT'' 代表GPT',
  `content` text COLLATE utf8mb4_general_ci COMMENT '消息内容',
  `sent_time` datetime DEFAULT NULL COMMENT '消息发送时间',
  PRIMARY KEY (`message_id`),
  KEY `gpt_message_patient_base_patient_uid_fk` (`patient_uid`),
  CONSTRAINT `gpt_message_patient_base_patient_uid_fk` FOREIGN KEY (`patient_uid`) REFERENCES `patient_base` (`patient_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='GPT对话表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gpt_message`
--

LOCK TABLES `gpt_message` WRITE;
/*!40000 ALTER TABLE `gpt_message` DISABLE KEYS */;
/*!40000 ALTER TABLE `gpt_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient_base`
--

DROP TABLE IF EXISTS `patient_base`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patient_base` (
  `patient_uid` bigint NOT NULL COMMENT '患者唯一id',
  `identification_number` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '身份证号码',
  `patient_name` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `physical_strength` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `sex` varchar(5) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '性别：[男性，女性]就这两种',
  `birthday` date DEFAULT NULL,
  `phone_number` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `wx_uid` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '微信id，确认绑定的微信,一般是openid或者unionid具体选择哪个再确认',
  `username` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '登录系统的用户名，方便websocket使用，也是唯一的',
  PRIMARY KEY (`patient_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='患者基本信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient_base`
--

LOCK TABLES `patient_base` WRITE;
/*!40000 ALTER TABLE `patient_base` DISABLE KEYS */;
INSERT INTO `patient_base` VALUES (120,'120','test',NULL,'男','2024-07-09',NULL,NULL,NULL),(123,'123','test',NULL,'男','2023-06-21','317','222',NULL),(124,'124',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(1811361598705803266,NULL,NULL,NULL,NULL,NULL,'18152727618',NULL,'thistest'),(1812765560231186433,'2335435','熊大',NULL,NULL,NULL,NULL,NULL,NULL),(1817203659879239681,NULL,NULL,NULL,NULL,NULL,'13796359847','o9dwD7Wtz01VLMNQhl8hVZdPtYHE','13796359847');
/*!40000 ALTER TABLE `patient_base` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient_bmi_mana`
--

DROP TABLE IF EXISTS `patient_bmi_mana`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patient_bmi_mana` (
  `bmi_uuid` bigint NOT NULL,
  `patient_uid` bigint DEFAULT NULL,
  `weight` float DEFAULT NULL COMMENT '体重（单位：千克）',
  `bmimeasurement_date` date DEFAULT NULL COMMENT '测量日期',
  `height` float DEFAULT NULL COMMENT '身高（单位：厘米）',
  PRIMARY KEY (`bmi_uuid`),
  KEY `patient_bmi_management_patient_base_patient_uid_fk` (`patient_uid`),
  CONSTRAINT `patient_bmi_management_patient_base_patient_uid_fk` FOREIGN KEY (`patient_uid`) REFERENCES `patient_base` (`patient_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient_bmi_mana`
--

LOCK TABLES `patient_bmi_mana` WRITE;
/*!40000 ALTER TABLE `patient_bmi_mana` DISABLE KEYS */;
INSERT INTO `patient_bmi_mana` VALUES (11,123,11,'2024-07-05',111);
/*!40000 ALTER TABLE `patient_bmi_mana` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient_case`
--

DROP TABLE IF EXISTS `patient_case`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patient_case` (
  `case_id` int NOT NULL COMMENT '主键，病历id',
  `patient_uid` int NOT NULL COMMENT '患者唯一id，外键',
  `patient_type` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '病人类型，是否为居民',
  `education_level` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '如：小学、初中等',
  `insurance_type` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '医疗保险类型',
  `address_province` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '省份名称（自治区、直辖市）',
  `address_city` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '城市/地区名称（州）',
  `address_county` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '县区名称',
  `address_town` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '乡镇名称（街道办事处）',
  `address_village` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '村庄/街道名称（路等）',
  `address_detail` varchar(300) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '详细住址',
  `occupation` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '患者职业',
  `onset_datetime` date DEFAULT NULL COMMENT '发病具体时间',
  `patient_source` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '患者类型名称（1门诊、2急诊、3住院、4体检、9其他）',
  `temperature` float DEFAULT NULL COMMENT '体温值(℃)',
  `pulse_rate` int DEFAULT NULL COMMENT '脉率值（次/min）',
  `diastolic_bp` float DEFAULT NULL COMMENT '舒张压值（mmHg）',
  `systolic_bp` float DEFAULT NULL COMMENT '收缩压值（mmHg）',
  `weight` float DEFAULT NULL COMMENT '体重值(kg)',
  `illness_severity` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '病情代码（1 危 2 急 3 一般 4 重 5 教学 6 科研）',
  `chief_complaint` text COLLATE utf8mb4_general_ci COMMENT '患者主诉内容',
  `present_illness` text COLLATE utf8mb4_general_ci COMMENT '现病史',
  `past_illness` text COLLATE utf8mb4_general_ci COMMENT '既往病情记录（既往病史）',
  `allergy_symptom` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '过敏症状名称',
  `allergen_name` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '过敏源名称',
  `allergy_type` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '过敏类别名称（ 如0药物过敏）',
  `severity_level` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '严重程度名称，例: 0一般',
  `physical_exam` text COLLATE utf8mb4_general_ci COMMENT '体格检查',
  `auxiliary_exam` text COLLATE utf8mb4_general_ci COMMENT '辅助检查结果',
  `diagnosis_name` text COLLATE utf8mb4_general_ci COMMENT '诊断名称（可多个，逗号分隔或使用关联表）',
  `other_treatment` text COLLATE utf8mb4_general_ci COMMENT '其他医学处置',
  `doctor_advice` text COLLATE utf8mb4_general_ci COMMENT '医嘱内容',
  PRIMARY KEY (`case_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient_case`
--

LOCK TABLES `patient_case` WRITE;
/*!40000 ALTER TABLE `patient_case` DISABLE KEYS */;
INSERT INTO `patient_case` VALUES (12,123,'居民','本科',NULL,'黑龙江','佳木斯',NULL,NULL,NULL,NULL,'工程师',NULL,NULL,36.5,70,120,70,74,NULL,'没有事 没有事 没有事 没有事没有事没有事 没有事 没有事没有事 没有事 没有事没有事 没有事 没有事没有事没有事 没有事 没有事没有事 没有事 没有事没有事 没有事','测试','没有病',NULL,NULL,NULL,NULL,'一切良好',NULL,NULL,NULL,'多喝水');
/*!40000 ALTER TABLE `patient_case` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient_device`
--

DROP TABLE IF EXISTS `patient_device`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patient_device` (
  `pdd_id` bigint NOT NULL COMMENT '表唯一id',
  `device_uid` bigint DEFAULT NULL,
  `device_name` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '绑定设备名称',
  `patient_uid` bigint NOT NULL,
  PRIMARY KEY (`pdd_id`),
  KEY `patient_device_patient_base_patient_uid_fk` (`patient_uid`),
  CONSTRAINT `patient_device_patient_base_patient_uid_fk` FOREIGN KEY (`patient_uid`) REFERENCES `patient_base` (`patient_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient_device`
--

LOCK TABLES `patient_device` WRITE;
/*!40000 ALTER TABLE `patient_device` DISABLE KEYS */;
/*!40000 ALTER TABLE `patient_device` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient_doctor`
--

DROP TABLE IF EXISTS `patient_doctor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patient_doctor` (
  `pd_id` bigint NOT NULL,
  `patient_uid` bigint DEFAULT NULL,
  `doctor_uid` bigint DEFAULT NULL,
  `care` tinyint DEFAULT NULL,
  PRIMARY KEY (`pd_id`),
  KEY `patient_doctor_doctor_base_doctor_uid_fk` (`doctor_uid`),
  KEY `patient_doctor_patient_base_patient_uid_fk` (`patient_uid`),
  CONSTRAINT `patient_doctor_doctor_base_doctor_uid_fk` FOREIGN KEY (`doctor_uid`) REFERENCES `doctor_base` (`doctor_uid`),
  CONSTRAINT `patient_doctor_patient_base_patient_uid_fk` FOREIGN KEY (`patient_uid`) REFERENCES `patient_base` (`patient_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='医患绑定表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient_doctor`
--

LOCK TABLES `patient_doctor` WRITE;
/*!40000 ALTER TABLE `patient_doctor` DISABLE KEYS */;
INSERT INTO `patient_doctor` VALUES (39,120,123,0),(40,123,123,1),(41,124,123,1),(42,120,1813086790381527042,0),(43,123,1813086790381527042,1),(44,124,1813086790381527042,0),(45,1811361598705803266,1813086790381527042,0),(46,1812765560231186433,1813086790381527042,1),(1817447092837445634,1817203659879239681,1813138006243344385,NULL);
/*!40000 ALTER TABLE `patient_doctor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient_now_disease`
--

DROP TABLE IF EXISTS `patient_now_disease`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patient_now_disease` (
  `pdd_id` bigint NOT NULL COMMENT '表唯一id，主键',
  `patient_uid` bigint DEFAULT NULL,
  `disease` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '疾病名称',
  `disease_time` date DEFAULT NULL COMMENT '确诊日期 YYYY-mm-dd',
  PRIMARY KEY (`pdd_id`),
  KEY `patient_now_disease_patient_base_patient_uid_fk` (`patient_uid`),
  CONSTRAINT `patient_now_disease_patient_base_patient_uid_fk` FOREIGN KEY (`patient_uid`) REFERENCES `patient_base` (`patient_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='患者当前疾病信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient_now_disease`
--

LOCK TABLES `patient_now_disease` WRITE;
/*!40000 ALTER TABLE `patient_now_disease` DISABLE KEYS */;
INSERT INTO `patient_now_disease` VALUES (1234,123,'da','2024-07-07');
/*!40000 ALTER TABLE `patient_now_disease` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `persure_heart_rate`
--

DROP TABLE IF EXISTS `persure_heart_rate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `persure_heart_rate` (
  `sdh_id` bigint NOT NULL COMMENT '表唯一id，主键',
  `systolic` float DEFAULT NULL COMMENT '收缩压，高压（单位：mmHg）',
  `diastolic` float DEFAULT NULL COMMENT '舒张压，低压（单位：mmHg）',
  `heart_rate` int DEFAULT NULL,
  `upload_time` datetime DEFAULT NULL COMMENT '记录日期保存到时间级别 YYYY-mm-dd HH:ii:ss',
  `patient_uid` bigint DEFAULT NULL COMMENT '患者唯一id',
  `sdh_classification` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '自动生成，具体分为：一级高血压低危，一级高血压中危，一级高血压高危，二级高血压中危，二级高血压高危，三级高血压高危',
  `risk_assessment` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`sdh_id`),
  KEY `persure_heart_rate_patient_base_patient_uid_fk` (`patient_uid`),
  CONSTRAINT `persure_heart_rate_patient_base_patient_uid_fk` FOREIGN KEY (`patient_uid`) REFERENCES `patient_base` (`patient_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='血压心率展示';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `persure_heart_rate`
--

LOCK TABLES `persure_heart_rate` WRITE;
/*!40000 ALTER TABLE `persure_heart_rate` DISABLE KEYS */;
INSERT INTO `persure_heart_rate` VALUES (9,42,12,32,'1998-09-12 23:03:36',123,'未分类',NULL),(19,42,12,32,'1998-09-12 23:03:36',123,'未分类','偏低'),(20,84,13,25,'2024-07-22 14:27:27',123,'一级高血压低危','偏低'),(40,84,30,91,'1990-10-31 19:56:30',123,NULL,NULL),(61,228,165,92,'2024-07-11 12:32:32',123,'三级高血压高危','重度'),(62,148,80,92,'2024-07-11 12:32:32',120,'一级高血压高危','中度'),(123,184,130,91,'1990-10-31 19:56:30',123,'三级高血压高危','偏低'),(124,119,69,88,'2024-07-07 22:02:55',123,NULL,'正常高值'),(125,180,170,80,'2024-07-07 22:03:30',123,NULL,'轻度'),(126,180,120,56,'2024-07-10 17:52:51',120,'一级高血压低危','重度'),(141,84,30,91,'1992-10-26 19:56:30',123,'一级高血压低危','中度'),(432,33,333,26,'2024-07-22 10:04:34',123,'一级高血压高危','中度'),(1111,1111,200,55,'2024-07-22 15:12:49',123,'三级高血压高危','正常高值'),(1112,159,159,90,'2024-07-22 15:13:33',123,'一级高血压中危','偏低'),(1113,126,100,35,'2024-07-22 15:14:42',123,'一级高血压高危','正常'),(1114,127,243,24,'2024-07-22 15:15:30',123,'二级高血压中危','重度'),(2222,210,120,33,'2024-07-27 17:56:48',123,'一级高血压低危',NULL),(2223,220,133,31,'2024-07-27 18:59:16',123,'一级高血压高危',NULL),(2224,222,133,44,'2024-07-27 19:58:51',123,'一级高血压中危',NULL),(2225,130,80,80,'2024-07-27 11:42:37',1811361598705803266,'二级高血压中危',NULL),(2233,160,100,100,'2024-07-27 12:05:15',124,'三级高血压高危',NULL);
/*!40000 ALTER TABLE `persure_heart_rate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pressure_anomaly`
--

DROP TABLE IF EXISTS `pressure_anomaly`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pressure_anomaly` (
  `pa_id` bigint NOT NULL,
  `patient_uid` bigint DEFAULT NULL,
  `date` date DEFAULT NULL,
  `severe` int DEFAULT '0',
  `moderate` int DEFAULT '0',
  `mild` int DEFAULT '0',
  `elevated` int DEFAULT '0',
  `low` int DEFAULT '0',
  `all_num` int DEFAULT '0',
  PRIMARY KEY (`pa_id`),
  KEY `patientUidConstraint` (`patient_uid`),
  CONSTRAINT `patientUidConstraint` FOREIGN KEY (`patient_uid`) REFERENCES `patient_base` (`patient_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pressure_anomaly`
--

LOCK TABLES `pressure_anomaly` WRITE;
/*!40000 ALTER TABLE `pressure_anomaly` DISABLE KEYS */;
INSERT INTO `pressure_anomaly` VALUES (1810225092562747394,123,'2024-07-11',1,0,0,0,0,1),(1810225092562747395,120,'2024-07-11',1,1,1,1,1,10),(1810225092562747396,124,'2024-07-11',1,0,0,0,0,2);
/*!40000 ALTER TABLE `pressure_anomaly` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_message`
--

DROP TABLE IF EXISTS `sys_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_message` (
  `notification_id` bigint NOT NULL COMMENT '通知唯一标识符',
  `doctor_uid` bigint DEFAULT NULL,
  `patient_uid` bigint DEFAULT NULL,
  `message_type` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '消息类型：用药提醒、医生提醒',
  `json_text` text COLLATE utf8mb4_general_ci COMMENT '存储JSON字符串',
  `sent_date` datetime DEFAULT NULL COMMENT '通知发送日期和时间',
  `is_read` tinyint DEFAULT NULL COMMENT '是否已读',
  PRIMARY KEY (`notification_id`),
  KEY `sys_message_doctor_base_doctor_uid_fk` (`doctor_uid`),
  KEY `sys_message_patient_base_patient_uid_fk` (`patient_uid`),
  CONSTRAINT `sys_message_doctor_base_doctor_uid_fk` FOREIGN KEY (`doctor_uid`) REFERENCES `doctor_base` (`doctor_uid`),
  CONSTRAINT `sys_message_patient_base_patient_uid_fk` FOREIGN KEY (`patient_uid`) REFERENCES `patient_base` (`patient_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统消息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_message`
--

LOCK TABLES `sys_message` WRITE;
/*!40000 ALTER TABLE `sys_message` DISABLE KEYS */;
INSERT INTO `sys_message` VALUES (74,123,123,NULL,NULL,'1976-10-19 06:34:08',NULL),(78,123,123,NULL,NULL,'1976-10-19 06:34:08',NULL),(123,123,123,NULL,NULL,'2024-07-05 23:54:33',NULL),(1812860461851353089,123,123,'医生提醒','{\n    \"message\": \"amet veniam id Excepteur voluptate\"\n}','2024-07-15 22:43:10',0),(1812862852327809026,123,123,'医生提醒','{\n    \"message\": \"amet veniam id Excepteur voluptate\"\n}','2024-07-15 22:52:40',0),(1812863998098423810,123,123,'医生提醒','{\n    \"message\": \"amet veniam id Excepteur voluptate\"\n}','2024-07-15 22:57:13',0),(1812865037115613185,123,123,'医生提醒','{\n    \"message\": \"amet veniam id Excepteur voluptate\"\n}','2024-07-15 23:01:21',0),(1812869106567753730,123,123,'医生提醒','laboris eu Excepteur ipsum','2024-07-15 23:17:31',0),(1812870717465702402,123,123,'医生提醒','laboris eu Excepteur ipsum','2024-07-15 23:23:55',0),(1812871663457513473,123,123,'医生提醒','laboris eu Excepteur ipsum','2024-07-15 23:27:41',0),(1812875219686227969,123,123,'医生提醒','laboris eu Excepteur ipsum','2024-07-15 23:41:49',0),(1812877565073829890,123,123,'医生提醒','enim dolor do minim in','2024-07-15 23:51:08',0),(1812888344066011137,123,123,'医生提醒','nulla pariatur','2024-07-16 00:33:58',0),(1812888899895177217,123,123,'医生提醒','nulla pariatur','2024-07-16 00:36:10',0),(1813148187158949889,123,123,'医生提醒','labore','2024-07-16 17:46:29',0),(1813150000914411522,1813085631554011137,1812765560231186433,'医生提醒','labore','2024-07-16 17:53:42',0),(1813150099447001090,1813085631554011137,1812765560231186433,'医生提醒','labore','2024-07-16 17:54:05',0),(1813151831371591681,1813085631554011137,1812765560231186433,'医生提醒','labore','2024-07-16 18:00:58',0),(1813151853072920577,1813085631554011137,1812765560231186433,'医生提醒','labore','2024-07-16 18:01:03',0),(1813529076370251777,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-17 19:00:00',0),(1813544175587840002,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-17 20:00:00',0),(1813559275086434305,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-17 21:00:00',0),(1813574374555668481,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-17 22:00:00',0),(1813589474368835586,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-17 23:00:00',0),(1813604573619965953,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-18 00:00:00',0),(1813740469283487745,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-18 09:00:00',0),(1813755568442343425,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-18 10:00:00',0),(1814284051832524801,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-19 21:00:00',0),(1814299150475481090,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-19 22:00:00',0),(1814314249420427266,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-19 23:00:00',0),(1814329349212622850,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-20 00:00:00',0),(1814344448379867138,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-20 01:00:00',0),(1814359548067205122,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-20 02:00:00',0),(1814374647393832962,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-20 03:00:00',0),(1814389746946953218,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-20 04:00:00',0),(1814404846357467138,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-20 05:00:00',0),(1814419945856061441,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-20 06:00:00',0),(1814435045354655745,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-20 07:00:00',0),(1814450144836472833,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-20 08:00:00',0),(1814465244330872834,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-20 09:00:00',0),(1814480343829467137,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-20 10:00:00',0),(1814495443349032961,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-20 11:00:00',0),(1814510542986039297,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-20 12:00:00',0),(1814525642342027266,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-20 13:00:00',0),(1814540741798678529,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-20 14:00:00',0),(1814555841305661441,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-20 15:00:00',0),(1814570940804255746,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-20 16:00:00',0),(1814586040298655745,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-20 17:00:00',0),(1814601139797250049,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-20 18:00:00',0),(1814616239279067138,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-20 19:00:00',0),(1814631338773467137,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-20 20:00:00',0),(1814646438284644354,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-20 21:00:00',0),(1814661537766461442,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-20 22:00:00',0),(1814676637281832962,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-20 23:00:00',0),(1814691736784621569,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-21 00:00:00',0),(1814706836262244354,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-21 01:00:00',0),(1814721935769227266,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-21 02:00:00',0),(1814737035246850050,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-21 03:00:00',0),(1814752134724472834,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-21 04:00:00',0),(1814767234223067138,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-21 05:00:00',0),(1814782333742632962,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-21 06:00:00',0),(1814797433228644353,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-21 07:00:00',0),(1814812532710461442,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-21 08:00:00',0),(1814827632209055745,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-21 09:00:00',0),(1814842731695067137,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-21 10:00:00',0),(1814857831189467138,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-21 11:00:00',0),(1814872930696450050,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-21 12:00:00',0),(1814888030564143105,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-21 13:00:00',0),(1814918229523574785,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-21 15:00:00',0),(1814933328682430466,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-21 16:00:00',0),(1814948428499845121,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-21 17:00:00',0),(1814963527901962242,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-21 18:00:00',0),(1814978627169869826,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-21 19:00:00',0),(1814993726668464129,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-21 20:00:00',0),(1815008826141892609,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-21 21:00:00',0),(1815023925653069826,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-21 22:00:00',0),(1815039025139081217,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-21 23:00:00',0),(1815054124633481218,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-22 00:00:00',0),(1815069224127881217,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-22 01:00:00',0),(1815084323618086913,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-22 02:00:00',0),(1815099423108292609,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-22 03:00:00',0),(1815114529032560641,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-22 04:00:02',0),(1815129622105481218,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-22 05:00:00',0),(1815144721595686914,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-22 06:00:00',0),(1815159821102669826,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-22 07:00:00',0),(1815174920571904002,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-22 08:00:00',0),(1815190020087275521,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-22 09:00:00',0),(1815205119573286913,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-22 10:00:00',0),(1815220219055104002,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-22 11:00:00',0),(1815235318557892610,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-22 12:00:00',0),(1815250418035515393,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-22 13:00:00',0),(1815265517525721089,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-22 14:00:00',0),(1815280617045286914,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-22 15:00:00',0),(1815295716577443841,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-22 16:00:00',0),(1815295717082771458,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-22 16:00:00',0),(1815310816608714753,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-22 17:00:00',0),(1815325915658510338,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-22 18:00:00',0),(1815341015077412865,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-22 19:00:00',0),(1815356114504704002,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-22 20:00:00',0),(1815371214204624897,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-22 21:00:00',0),(1815386313560612866,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-22 22:00:00',0),(1815401413021458433,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-22 23:00:00',0),(1815416512624910337,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-23 00:00:00',0),(1815431612027035650,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-23 01:00:00',0),(1815446711483686913,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-23 02:00:00',0),(1815461810969698305,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-23 03:00:00',0),(1815476910485069826,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-23 04:00:00',0),(1815492009962692609,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-23 05:00:00',0),(1815507109482258433,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-23 06:00:00',0),(1815522208951492610,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-23 07:00:00',0),(1815537308458475522,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-23 08:00:00',0),(1815552407973847041,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-23 09:00:00',0),(1815567507426304001,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-23 10:00:00',0),(1815582606929092610,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-23 11:00:00',0),(1815597706427686913,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-23 12:00:00',0),(1816382880849555457,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-25 16:00:00',0),(1816397987860148226,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-25 17:00:02',0),(1816413079309873153,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-25 18:00:00',0),(1816413079491211265,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-25 18:00:00',0),(1816458294558093314,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-25 20:59:40',0),(1816458837350379522,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-25 21:01:50',0),(1816669771226460161,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-26 11:00:00',0),(1816684870674677762,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-26 12:00:00',0),(1816699969921613825,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-26 13:00:00',0),(1816715069344710658,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-26 14:00:00',0),(1816730168780390401,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-26 15:00:00',0),(1816730418483978242,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-26 15:01:00',0),(1816745268291567618,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-26 16:00:00',0),(1816760368163475457,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-26 17:00:00',0),(1816775467737559041,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-26 18:00:00',0),(1816790585955332098,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-26 19:00:05',0),(1816805666089766914,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-26 20:00:00',0),(1816815756292284418,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-26 20:40:06',0),(1816820765650202625,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-26 21:00:00',0),(1816821797529751554,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-26 21:04:06',0),(1816835865765498882,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-26 22:00:00',0),(1816850333644787714,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-26 22:57:30',0),(1816850964883431425,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-26 23:00:00',0),(1817017059555713026,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-27 10:00:00',0),(1817020912669712385,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-27 10:15:19',0),(1817032158567620609,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-27 11:00:00',0),(1817032158753263617,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-27 11:00:00',0),(1817032159133995009,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-27 11:00:00',0),(1817047258058850305,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-27 12:00:00',0),(1817047258087186433,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-27 12:00:00',0),(1817047258234097665,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-27 12:00:00',0),(1817062357661298689,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-27 13:00:00',0),(1817062357778952193,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-27 13:00:00',0),(1817077457235603458,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-27 14:00:00',0),(1817092556763500546,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-27 15:00:00',0),(1817107656320765954,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-27 16:00:00',0),(1817111055972851714,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-27 16:13:31',0),(1817122755672559617,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-27 17:00:00',0),(1817137855179542530,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-27 18:00:00',0),(1817152954665553921,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-27 19:00:00',0),(1817168054109622274,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-27 20:00:00',0),(1817180521577492482,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-27 20:49:33',0),(1817183153561972738,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-27 21:00:00',0),(1817183153725657089,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-27 21:00:00',0),(1817198253022818306,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-27 22:00:00',0),(1817198253081645057,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-27 22:00:00',0),(1817213352643059713,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-27 23:00:00',0),(1817213352781565953,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-27 23:00:00',0),(1817379447005966337,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-28 10:00:00',0),(1817379447148756994,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-28 10:00:00',0),(1817394546567475201,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-28 11:00:00',0),(1817394546597019649,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-28 11:00:00',0),(1817409646050324481,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-28 12:00:00',0),(1817409661681647617,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-28 12:00:04',0),(1817411295752941570,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-28 12:06:33',0),(1817424745506127873,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-28 13:00:00',0),(1817424745627578370,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-28 13:00:00',0),(1817437116627615746,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-28 13:49:10',0),(1817439844737155074,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-28 14:00:00',0),(1817454944214777858,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-28 15:00:00',0),(1817470043755315201,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-28 16:00:00',0),(1817542680451907585,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-28 20:48:38',0),(1817545541239865345,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-28 21:00:00',0),(1817754104658837506,NULL,NULL,'医生提醒',NULL,'2024-07-29 10:48:45',0),(1817756934278922241,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-29 11:00:00',0),(1817772033580384258,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-29 12:00:00',0),(1817792175844560898,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-29 13:20:02',0),(1817802232627924994,NULL,123,'用药提醒','{\"drugName\":\"测试药物\",\"dose\":\"1\",\"unit\":\"毫克\"}','2024-07-29 14:00:00',0);
/*!40000 ALTER TABLE `sys_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_feedback`
--

DROP TABLE IF EXISTS `user_feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_feedback` (
  `prr_id` bigint NOT NULL,
  `patient_uid` bigint DEFAULT NULL,
  `remark_title` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '反馈标题，限定80字以内',
  `remark` text COLLATE utf8mb4_general_ci COMMENT '反馈内容',
  `remark_time` datetime DEFAULT NULL COMMENT '填写反馈时间',
  PRIMARY KEY (`prr_id`),
  KEY `user_feedback_patient_base_patient_uid_fk` (`patient_uid`),
  CONSTRAINT `user_feedback_patient_base_patient_uid_fk` FOREIGN KEY (`patient_uid`) REFERENCES `patient_base` (`patient_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_feedback`
--

LOCK TABLES `user_feedback` WRITE;
/*!40000 ALTER TABLE `user_feedback` DISABLE KEYS */;
INSERT INTO `user_feedback` VALUES (1808779506109157378,123,'test','这个是测试内容','1993-07-18 14:41:21');
/*!40000 ALTER TABLE `user_feedback` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-07-30 10:17:17
