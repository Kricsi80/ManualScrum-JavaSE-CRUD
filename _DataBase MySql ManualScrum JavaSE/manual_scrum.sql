-- phpMyAdmin SQL Dump
-- version 4.9.2
-- https://www.phpmyadmin.net/
--
-- Gép: 127.0.0.1
-- Létrehozás ideje: 2020. Jan 25. 10:16
-- Kiszolgáló verziója: 10.4.11-MariaDB
-- PHP verzió: 7.4.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Adatbázis: `manual_scrum`
--
CREATE DATABASE IF NOT EXISTS `manual_scrum` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `manual_scrum`;

DELIMITER $$
--
-- Eljárások
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `createNewAssignment` (IN `task_idIN` INT(10), IN `employee_idIN` INT(10))  NO SQL
INSERT INTO `assignment`(`assignment`.`id`, `assignment`.`task_id`, `assignment`.`employee_id`)
	values(NULL, task_idIN, employee_idIN)$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `createNewEmployee` (IN `nameIN` VARCHAR(100) CHARSET utf8)  NO SQL
INSERT INTO `employee`(`employee`.`id`, `employee`.`name`)
	VALUES(NULL, nameIN)$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `createNewProject` (IN `titleIN` VARCHAR(100) CHARSET utf8)  NO SQL
INSERT INTO `project`(`project`.`id`, `project`.`title`)
	VALUES(NULL, titleIN)$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `createNewSprint` (IN `project_idIN` INT(10))  NO SQL
INSERT INTO `sprint`(`sprint`.id, `sprint`.`is_finished`, `sprint`.`project_id`)
	VALUES (NULL, 0, project_idIN)$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `createNewTask` (IN `titleIN` VARCHAR(100), IN `descriptionIN` VARCHAR(100), IN `sprint_idIN` INT(10))  NO SQL
INSERT INTO `task`(`task`.`id`, `task`.`title`, `task`.`description`, `task`.`position`, `task`.`sprint_id`)

VALUES(NULL, titleIN, descriptionIN, 0, sprint_idIN)$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `deleteAllAssignmentByEmployeeId` (IN `employee_idIN` INT(10))  NO SQL
DELETE FROM `assignment`
WHERE `assignment`.`employee_id` = employee_idIN$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `deleteAllAssignmentByTaskId` (IN `task_idIN` INT(10))  NO SQL
DELETE FROM `assignment`
WHERE `assignment`.`task_id` = task_idIN$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `deleteAllSprintByProjectId` (IN `project_idIN` INT(10))  NO SQL
DELETE FROM `sprint`
WHERE `sprint`.`project_id` = project_idIN$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `deleteAllTaskBySprintId` (IN `sprint_idIN` INT(10))  NO SQL
DELETE FROM `task`
WHERE `task`.`sprint_id` = sprint_idIN$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `deleteAssignmentById` (IN `idIN` INT(10))  NO SQL
DELETE FROM `assignment`
WHERE `assignment`.`id` = idIN$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `deleteEmployeeById` (IN `idIN` INT(10))  NO SQL
DELETE FROM `employee`
WHERE `employee`.`id` = idIN$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `deleteProjectById` (IN `idIN` INT(10))  NO SQL
DELETE FROM `project`
WHERE `project`.`id` = idIN$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `deleteSprintById` (IN `idIN` INT(10))  NO SQL
DELETE FROM `sprint`
WHERE `sprint`.`id` = idIN$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `deleteTaskById` (IN `idIN` INT(10))  NO SQL
DELETE FROM `task`
WHERE `task`.`id` = idIN$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getAllAssignmentByEmployeeId` (IN `employee_idIN` INT(10))  NO SQL
SELECT * FROM `assignment`
WHERE `assignment`.`employee_id` = employee_idIN$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getAllAssignmentByTaskId` (IN `task_idIN` INT(10))  NO SQL
SELECT * FROM `assignment`
WHERE `assignment`.`task_id` = task_idIN$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getAllEmployees` ()  NO SQL
SELECT * FROM `employee`$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getAllProjects` ()  NO SQL
SELECT * FROM `project`$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getAllSprintByProjectId` (IN `project_idIN` INT(10))  NO SQL
SELECT * FROM `sprint`
WHERE `sprint`.`project_id` = project_idIN$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getAllTasksBySprintId` (IN `sprint_idIN` INT(10))  NO SQL
SELECT * FROM `task`
where `task`.`sprint_id` = sprint_idIN$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getAssignmentById` (IN `idIN` INT(10))  NO SQL
SELECT * FROM `assignment`
WHERE `assignment`.`id` = idIN$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getEmployeeById` (IN `idIN` INT(10))  NO SQL
SELECT * FROM `employee`
WHERE `employee`.`id` = idIN$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getProjectById` (IN `idIN` INT(10))  NO SQL
SELECT * FROM `project`
WHERE `project`.`id` = idIN$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getSprintById` (IN `idIN` INT(10))  NO SQL
SELECT * FROM `sprint`
WHERE `sprint`.`id` = idIN$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getTaskById` (IN `idIN` INT(10))  NO SQL
SELECT * FROM `task`
WHERE `task`.`id` = idIN$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `assignment`
--

CREATE TABLE `assignment` (
  `id` int(10) NOT NULL,
  `employee_id` int(10) NOT NULL,
  `task_id` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- A tábla adatainak kiíratása `assignment`
--

INSERT INTO `assignment` (`id`, `employee_id`, `task_id`) VALUES
(1, 1, 1),
(2, 2, 2),
(3, 3, 3),
(4, 1, 4),
(5, 2, 5),
(6, 3, 6),
(7, 1, 7),
(8, 2, 8),
(9, 3, 9),
(10, 1, 10),
(11, 2, 11),
(12, 3, 12),
(13, 1, 13),
(14, 2, 14);

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `employee`
--

CREATE TABLE `employee` (
  `id` int(10) NOT NULL,
  `name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- A tábla adatainak kiíratása `employee`
