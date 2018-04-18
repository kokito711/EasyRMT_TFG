-- -----------------------------------------------------
-- Table `easyrmt`.`requirement_type`
-- -----------------------------------------------------
-- DROP TABLE IF EXISTS `easyrmt`.`requirement_type` ;

CREATE TABLE IF NOT EXISTS easyrmt.object (
  `idobject` INT NOT NULL AUTO_INCREMENT,
  `project_idproject` INT NOT NULL,
  PRIMARY KEY (`idobject`),
  UNIQUE INDEX `idObject_UNIQUE` (`idobject` ASC),
  INDEX `fk_Object_Project1_idx` (`project_idproject` ASC),
  CONSTRAINT `fk_Object_Project1`
    FOREIGN KEY (`project_idproject`)
    REFERENCES easyrmt.project (`idproject`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS easyrmt.epic (
  `idobject` INT NOT NULL,
  `name` VARCHAR(64) NOT NULL DEFAULT 'Object name',
  `identifier` VARCHAR(10) NULL,
  `description` LONGTEXT NULL,
  `definitionofdone` LONGTEXT NULL,
  `priority` ENUM('VERY_LOW', 'LOW', 'NORMAL', 'HIGH', 'VERY_HIGH', 'BLOCKER') NOT NULL DEFAULT 'NORMAL',
  `complexity` ENUM('VERY_LOW', 'LOW', 'NORMAL', 'HIGH', 'VERY_HIGH', 'BLOCKER') NOT NULL DEFAULT 'NORMAL',
  `state` ENUM('DRAFT', 'APPROVED', 'WORKING', 'TESTING', 'IMPLEMENTED', 'REJECTED') NOT NULL DEFAULT 'DRAFT',
  `cost` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  `estimatedhours` DECIMAL(4,2) NULL,
  `storypoints` INT NULL,
  `source` VARCHAR(64) NULL,
  `scope` ENUM('PROJECT', 'FEATURE', 'REQUIREMENT') NULL,
  `risk` ENUM('HIGH', 'MEDIUM', 'LOW', 'NONE') NULL,
  `created` DATETIME NOT NULL DEFAULT '1000-01-01 00:00:00',
  `lastupdated` DATETIME NOT NULL DEFAULT '1000-01-01 00:00:00',
  `version` VARCHAR(45) NULL,
  `validationmethod` LONGTEXT NULL,
  `author` INT NOT NULL DEFAULT 0,
  `assignedto` INT NULL,
  `justification` MEDIUMTEXT NULL,
  `testcases` LONGTEXT NULL,
  PRIMARY KEY (`idobject`),
  UNIQUE INDEX `idObject_UNIQUE` (`idobject` ASC),
  CONSTRAINT `fk_Epic_Object1`
    FOREIGN KEY (`idobject`)
    REFERENCES easyrmt.object (`idobject`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `easyrmt`.`user_story` (
  `idobject` INT NOT NULL,
  `name` VARCHAR(64) NOT NULL DEFAULT 'Object name',
  `identifier` VARCHAR(10) NULL,
  `description` LONGTEXT NULL,
  `definitionofdone` LONGTEXT NULL,
  `priority` ENUM('VERY_LOW', 'LOW', 'NORMAL', 'HIGH', 'VERY_HIGH', 'BLOCKER') NOT NULL DEFAULT 'NORMAL',
  `complexity` ENUM('VERY_LOW', 'LOW', 'NORMAL', 'HIGH', 'VERY_HIGH', 'BLOCKER') NOT NULL DEFAULT 'NORMAL',
  `state` ENUM('DRAFT', 'APPROVED', 'WORKING', 'TESTING', 'IMPLEMENTED', 'REJECTED') NOT NULL DEFAULT 'DRAFT',
  `cost` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  `estimatedhours` DECIMAL(4,2) NULL,
  `storypoints` INT NULL,
  `source` VARCHAR(64) NULL,
  `scope` ENUM('PROJECT', 'FEATURE', 'REQUIREMENT') NULL,
  `risk` ENUM('HIGH', 'MEDIUM', 'LOW', 'NONE') NULL,
  `created` DATETIME NOT NULL DEFAULT '1000-01-01 00:00:00',
  `lastupdated` DATETIME NOT NULL DEFAULT '1000-01-01 00:00:00',
  `version` VARCHAR(45) NULL,
  `validationmethod` LONGTEXT NULL,
  `author` INT NOT NULL DEFAULT 0,
  `assignedto` INT NULL,
  `justification` MEDIUMTEXT NULL,
  `testcases` LONGTEXT NULL,
  `epic` INT NOT NULL,
  PRIMARY KEY (`idobject`),
  UNIQUE INDEX `idObject_UNIQUE` (`idobject` ASC),
  INDEX `fk_User_Story_Epic1_idx` (`epic` ASC),
  CONSTRAINT `fk_User_Story_Epic1`
    FOREIGN KEY (`epic`)
    REFERENCES `easyrmt`.`epic` (`idobject`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_User_Story_Object1`
    FOREIGN KEY (`idobject`)
    REFERENCES `easyrmt`.`object` (`idobject`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;
