-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         10.4.32-MariaDB - mariadb.org binary distribution
-- SO del servidor:              Win64
-- HeidiSQL Versión:             12.11.0.7065
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Volcando estructura de base de datos para optiplan_bd
CREATE DATABASE IF NOT EXISTS `optiplan_bd` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `optiplan_bd`;

-- Volcando estructura para tabla optiplan_bd.categorias
CREATE TABLE IF NOT EXISTS `categorias` (
  `CategoriaID` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`CategoriaID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla optiplan_bd.categorias: ~4 rows (aproximadamente)
INSERT INTO `categorias` (`CategoriaID`, `Nombre`) VALUES
	(1, 'Trabajo'),
	(2, 'Estudio'),
	(3, 'Personal'),
	(4, 'Otro');

-- Volcando estructura para tabla optiplan_bd.recordatorios
CREATE TABLE IF NOT EXISTS `recordatorios` (
  `RecordatorioID` int(11) NOT NULL AUTO_INCREMENT,
  `UsuarioID` int(11) DEFAULT NULL,
  `TareaID` int(11) DEFAULT NULL,
  `FechaEnvio` datetime DEFAULT NULL,
  `Medio` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`RecordatorioID`),
  KEY `UsuarioID` (`UsuarioID`),
  KEY `TareaID` (`TareaID`),
  CONSTRAINT `recordatorios_ibfk_1` FOREIGN KEY (`UsuarioID`) REFERENCES `usuarios` (`UsuarioID`),
  CONSTRAINT `recordatorios_ibfk_2` FOREIGN KEY (`TareaID`) REFERENCES `tareas` (`TareaID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla optiplan_bd.recordatorios: ~0 rows (aproximadamente)

-- Volcando estructura para tabla optiplan_bd.tareas
CREATE TABLE IF NOT EXISTS `tareas` (
  `TareaID` int(11) NOT NULL AUTO_INCREMENT,
  `UsuarioID` int(11) DEFAULT NULL,
  `Titulo` varchar(100) DEFAULT NULL,
  `Descripcion` text DEFAULT NULL,
  `CategoriaID` int(11) DEFAULT NULL,
  `Prioridad` varchar(20) DEFAULT NULL,
  `Estado` varchar(20) DEFAULT NULL,
  `Fecha` date DEFAULT NULL,
  `HoraInicio` time DEFAULT NULL,
  `HoraFin` time DEFAULT NULL,
  `FechaCreacion` datetime DEFAULT current_timestamp(),
  `RecordatorioActivo` tinyint(1) DEFAULT 0,
  `HoraRecordatorio` time DEFAULT NULL,
  PRIMARY KEY (`TareaID`),
  KEY `UsuarioID` (`UsuarioID`),
  KEY `CategoriaID` (`CategoriaID`),
  CONSTRAINT `tareas_ibfk_1` FOREIGN KEY (`UsuarioID`) REFERENCES `usuarios` (`UsuarioID`),
  CONSTRAINT `tareas_ibfk_2` FOREIGN KEY (`CategoriaID`) REFERENCES `categorias` (`CategoriaID`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla optiplan_bd.tareas: ~6 rows (aproximadamente)
INSERT INTO `tareas` (`TareaID`, `UsuarioID`, `Titulo`, `Descripcion`, `CategoriaID`, `Prioridad`, `Estado`, `Fecha`, `HoraInicio`, `HoraFin`, `FechaCreacion`, `RecordatorioActivo`, `HoraRecordatorio`) VALUES
	(6, 1, 'test3', 'esto es un test', 3, 'Alta', 'Completado', '2025-09-24', NULL, '19:25:00', '2025-09-23 18:25:55', 0, NULL),
	(7, 1, 'Tarea1', 'Tarea1', 2, 'Media', 'Completado', '2025-09-24', NULL, '15:04:00', '2025-09-24 14:02:37', 0, NULL),
	(14, 1, 'tagrea', 'tagrea', 1, 'Media', 'Completado', '2025-09-24', NULL, '16:01:00', '2025-09-24 15:33:48', 0, NULL),
	(15, 1, 'tareapp', 'pp', 2, 'Alta', 'Completado', '2025-09-24', NULL, '16:00:00', '2025-09-24 15:36:31', 0, NULL),
	(16, 1, '123', '123', 2, 'Alta', 'Pendiente', '2025-10-01', NULL, '11:46:17', '2025-09-30 11:46:34', 0, NULL),
	(17, 1, 'Wa', 'test tarea prueba', 3, 'Alta', 'Pendiente', '2025-10-02', NULL, '14:45:39', '2025-09-30 14:46:02', 0, NULL);

-- Volcando estructura para tabla optiplan_bd.usuarios
CREATE TABLE IF NOT EXISTS `usuarios` (
  `UsuarioID` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(100) DEFAULT NULL,
  `Correo` varchar(100) DEFAULT NULL,
  `Contrasena` varchar(256) DEFAULT NULL,
  `FechaRegistro` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`UsuarioID`),
  UNIQUE KEY `Correo` (`Correo`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Volcando datos para la tabla optiplan_bd.usuarios: ~3 rows (aproximadamente)
INSERT INTO `usuarios` (`UsuarioID`, `Nombre`, `Correo`, `Contrasena`, `FechaRegistro`) VALUES
	(1, 'Fabricio', 'correo@gmail.com', '123', '2025-09-22 23:44:25'),
	(2, 'Test', 'correo@gmail', '123', '2025-09-23 00:01:19'),
	(3, 'Test1', 'correo123', '321', '2025-09-23 00:02:54');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
