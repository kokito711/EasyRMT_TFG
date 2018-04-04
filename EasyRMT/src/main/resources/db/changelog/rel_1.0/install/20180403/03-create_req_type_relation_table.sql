-- -- -----------------------------------------------------
-- -- Table `easyrmt`.`project_has_requirement_type`
-- -- -----------------------------------------------------
-- -- DROP TABLE IF EXISTS `easyrmt`.`project_has_requirement_type` ;
--
CREATE TABLE IF NOT EXISTS easyrmt.project_has_requirement_type (
  `project_idproject` INT NOT NULL,
  `object_types_idtype` INT NOT NULL,
  PRIMARY KEY (`project_idproject`, `object_types_idtype`),
  INDEX `fk_Project_has_Object_Types_Object_Types1_idx` (`object_types_idtype` ASC),
  INDEX `fk_Project_has_Object_Types_Project_idx` (`project_idproject` ASC),
  CONSTRAINT `fk_Project_has_Object_Types_Project`
    FOREIGN KEY (`project_idproject`)
    REFERENCES `easyrmt`.`project` (`idproject`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Project_has_Object_Types_Object_Types1`
    FOREIGN KEY (`object_types_idtype`)
    REFERENCES `easyrmt`.`requirement_type` (`idtype`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;