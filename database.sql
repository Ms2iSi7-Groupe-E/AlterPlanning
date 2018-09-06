-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le :  mer. 20 juin 2018 à 07:39
-- Version du serveur :  5.7.19
-- Version de PHP :  5.6.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `alter-planning`
--
CREATE DATABASE IF NOT EXISTS `alter-planning` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `alter-planning`;

-- --------------------------------------------------------

--
-- Structure de la table `calendars`
--

DROP TABLE IF EXISTS `calendars`;
CREATE TABLE IF NOT EXISTS `calendars` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `stagiaireId` int(11) DEFAULT NULL,
  `entrepriseId` int(11) DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `state` enum('DRAFT','PROPOSAL','VALIDATED') NOT NULL DEFAULT 'DRAFT',
  `isModel` tinyint(1) NOT NULL DEFAULT '0',
  `createdAt` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `calendar_constraints`
--

DROP TABLE IF EXISTS `calendar_constraints`;
CREATE TABLE IF NOT EXISTS `calendar_constraints` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `calendarId` int(11) NOT NULL,
  `constraintType` enum('AJOUT_FORMATION','AJOUT_MODULE', 'AJOUT_MODULE_INDEPENDANT','AJOUT_PERIODE','EN_MEME_TEMPS_QUE','PAS_EN_MEME_TEMPS_QUE','DISPENSE_PERIODE','DISPENSE_MODULE','A_PARTIR_DE_MODELE','LIEUX','HEURES_MIN','HEURES_MAX') NOT NULL,
  `constraintValue` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `calendarId` (`calendarId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `calendar_cours`
--

DROP TABLE IF EXISTS `calendar_cours`;
CREATE TABLE IF NOT EXISTS `calendar_cours` (
  `calendarId` int(11) NOT NULL,
  `coursId` varchar(100) NOT NULL,
  `isIndependantModule` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`calendarId`,`coursId`,`isIndependantModule`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `calendar_models`
--

DROP TABLE IF EXISTS `calendar_models`;
CREATE TABLE IF NOT EXISTS `calendar_models` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `calendarId` int(11) NOT NULL,
  `createdAt` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `calendarId` (`calendarId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `history`
--

DROP TABLE IF EXISTS `history`;
CREATE TABLE IF NOT EXISTS `history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL,
  `description` text NOT NULL,
  `createdAt` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `userId` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `independant_modules`
--

DROP TABLE IF EXISTS `independant_modules`;
CREATE TABLE IF NOT EXISTS `independant_modules` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `shortName` varchar(20) NOT NULL,
  `longName` varchar(200) NOT NULL,
  `startDate` date NOT NULL,
  `endDate` date NOT NULL,
  `hours` int(11) NOT NULL,
  `codeLieu` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `shortName_uq` (`shortName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `module_requirements`
--

DROP TABLE IF EXISTS `module_requirements`;
CREATE TABLE IF NOT EXISTS `module_requirements` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `moduleId` int(11) NOT NULL,
  `requiredModuleId` int(11) NOT NULL,
  `isOr` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `module_requirements_uq` (`moduleId`,`requiredModuleId`,`isOr`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Structure de la table `parameters`
--

DROP TABLE IF EXISTS `parameters`;
CREATE TABLE IF NOT EXISTS `parameters` (
  `parameter_key` varchar(50) NOT NULL,
  `parameter_value` text,
  UNIQUE KEY `parameter_key` (`parameter_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `parameters`
--

INSERT INTO `parameters` (`parameter_key`, `parameter_value`) VALUES
('COURSE_COLOR_1', '#C0392B'),
('COURSE_COLOR_2', '#9B59B6'),
('COURSE_COLOR_3', '#2980B9'),
('COURSE_COLOR_4', '#1ABC9C'),
('COURSE_COLOR_5', '#27AE60'),
('COURSE_COLOR_6', '#F1C40F'),
('COURSE_COLOR_7', '#D35400'),
('FORMATION_COLOR_1', '#c43f30'),
('FORMATION_COLOR_2', '#9B59B6'),
('FORMATION_COLOR_3', '#2980B9'),
('FORMATION_COLOR_4', '#1ABC9C'),
('FORMATION_COLOR_5', '#27AE60'),
('FORMATION_COLOR_6', '#F1C40F'),
('FORMATION_COLOR_7', '#ce5c10'),
('SWITCH_COLOR_1', '#ebebe0'),
('SWITCH_COLOR_2', '#ff9933'),
('SWITCH_COLOR_3', '#ff0000'),
('SWITCH_VALUE_1', '20'),
('SWITCH_VALUE_2', '30');

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `is_active` bit(1) NOT NULL,
  `is_admin` bit(1) NOT NULL,
  `email` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `password` varchar(200) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `users`
--

INSERT INTO `users` (`id`, `is_active`, `is_admin`, `email`, `name`, `password`) VALUES
(1, b'1', b'1', 'admin@admin.com', 'admin', '$2a$10$XGIoyQZeI7iIuYQCBsWTe.kmssxei5JBwiILTmytloBXXKJDGzt7O');

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `calendar_constraints`
--
ALTER TABLE `calendar_constraints`
  ADD CONSTRAINT `fk_calendarconstraints_calendar` FOREIGN KEY (`calendarId`) REFERENCES `calendars` (`id`);

--
-- Contraintes pour la table `calendar_cours`
--
ALTER TABLE `calendar_cours`
  ADD CONSTRAINT `fk_calendarcours_calendar` FOREIGN KEY (`calendarId`) REFERENCES `calendars` (`id`);

--
-- Contraintes pour la table `calendar_models`
--
ALTER TABLE `calendar_models`
  ADD CONSTRAINT `fk_calendarmodels_calendar` FOREIGN KEY (`calendarId`) REFERENCES `calendars` (`id`);

--
-- Contraintes pour la table `history`
--
ALTER TABLE `history`
  ADD CONSTRAINT `fk_history_user` FOREIGN KEY (`userId`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
