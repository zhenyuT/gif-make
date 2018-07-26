/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.21-log : Database - gif_make
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`gif_make` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `gif_make`;

/*Table structure for table `gif_config` */

DROP TABLE IF EXISTS `gif_config`;

CREATE TABLE `gif_config` (
  `gif_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `gif_name` varchar(30) NOT NULL DEFAULT '' COMMENT '动图名称',
  `default_sentences` varchar(1000) NOT NULL DEFAULT '' COMMENT '默认对话，json数组格式',
  `preview_img` varchar(200) NOT NULL DEFAULT '' COMMENT '预览图地址',
  `add_time` datetime NOT NULL COMMENT '添加时间',
  `add_user` varchar(50) NOT NULL DEFAULT '' COMMENT '添加人',
  PRIMARY KEY (`gif_id`),
  UNIQUE KEY `GifName` (`gif_name`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8;

/*Data for the table `gif_config` */

insert  into `gif_config`(`gif_id`,`gif_name`,`default_sentences`,`preview_img`,`add_time`,`add_user`) values (1,'sorry','[\"好啊\",\"就算你是一流工程师\",\"就算你出报告再完美\",\"我叫你改报告你就要改\",\"毕竟我是客户\",\"客户了不起啊\",\"sorry 客户真的了不起\",\"以后叫他天天改报告\",\"天天改 天天改\"]','/sorry.jpg','2018-07-23 10:40:10','tianzy'),(2,'dongdong','[\"迟迟\",\"mu~~a\",\"这么多人 怪不好意思\"]','/dongdong.png','2018-07-23 10:59:46','tianzy'),(4,'chichi','[\"傻屌你说什么\", \"看我超级变身\",\"叼叼叼 劳资服了\"]','/chichi.jpg','2018-07-23 11:59:37','tianzy'),(34,'琪琪','[\"琪琪，你的包要换\",\"换你妹\",\"再换包我打死你\"]','/34-qiqi.jpg','2018-07-26 11:49:46',''),(35,'肖鸡头','[\"来笑一个\",\"别闹\"]','/35-xiaojitou.jpg','2018-07-26 14:46:57',''),(43,'dd','[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\"]','/43-dd.jpg','2018-07-26 15:15:26',''),(45,'嘎嘎嘎','[\"哈哈\",\"就算你是舔哥\"]','/45-gagaga.jpg','2018-07-26 15:18:54','');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