--

INSERT INTO `employee` (`id`, `name`) VALUES
(1, 'John Doe'),
(2, 'Robert Redford'),
(3, 'Test Employee');

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `project`
--

CREATE TABLE `project` (
  `id` int(10) NOT NULL,
  `title` varchar(100) CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- A tábla adatainak kiíratása `project`
--

INSERT INTO `project` (`id`, `title`) VALUES
(1, 'Project A'),
(2, 'Project B');

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `sprint`
--

CREATE TABLE `sprint` (
  `id` int(10) NOT NULL,
  `is_finished` tinyint(1) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `project_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- A tábla adatainak kiíratása `sprint`
--

INSERT INTO `sprint` (`id`, `is_finished`, `created_at`, `project_id`) VALUES
(1, 1, '2020-01-01 17:22:23', 1),
(2, 1, '2020-01-08 17:22:23', 1),
(3, 1, '2020-01-15 17:22:23', 1),
(4, 0, '2020-01-22 09:22:40', 1),
(5, 1, '2020-01-01 17:22:23', 2),
(6, 1, '2020-01-08 17:22:23', 2),
(7, 1, '2020-01-15 17:22:23', 2),
(8, 0, '2020-01-22 09:22:40', 2);

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `task`
--

CREATE TABLE `task` (
  `id` int(10) NOT NULL,
  `title` varchar(100) NOT NULL,
  `description` varchar(200) NOT NULL,
  `position` int(1) NOT NULL,
  `sprint_id` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- A tábla adatainak kiíratása `task`
--

INSERT INTO `task` (`id`, `title`, `description`, `position`, `sprint_id`) VALUES
(1, 'Task 1', 'Test Task 1', 3, 1),
(2, 'Task 2', 'Test Task 2', 3, 1),
(3, 'Task 3', 'Test Task 3', 3, 2),
(4, 'Task 4', 'Test Task 4', 3, 2),
(5, 'Task 5', 'Test Task 5', 3, 3),
(6, 'Task 6', 'Test Task 6', 3, 3),
(7, 'Task 7', 'Test Task 7', 1, 4),
(8, 'Task 8', 'Test Task 8', 3, 5),
(9, 'Task 9', 'Test Task 9', 3, 5),
(10, 'Task 10', 'Test Task 10', 3, 6),
(11, 'Task 11', 'Test Task 11', 3, 6),
(12, 'Task 12', 'Test Task 12', 3, 7),
(13, 'Task 13', 'Test Task 13', 1, 8),
(14, 'Task 14', 'Test Task 14', 2, 8);

--
-- Indexek a kiírt táblákhoz
--

--
-- A tábla indexei `assignment`
--
ALTER TABLE `assignment`
  ADD PRIMARY KEY (`id`),
  ADD KEY `task_id` (`task_id`),
  ADD KEY `employee_id` (`employee_id`);

--
-- A tábla indexei `employee`
--
ALTER TABLE `employee`
  ADD PRIMARY KEY (`id`);

--
-- A tábla indexei `project`
--
ALTER TABLE `project`
  ADD PRIMARY KEY (`id`);

--
-- A tábla indexei `sprint`
--
ALTER TABLE `sprint`
  ADD PRIMARY KEY (`id`),
  ADD KEY `project_id` (`project_id`);

--
-- A tábla indexei `task`
--
ALTER TABLE `task`
  ADD PRIMARY KEY (`id`),
  ADD KEY `sprint_id` (`sprint_id`);

--
-- A kiírt táblák AUTO_INCREMENT értéke
--

--
-- AUTO_INCREMENT a táblához `assignment`
--
ALTER TABLE `assignment`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT a táblához `employee`
--
ALTER TABLE `employee`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT a táblához `project`
--
ALTER TABLE `project`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT a táblához `sprint`
--
ALTER TABLE `sprint`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT a táblához `task`
--
ALTER TABLE `task`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- Megkötések a kiírt táblákhoz
--

--
-- Megkötések a táblához `assignment`
--
ALTER TABLE `assignment`
  ADD CONSTRAINT `assignment_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `assignment_ibfk_2` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE CASCADE;

--
-- Megkötések a táblához `sprint`
--
ALTER TABLE `sprint`
  ADD CONSTRAINT `sprint_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`) ON DELETE CASCADE;

--
-- Megkötések a táblához `task`
--
ALTER TABLE `task`
  ADD CONSTRAINT `task_ibfk_1` FOREIGN KEY (`sprint_id`) REFERENCES `sprint` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
