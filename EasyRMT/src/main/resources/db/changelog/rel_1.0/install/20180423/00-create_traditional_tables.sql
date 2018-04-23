-- -----------------------------------------------------
-- Table `easyrmt`.`feature`
-- -----------------------------------------------------
--DROP TABLE IF EXISTS `easyrmt`.`feature` ;

CREATE TABLE IF NOT EXISTS easyrmt.feature (
  `idobject` INT NOT NULL,
  `name` VARCHAR(64) NOT NULL DEFAULT 'Object name',
  `identifier` VARCHAR(10) NULL,
  `description` LONGTEXT NULL,
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
  CONSTRAINT `fk_Feature_Object1`
    FOREIGN KEY (`idobject`)
    REFERENCES `easyrmt`.`object` (`idobject`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `easyrmt`.`use_case`
-- -----------------------------------------------------
--DROP TABLE IF EXISTS `easyrmt`.`use_case` ;

CREATE TABLE IF NOT EXISTS `easyrmt`.`use_case` (
  `idobject` INT NOT NULL,
  `name` VARCHAR(64) NOT NULL DEFAULT 'Object name',
  `identifier` VARCHAR(10) NULL,
  `description` LONGTEXT NULL,
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
  `actors` MEDIUMTEXT NULL,
  `preconditions` MEDIUMTEXT NULL,
  `postconditions` MEDIUMTEXT NULL,
  `steps` LONGTEXT NULL,
  `feature` INT NOT NULL,
  PRIMARY KEY (`idobject`),
  UNIQUE INDEX `idObject_UNIQUE` (`idobject` ASC),
  INDEX `fk_Use_Case_Feature1_idx` (`feature` ASC),
  CONSTRAINT `fk_Use_Case_Feature1`
    FOREIGN KEY (`feature`)
    REFERENCES `easyrmt`.`feature` (`idobject`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Use_Case_Object1`
    FOREIGN KEY (`idobject`)
    REFERENCES `easyrmt`.`object` (`idobject`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;
