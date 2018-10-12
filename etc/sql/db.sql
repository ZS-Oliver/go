-- MySQL dump 10.13  Distrib 5.7.20, for Win64 (x86_64)
--
-- Host: localhost    Database: go
-- ------------------------------------------------------
-- Server version	5.7.20

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `tbl_class`
--

DROP TABLE IF EXISTS `tbl_class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_class` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `audition_time` int(11) NOT NULL COMMENT '试听时间',
  `op_id` int(11) DEFAULT NULL COMMENT '创建人',
  `total` int(11) NOT NULL COMMENT '总容量',
  `sids` varchar(50) DEFAULT NULL COMMENT '学员id，用:隔开',
  `site` char(50) DEFAULT NULL COMMENT '地点',
  `valid` tinyint(4) DEFAULT '1' COMMENT '是否有效',
  PRIMARY KEY (`id`),
  KEY `tbl_class_tbl_employee_id_fk` (`eid`),
  CONSTRAINT `tbl_class_tbl_employee_id_fk` FOREIGN KEY (`op_id`) REFERENCES `tbl_employee` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='试听班级';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_employee`
--

DROP TABLE IF EXISTS `tbl_employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_employee` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `mid` int(11) NOT NULL COMMENT '市场经理id',
  `name` char(20) NOT NULL COMMENT '姓名',
  `phone` char(11) NOT NULL COMMENT '手机号',
  `school` char(50) DEFAULT NULL COMMENT '所属学校',
  `auth` tinyint(4) NOT NULL COMMENT '权限',
  `init_pwd` tinyint(4) DEFAULT '1' COMMENT '是否为初始密码',
  `passwd` char(32) NOT NULL DEFAULT '25D55AD283AA400AF464C76D713C07AD' COMMENT '密码,初始密码为12345678',
  `valid` tinyint(4) DEFAULT '1' COMMENT '是否有效',
  PRIMARY KEY (`id`),
  UNIQUE KEY `tbl_employee_phone_uindex` (`phone`),
  KEY `tbl_employee_tbl_marketing_manager_id_fk` (`mid`),
  CONSTRAINT `tbl_employee_tbl_marketing_manager_id_fk` FOREIGN KEY (`mid`) REFERENCES `tbl_marketing_manager` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='员工表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_journal`
--

DROP TABLE IF EXISTS `tbl_journal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_journal` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `op_id` int(11) NOT NULL COMMENT '跟进人',
  `sid` int(11) NOT NULL COMMENT '学员id',
  `ctime` int(11) DEFAULT NULL COMMENT '创建时间',
  `cur_stage` tinyint(4) DEFAULT NULL COMMENT '当前阶段',
  `content` char(100) DEFAULT NULL COMMENT '内容',
  `next_state` tinyint(4) DEFAULT NULL COMMENT '学员下一状态',
  `next_degree` tinyint(4) DEFAULT NULL COMMENT '学员下一重要程度',
  `next_stage` tinyint(4) DEFAULT NULL COMMENT '学员下一阶段',
  `excepted_date` char(8) DEFAULT NULL COMMENT '意向日期',
  `valid` tinyint(4) DEFAULT '1' COMMENT '是否有效',
  PRIMARY KEY (`id`),
  KEY `tbl_journal_tbl_employee_id_fk` (`eid`),
  KEY `tbl_journal_tbl_student_id_fk` (`sid`),
  CONSTRAINT `tbl_journal_tbl_employee_id_fk` FOREIGN KEY (`op_id`) REFERENCES `tbl_employee` (`id`),
  CONSTRAINT `tbl_journal_tbl_student_id_fk` FOREIGN KEY (`sid`) REFERENCES `tbl_student` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='成长轨迹';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_marketing_manager`
--

DROP TABLE IF EXISTS `tbl_marketing_manager`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_marketing_manager` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` char(20) NOT NULL COMMENT '名字',
  `phone` char(11) NOT NULL COMMENT '手机号',
  `school` char(50) NOT NULL COMMENT '学校',
  `auth` tinyint(4) DEFAULT NULL COMMENT '权限',
  `init_pwd` tinyint(4) DEFAULT NULL COMMENT '是否初始密码',
  `passwd` char(32) DEFAULT '25D55AD283AA400AF464C76D713C07AD' COMMENT '密码，默认12345678',
  `valid` tinyint(4) DEFAULT '1' COMMENT '是否有效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='市场部经理';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_source`
--

DROP TABLE IF EXISTS `tbl_source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_source` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `type` tinyint(4) NOT NULL COMMENT '种类',
  `source` char(50) DEFAULT NULL COMMENT '具体来源',
  `valid` tinyint(4) DEFAULT '1' COMMENT '是否有效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='学生来源';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_student`
--

DROP TABLE IF EXISTS `tbl_student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_student` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` char(20) NOT NULL COMMENT '姓名',
  `nickname` char(20) DEFAULT NULL COMMENT '昵称',
  `birthday` char(8) DEFAULT NULL COMMENT '生日',
  `parent_name` char(20) DEFAULT NULL COMMENT '家长姓名',
  `phone` char(11) NOT NULL COMMENT '手机号',
  `alternate_number` char(11) DEFAULT NULL COMMENT '备用号码',
  `wechat` char(50) DEFAULT NULL COMMENT '微信号',
  `addr` char(50) DEFAULT NULL COMMENT '家庭住址',
  `source_id` int(11) DEFAULT NULL COMMENT '来源id',
  `ctime` int(11) DEFAULT NULL COMMENT '创建时间',
  `op_id` int(11) DEFAULT NULL COMMENT '创建人id',
  `state` tinyint(4) DEFAULT NULL COMMENT '学员状态',
  `degree` tinyint(4) DEFAULT NULL COMMENT '重要程度',
  `sales_stage` tinyint(4) DEFAULT NULL COMMENT '销售阶段',
  `expected_date` char(8) DEFAULT NULL COMMENT '意向日期',
  `contrate_date` char(8) DEFAULT NULL COMMENT '签约日期',
  `valid` tinyint(4) DEFAULT '1' COMMENT '是否有效',
  PRIMARY KEY (`id`),
  UNIQUE KEY `tbl_student_phone_uindex` (`phone`),
  KEY `tbl_student_tbl_employee_id_fk` (`eid`),
  KEY `tbl_student_tbl_source_id_fk` (`sourceId`),
  CONSTRAINT `tbl_student_tbl_employee_id_fk` FOREIGN KEY (`op_id`) REFERENCES `tbl_employee` (`id`),
  CONSTRAINT `tbl_student_tbl_source_id_fk` FOREIGN KEY (`source_id`) REFERENCES `tbl_source` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='学生表';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-09-29 16:54:46
