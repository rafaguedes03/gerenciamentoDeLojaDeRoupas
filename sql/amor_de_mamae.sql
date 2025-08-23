CREATE DATABASE  IF NOT EXISTS `amor_de_mamae` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `amor_de_mamae`;
-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: amor_de_mamae
-- ------------------------------------------------------
-- Server version	5.5.5-10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `caixa`
--

DROP TABLE IF EXISTS `caixa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `caixa` (
  `id_caixa` int(11) NOT NULL AUTO_INCREMENT,
  `data_abertura` datetime NOT NULL,
  `data_fechamento` datetime DEFAULT NULL,
  `saldo_inicial` decimal(10,2) NOT NULL,
  `saldo_final` decimal(10,2) DEFAULT NULL,
  `valor_vendas` decimal(10,2) DEFAULT NULL,
  `aberto` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id_caixa`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `caixa`
--

LOCK TABLES `caixa` WRITE;
/*!40000 ALTER TABLE `caixa` DISABLE KEYS */;
INSERT INTO `caixa` VALUES (1,'2025-08-23 14:14:34','2025-08-23 14:15:19',0.00,120.00,120.00,0);
/*!40000 ALTER TABLE `caixa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cliente` (
  `id_cliente` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `email` char(50) DEFAULT NULL,
  `cpf` char(14) NOT NULL,
  `data_nascimento` date DEFAULT NULL,
  PRIMARY KEY (`id_cliente`),
  UNIQUE KEY `cpf` (`cpf`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente`
--

LOCK TABLES `cliente` WRITE;
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
INSERT INTO `cliente` VALUES (1,'Rafael','111111111','111.111.111-11','1111-11-11');
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estoque`
--

DROP TABLE IF EXISTS `estoque`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estoque` (
  `id_estoque` int(11) NOT NULL AUTO_INCREMENT,
  `id_produto` int(11) NOT NULL,
  `tamanho` varchar(10) NOT NULL,
  `quantidade` int(11) DEFAULT 0,
  PRIMARY KEY (`id_estoque`),
  UNIQUE KEY `id_produto` (`id_produto`,`tamanho`),
  CONSTRAINT `estoque_ibfk_1` FOREIGN KEY (`id_produto`) REFERENCES `produtos` (`id_produto`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estoque`
--

LOCK TABLES `estoque` WRITE;
/*!40000 ALTER TABLE `estoque` DISABLE KEYS */;
INSERT INTO `estoque` VALUES (1,1,'11',1101);
/*!40000 ALTER TABLE `estoque` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fiado`
--

DROP TABLE IF EXISTS `fiado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fiado` (
  `id_fiado` int(11) NOT NULL AUTO_INCREMENT,
  `id_venda` int(11) NOT NULL,
  `status` enum('ativo','pago','atrasado') DEFAULT 'ativo',
  PRIMARY KEY (`id_fiado`),
  KEY `id_venda` (`id_venda`),
  CONSTRAINT `fiado_ibfk_1` FOREIGN KEY (`id_venda`) REFERENCES `venda` (`id_venda`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fiado`
--

LOCK TABLES `fiado` WRITE;
/*!40000 ALTER TABLE `fiado` DISABLE KEYS */;
INSERT INTO `fiado` VALUES (1,1,'ativo');
/*!40000 ALTER TABLE `fiado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `itemvenda`
--

DROP TABLE IF EXISTS `itemvenda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `itemvenda` (
  `id_item_venda` int(11) NOT NULL AUTO_INCREMENT,
  `id_venda` int(11) NOT NULL,
  `id_estoque` int(11) NOT NULL,
  `quantidade` int(11) NOT NULL,
  `preco_unitario` decimal(10,2) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id_item_venda`),
  KEY `id_venda` (`id_venda`),
  KEY `id_estoque` (`id_estoque`),
  CONSTRAINT `itemvenda_ibfk_1` FOREIGN KEY (`id_venda`) REFERENCES `venda` (`id_venda`),
  CONSTRAINT `itemvenda_ibfk_2` FOREIGN KEY (`id_estoque`) REFERENCES `estoque` (`id_estoque`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `itemvenda`
--

LOCK TABLES `itemvenda` WRITE;
/*!40000 ALTER TABLE `itemvenda` DISABLE KEYS */;
INSERT INTO `itemvenda` VALUES (1,1,1,10,11.00,110.00);
/*!40000 ALTER TABLE `itemvenda` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parcelafiado`
--

DROP TABLE IF EXISTS `parcelafiado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parcelafiado` (
  `id_parcela` int(11) NOT NULL AUTO_INCREMENT,
  `id_fiado` int(11) NOT NULL,
  `numero_parcela` int(11) NOT NULL,
  `valor_parcela` decimal(10,2) NOT NULL,
  `data_vencimento` date NOT NULL,
  `status` enum('a vencer','vencida','paga') DEFAULT 'a vencer',
  PRIMARY KEY (`id_parcela`),
  KEY `id_fiado` (`id_fiado`),
  CONSTRAINT `parcelafiado_ibfk_1` FOREIGN KEY (`id_fiado`) REFERENCES `fiado` (`id_fiado`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parcelafiado`
--

LOCK TABLES `parcelafiado` WRITE;
/*!40000 ALTER TABLE `parcelafiado` DISABLE KEYS */;
INSERT INTO `parcelafiado` VALUES (1,1,1,9.17,'2025-09-23','paga'),(2,1,2,9.17,'2025-10-23','a vencer'),(3,1,3,9.17,'2025-11-23','a vencer'),(4,1,4,9.17,'2025-12-23','a vencer'),(5,1,5,9.17,'2026-01-23','a vencer'),(6,1,6,9.17,'2026-02-23','a vencer'),(7,1,7,9.17,'2026-03-23','a vencer'),(8,1,8,9.17,'2026-04-23','a vencer'),(9,1,9,9.17,'2026-05-23','a vencer'),(10,1,10,9.17,'2026-06-23','a vencer'),(11,1,11,9.17,'2026-07-23','a vencer'),(12,1,12,9.17,'2026-08-23','a vencer');
/*!40000 ALTER TABLE `parcelafiado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `produtos`
--

DROP TABLE IF EXISTS `produtos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `produtos` (
  `id_produto` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `codigo` char(20) NOT NULL,
  `preco` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id_produto`),
  UNIQUE KEY `codigo` (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produtos`
--

LOCK TABLES `produtos` WRITE;
/*!40000 ALTER TABLE `produtos` DISABLE KEYS */;
INSERT INTO `produtos` VALUES (1,'1','1',11.00);
/*!40000 ALTER TABLE `produtos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `login` varchar(50) NOT NULL,
  `senha` varchar(255) NOT NULL,
  `tipo` enum('Administrador','Funcionario') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `login` (`login`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Administrador','admin','$2a$12$nFzYepGm.m8nLYBKJvmJTuG0Du.O7DZmzq2kqqQ5SkJi1wwjx0Ezm','Administrador');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `venda`
--

DROP TABLE IF EXISTS `venda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `venda` (
  `id_venda` int(11) NOT NULL AUTO_INCREMENT,
  `id_cliente` int(11) DEFAULT NULL,
  `id_caixa` int(11) NOT NULL,
  `data_venda` datetime NOT NULL,
  `total_venda` decimal(10,2) NOT NULL,
  `forma_pagamento` enum('Dinheiro','Cartao','Pix','Fiado') NOT NULL,
  `status_parcelado` enum('sim','nao') DEFAULT 'nao',
  `num_parcelas` int(11) DEFAULT 1,
  PRIMARY KEY (`id_venda`),
  KEY `id_cliente` (`id_cliente`),
  KEY `id_caixa` (`id_caixa`),
  CONSTRAINT `venda_ibfk_1` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id_cliente`),
  CONSTRAINT `venda_ibfk_2` FOREIGN KEY (`id_caixa`) REFERENCES `caixa` (`id_caixa`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `venda`
--

LOCK TABLES `venda` WRITE;
/*!40000 ALTER TABLE `venda` DISABLE KEYS */;
INSERT INTO `venda` VALUES (1,1,1,'2025-08-23 14:15:10',110.00,'Fiado','sim',12);
/*!40000 ALTER TABLE `venda` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-23 14:40:53
