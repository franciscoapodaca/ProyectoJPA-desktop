-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         5.6.21-log - MySQL Community Server (GPL)
-- SO del servidor:              Win64
-- HeidiSQL Versión:             8.3.0.4694
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Volcando estructura de base de datos para amantemusica
CREATE DATABASE IF NOT EXISTS `amantemusica` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `amantemusica`;


-- Volcando estructura para tabla amantemusica.actores
CREATE TABLE IF NOT EXISTS `actores` (
  `cveActor` varchar(10) NOT NULL,
  `nombre` varchar(35) NOT NULL,
  PRIMARY KEY (`cveActor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- La exportación de datos fue deseleccionada.


-- Volcando estructura para tabla amantemusica.actores_peliculas
CREATE TABLE IF NOT EXISTS `actores_peliculas` (
  `clave` varchar(10) NOT NULL,
  `cveActor` varchar(10) NOT NULL,
  PRIMARY KEY (`clave`,`cveActor`),
  KEY `cveActor` (`cveActor`),
  CONSTRAINT `clave2` FOREIGN KEY (`clave`) REFERENCES `peliculas` (`clave`),
  CONSTRAINT `cveActor` FOREIGN KEY (`cveActor`) REFERENCES `actores` (`cveActor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- La exportación de datos fue deseleccionada.


-- Volcando estructura para tabla amantemusica.albums
CREATE TABLE IF NOT EXISTS `albums` (
  `cveAlbum` varchar(10) NOT NULL,
  `titulo` varchar(35) NOT NULL,
  `disquera` varchar(35) DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  PRIMARY KEY (`cveAlbum`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- La exportación de datos fue deseleccionada.


-- Volcando estructura para tabla amantemusica.canciones
CREATE TABLE IF NOT EXISTS `canciones` (
  `clave` varchar(10) NOT NULL,
  `interprete` varchar(35) DEFAULT NULL,
  `autor` varchar(35) DEFAULT NULL,
  PRIMARY KEY (`clave`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- La exportación de datos fue deseleccionada.


-- Volcando estructura para tabla amantemusica.canciones_albums
CREATE TABLE IF NOT EXISTS `canciones_albums` (
  `clave` varchar(10) NOT NULL,
  `cveAlbum` varchar(10) NOT NULL,
  PRIMARY KEY (`clave`,`cveAlbum`),
  KEY `cveAlbum` (`cveAlbum`),
  CONSTRAINT `clave` FOREIGN KEY (`clave`) REFERENCES `canciones` (`clave`),
  CONSTRAINT `cveAlbum` FOREIGN KEY (`cveAlbum`) REFERENCES `albums` (`cveAlbum`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- La exportación de datos fue deseleccionada.


-- Volcando estructura para tabla amantemusica.generos
CREATE TABLE IF NOT EXISTS `generos` (
  `cveGenero` varchar(10) NOT NULL,
  `nombre` varchar(20) NOT NULL,
  `tipoMedio` varchar(1) NOT NULL,
  PRIMARY KEY (`cveGenero`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- La exportación de datos fue deseleccionada.


-- Volcando estructura para tabla amantemusica.medios
CREATE TABLE IF NOT EXISTS `medios` (
  `clave` varchar(10) NOT NULL,
  `titutlo` varchar(35) NOT NULL,
  `tipoMedio` varchar(1) NOT NULL,
  `duracion` int(11) NOT NULL,
  `cveGenero` varchar(10) NOT NULL,
  PRIMARY KEY (`clave`),
  KEY `cveGenero` (`cveGenero`),
  CONSTRAINT `cveGenero` FOREIGN KEY (`cveGenero`) REFERENCES `generos` (`cveGenero`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- La exportación de datos fue deseleccionada.


-- Volcando estructura para tabla amantemusica.peliculas
CREATE TABLE IF NOT EXISTS `peliculas` (
  `clave` varchar(10) NOT NULL,
  `director` varchar(35) NOT NULL,
  `fecha` date DEFAULT NULL,
  PRIMARY KEY (`clave`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- La exportación de datos fue deseleccionada.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
