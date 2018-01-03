-- phpMyAdmin SQL Dump
-- version 4.7.7
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jan 03, 2018 at 10:46 PM
-- Server version: 5.7.20
-- PHP Version: 7.1.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `alter-planning`
--
CREATE DATABASE IF NOT EXISTS `alter-planning` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `alter-planning`;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `uid` varchar(32) NOT NULL,
  `birthday` date DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `email` varchar(255) NOT NULL,
  `is_enabled` bit(1) NOT NULL,
  `firstname` varchar(255) NOT NULL,
  `lastname` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `roles` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`uid`, `birthday`, `city`, `country`, `created_at`, `email`, `is_enabled`, `firstname`, `lastname`, `password`, `roles`) VALUES
('402881a260bdfb7d0160bdfba8e30000', NULL, NULL, NULL, '2018-01-03 22:44:35', 'admin@test.fr', b'1', 'Admin', 'ADMIN', '$2a$10$T9bhNXsqhnO/HclA85QEoeXtJJnVOG713.ztB6pQ91Rz8zxqp8.Uy', 'ROLE_ADMINISTRATOR'),
('402881a260bdfb7d0160bdfba9940001', NULL, NULL, NULL, '2018-01-03 22:44:35', 'user1@test.fr', b'1', 'User1', 'USER1', '$2a$10$dufz14vdiO9sz2GIKralreVRncxFsfV/cI4XQgHpLPVQmSjDj8Z0S', 'ROLE_USER'),
('402881a260bdfb7d0160bdfba9e90002', NULL, NULL, NULL, '2018-01-03 22:44:35', 'user2@test.fr', b'0', 'User2', 'USER2', '$2a$10$7gOdG04mRMbF4boipTy1ceB4lhLb4fWeQjisHAnBP6Ah8AqaxBh6C', 'ROLE_USER'),
('402881a260bdfb7d0160bdfbaa3d0003', NULL, NULL, NULL, '2018-01-03 22:44:35', 'user3@test.fr', b'0', 'User3', 'USER3', '$2a$10$xi2EOpetIaXo2HmrM99ptusLDB0KsdQ5EkmglePnrKLyMhyCAiMVG', 'ROLE_USER'),
('402881a260bdfb7d0160bdfbaa8d0004', NULL, NULL, NULL, '2018-01-03 22:44:35', 'user4@test.fr', b'1', 'User4', 'USER4', '$2a$10$kI1LUHxU1gsPctVC6.6C5eR0CNWzBiya6PRv1EKmFqR0Ym9CJ/I4q', 'ROLE_USER'),
('402881a260bdfb7d0160bdfbaae90005', NULL, NULL, NULL, '2018-01-03 22:44:35', 'user5@test.fr', b'1', 'User5', 'USER5', '$2a$10$ZVpygtE4t2MH/vJzT6XFvOI/M1OClwj0ZzDynApfJXmiFoJ2.CgYG', 'ROLE_USER');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`uid`),
  ADD UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
