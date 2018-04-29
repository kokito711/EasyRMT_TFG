-- -----------------------------------------------------
-- Table `easyrmt`.`documentation`
-- -----------------------------------------------------
--DROP TABLE IF EXISTS `easyrmt`.`documentation` ;

CREATE TABLE easyrmt.documentation (
  `iddocumentation` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(64) NOT NULL DEFAULT 'file',
  `size` DOUBLE NOT NULL DEFAULT 0.0,
  `type` VARCHAR(255) NOT NULL DEFAULT 'application/octet-stream',
  `idobject` INT NULL,
  `idproject` INT NOT NULL,
  `path` VARCHAR(256) NOT NULL DEFAULT '/',
  PRIMARY KEY (`iddocumentation`),
  INDEX `fk_documentation_1_idx` (`idobject` ASC),
  INDEX `fk_documentation_2_idx` (`idproject` ASC),
  CONSTRAINT `fk_documentation_1`
    FOREIGN KEY (`idobject`)
    REFERENCES `easyrmt`.`object` (`idobject`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_documentation_2`
    FOREIGN KEY (`idproject`)
    REFERENCES `easyrmt`.`project` (`idproject`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;