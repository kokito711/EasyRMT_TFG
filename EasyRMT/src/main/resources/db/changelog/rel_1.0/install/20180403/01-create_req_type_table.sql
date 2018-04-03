-- -----------------------------------------------------
-- Table `easyrmt`.`requirement_type`
-- -----------------------------------------------------
-- DROP TABLE IF EXISTS `easyrmt`.`requirement_type` ;

CREATE TABLE IF NOT EXISTS easyrmt.requirement_type (
  `idtype` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(64) NOT NULL DEFAULT '\"\"',
  PRIMARY KEY (`idtype`),
  UNIQUE INDEX `idType_UNIQUE` (`idtype` ASC),
  UNIQUE INDEX `Type_UNIQUE` (`type` ASC))
ENGINE = InnoDB;
