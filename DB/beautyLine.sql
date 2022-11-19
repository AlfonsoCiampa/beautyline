-- -------------------------------------------------------------
-- TablePlus 3.11.0(352)
--
-- https://tableplus.com/
--
-- Database: beautyLine
-- Generation Time: 2022-11-19 16:26:51.4870
-- -------------------------------------------------------------


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


CREATE TABLE `account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdOn` datetime DEFAULT CURRENT_TIMESTAMP,
  `lastModified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `referenceId` bigint(20) DEFAULT NULL,
  `referenceType` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`) USING BTREE,
  KEY `referenceId` (`referenceId`),
  CONSTRAINT `account_ibfk_1` FOREIGN KEY (`referenceId`) REFERENCES `consumer` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `booking` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdOn` datetime DEFAULT CURRENT_TIMESTAMP,
  `lastModified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `consumerId` bigint(20) NOT NULL,
  `endTime` datetime(6) NOT NULL,
  `note` longtext,
  `startTime` datetime(6) NOT NULL,
  `treatmentId` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `treatmentId` (`treatmentId`),
  CONSTRAINT `booking_ibfk_1` FOREIGN KEY (`treatmentId`) REFERENCES `treatment` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `bookingConfiguration` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdOn` datetime DEFAULT CURRENT_TIMESTAMP,
  `lastModified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `date` varchar(255) NOT NULL,
  `endTime` time NOT NULL,
  `startTime` time NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `consumer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdOn` datetime DEFAULT CURRENT_TIMESTAMP,
  `lastModified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `name` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `surname` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdOn` datetime DEFAULT CURRENT_TIMESTAMP,
  `lastModified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `description` longtext NOT NULL,
  `endDate` datetime(6) NOT NULL,
  `name` varchar(255) NOT NULL,
  `startDate` datetime(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `gift` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdOn` datetime DEFAULT CURRENT_TIMESTAMP,
  `lastModified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `consumerId` bigint(20) NOT NULL,
  `orderItemId` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhwawc25oc5tjn04s1a6a7r6q2` (`orderItemId`),
  KEY `consumerId` (`consumerId`),
  CONSTRAINT `FKhwawc25oc5tjn04s1a6a7r6q2` FOREIGN KEY (`orderItemId`) REFERENCES `orderItem` (`id`),
  CONSTRAINT `gift_ibfk_1` FOREIGN KEY (`consumerId`) REFERENCES `consumer` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdOn` datetime DEFAULT CURRENT_TIMESTAMP,
  `lastModified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `consumerId` bigint(20) NOT NULL,
  `date` datetime(6) NOT NULL,
  `note` longtext,
  `packageCode` varchar(255) DEFAULT NULL,
  `price` decimal(7,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `consumerId` (`consumerId`),
  KEY `packageCode` (`packageCode`),
  CONSTRAINT `order_ibfk_1` FOREIGN KEY (`consumerId`) REFERENCES `consumer` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `order_ibfk_2` FOREIGN KEY (`packageCode`) REFERENCES `package` (`code`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `orderItem` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdOn` datetime DEFAULT CURRENT_TIMESTAMP,
  `lastModified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `orderId` bigint(20) NOT NULL,
  `price` decimal(7,2) NOT NULL,
  `productId` bigint(20) NOT NULL,
  `retrieved` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKp7tcag1ev6iglxorif3uer8fk` (`orderId`),
  KEY `productId` (`productId`),
  CONSTRAINT `FKp7tcag1ev6iglxorif3uer8fk` FOREIGN KEY (`orderId`) REFERENCES `order` (`id`),
  CONSTRAINT `orderitem_ibfk_1` FOREIGN KEY (`productId`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `package` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdOn` datetime DEFAULT CURRENT_TIMESTAMP,
  `lastModified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `description` longtext NOT NULL,
  `endDate` datetime(6) NOT NULL,
  `name` varchar(255) NOT NULL,
  `startDate` datetime(6) NOT NULL,
  `code` varchar(255) NOT NULL,
  `price` decimal(7,2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `packageItem` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdOn` datetime DEFAULT CURRENT_TIMESTAMP,
  `lastModified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `packageId` bigint(20) NOT NULL,
  `productId` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9cp5pwydbhk0js5gsrutf6ydu` (`packageId`),
  KEY `productId` (`productId`),
  CONSTRAINT `FK9cp5pwydbhk0js5gsrutf6ydu` FOREIGN KEY (`packageId`) REFERENCES `package` (`id`),
  CONSTRAINT `packageitem_ibfk_1` FOREIGN KEY (`productId`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdOn` datetime DEFAULT CURRENT_TIMESTAMP,
  `lastModified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `description` longtext NOT NULL,
  `name` varchar(255) NOT NULL,
  `obscure` bit(1) NOT NULL,
  `price` decimal(7,2) NOT NULL,
  `quantity` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `serviceComment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdOn` datetime DEFAULT CURRENT_TIMESTAMP,
  `lastModified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `consumerId` bigint(20) NOT NULL,
  `date` datetime(6) NOT NULL,
  `description` longtext NOT NULL,
  `serviceId` bigint(20) NOT NULL,
  `serviceType` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `treatment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdOn` datetime DEFAULT CURRENT_TIMESTAMP,
  `lastModified` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `description` longtext NOT NULL,
  `name` varchar(255) NOT NULL,
  `obscure` bit(1) NOT NULL,
  `price` decimal(7,2) NOT NULL,
  `duration` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;