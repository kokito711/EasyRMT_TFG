-- -----------------------------------------------------
-- Table `easyrmt`.`user`
-- -----------------------------------------------------
--DROP TABLE IF EXISTS `easyrmt`.`user` ;

CREATE TABLE IF NOT EXISTS easyrmt.user (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(16) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(150) NOT NULL,
  `name` VARCHAR(30) NULL,
  `lastname` VARCHAR(45) NULL,
  `phone` VARCHAR(15) NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC))
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `easyrmt`.`role`
-- -----------------------------------------------------
--DROP TABLE IF EXISTS `easyrmt`.`role` ;

CREATE TABLE IF NOT EXISTS easyrmt.role (
  `role_id` INT NOT NULL AUTO_INCREMENT,
  `role` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE INDEX `role_UNIQUE` (`role` ASC))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `easyrmt`.`user_role`
-- -----------------------------------------------------
--DROP TABLE IF EXISTS `easyrmt`.`user_role` ;

CREATE TABLE IF NOT EXISTS easyrmt.user_role (
  `user_id` INT NOT NULL,
  `role_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`),
  INDEX `fk_user_role_idx` (`role_id` ASC),
  CONSTRAINT `fk_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `easyrmt`.`user` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_user_role`
    FOREIGN KEY (`role_id`)
    REFERENCES `easyrmt`.`role` (`role_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Values for role table
-- -----------------------------------------------------
INSERT IGNORE INTO role (role) VALUES ('ADMIN');
INSERT IGNORE INTO role (role) VALUES ('PROJECT_MANAGER');
INSERT IGNORE INTO role (role) VALUES ('ANALYST');
INSERT IGNORE INTO role (role) VALUES ('STAKEHOLDER');