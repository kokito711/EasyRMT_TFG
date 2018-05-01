-- -----------------------------------------------------
-- Table `easyrmt`.`user`
-- -----------------------------------------------------
--DROP TABLE IF EXISTS `easyrmt`.`user` ;

CREATE TABLE IF NOT EXISTS easyrmt.user (
  `userId` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(16) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(150) NOT NULL,
  `name` VARCHAR(30) NULL,
  `lastname` VARCHAR(45) NULL,
  `phone` VARCHAR(15) NULL,
  PRIMARY KEY (`userId`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC))
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `easyrmt`.`role`
-- -----------------------------------------------------
--DROP TABLE IF EXISTS `easyrmt`.`role` ;

CREATE TABLE IF NOT EXISTS easyrmt.role (
  `idrole` INT NOT NULL AUTO_INCREMENT,
  `role` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idrole`),
  UNIQUE INDEX `role_UNIQUE` (`role` ASC))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `easyrmt`.`user_role`
-- -----------------------------------------------------
--DROP TABLE IF EXISTS `easyrmt`.`user_role` ;

CREATE TABLE IF NOT EXISTS easyrmt.user_role (
  `user_id` INT NOT NULL,
  `role_Id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `role_Id`),
  INDEX `fk_user_role_idx` (`role_Id` ASC),
  CONSTRAINT `fk_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `easyrmt`.`user` (`userId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_user_role`
    FOREIGN KEY (`role_Id`)
    REFERENCES `easyrmt`.`role` (`idrole`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Values for role table
-- -----------------------------------------------------
INSERT INTO role (role) VALUES ('ADMIN');
INSERT INTO role (role) VALUES ('PROJECT_MANAGER');
INSERT INTO role (role) VALUES ('ANALYST');
INSERT INTO role (role) VALUES ('STAKEHOLDER');