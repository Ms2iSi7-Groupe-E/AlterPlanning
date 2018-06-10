-- phpMyAdmin SQL Dump
-- version 4.8.0.1
-- https://www.phpmyadmin.net/
--
-- Host: db
-- Generation Time: Jun 10, 2018 at 08:51 PM
-- Server version: 10.3.6-MariaDB-1:10.3.6+maria~jessie
-- PHP Version: 7.2.4

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

-- --------------------------------------------------------

--
-- Table structure for table `calendars`
--

CREATE TABLE `calendars` (
  `id` int(11) NOT NULL,
  `stagiaireId` int(11) DEFAULT NULL,
  `entrepriseId` int(11) DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `state` enum('DRAFT','PROPOSAL','VALIDATED') NOT NULL DEFAULT 'DRAFT',
  `isModel` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `calendar_constraints`
--

CREATE TABLE `calendar_constraints` (
  `id` int(11) NOT NULL,
  `calendarId` int(11) NOT NULL,
  `constraintType` varchar(255) NOT NULL,
  `constraintValue` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `calendar_cours`
--

CREATE TABLE `calendar_cours` (
  `calendarId` int(11) NOT NULL,
  `coursId` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `calendar_models`
--

CREATE TABLE `calendar_models` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `calendarId` int(11) NOT NULL,
  `createdAt` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `history`
--

CREATE TABLE `history` (
  `id` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `description` text NOT NULL,
  `createdAt` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `independant_modules`
--

CREATE TABLE `independant_modules` (
  `id` int(11) NOT NULL,
  `shortName` varchar(20) NOT NULL,
  `longName` varchar(200) NOT NULL,
  `startDate` date NOT NULL,
  `endDate` date NOT NULL,
  `hours` int(11) NOT NULL,
  `codeLieu` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `module_requirements`
--

CREATE TABLE `module_requirements` (
  `id` int(11) NOT NULL,
  `moduleId` int(11) NOT NULL,
  `requiredModuleId` int(11) NOT NULL,
  `isOr` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `parameters`
--

CREATE TABLE `parameters` (
  `parameter_key` varchar(50) NOT NULL,
  `parameter_value` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `is_active` bit(1) NOT NULL,
  `is_admin` bit(1) NOT NULL,
  `email` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `password` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `is_active`, `is_admin`, `email`, `name`, `password`) VALUES
(1, b'1', b'1', 'admin@admin.com', 'admin', '$2a$10$XGIoyQZeI7iIuYQCBsWTe.kmssxei5JBwiILTmytloBXXKJDGzt7O');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `calendars`
--
ALTER TABLE `calendars`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `calendar_constraints`
--
ALTER TABLE `calendar_constraints`
  ADD PRIMARY KEY (`id`),
  ADD KEY `calendarId` (`calendarId`);

--
-- Indexes for table `calendar_cours`
--
ALTER TABLE `calendar_cours`
  ADD PRIMARY KEY (`calendarId`,`coursId`);

--
-- Indexes for table `calendar_models`
--
ALTER TABLE `calendar_models`
  ADD PRIMARY KEY (`id`),
  ADD KEY `calendarId` (`calendarId`);

--
-- Indexes for table `history`
--
ALTER TABLE `history`
  ADD PRIMARY KEY (`id`),
  ADD KEY `userId` (`userId`);

--
-- Indexes for table `module_requirements`
--
ALTER TABLE `module_requirements`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `parameters`
--
ALTER TABLE `parameters`
  ADD UNIQUE KEY `parameter_key` (`parameter_key`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `calendars`
--
ALTER TABLE `calendars`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- AUTO_INCREMENT for table `calendar_constraints`
--
ALTER TABLE `calendar_constraints`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `calendar_models`
--
ALTER TABLE `calendar_models`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `history`
--
ALTER TABLE `history`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `module_requirements`
--
ALTER TABLE `module_requirements`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `calendar_constraints`
--
ALTER TABLE `calendar_constraints`
  ADD CONSTRAINT `fk_calendarconstraints_calendar` FOREIGN KEY (`calendarId`) REFERENCES `calendars` (`id`);

--
-- Constraints for table `calendar_cours`
--
ALTER TABLE `calendar_cours`
  ADD CONSTRAINT `fk_calendarcours_calendar` FOREIGN KEY (`calendarId`) REFERENCES `calendars` (`id`);

--
-- Constraints for table `calendar_models`
--
ALTER TABLE `calendar_models`
  ADD CONSTRAINT `fk_calendarmodels_calendar` FOREIGN KEY (`calendarId`) REFERENCES `calendars` (`id`);

--
-- Constraints for table `history`
--
ALTER TABLE `history`
  ADD CONSTRAINT `fk_history_user` FOREIGN KEY (`userId`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
