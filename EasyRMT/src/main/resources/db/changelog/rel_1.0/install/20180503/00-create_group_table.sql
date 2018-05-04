-- -----------------------------------------------------
-- Table easyrmt.group
-- -----------------------------------------------------
--DROP TABLE IF EXISTS easyrmt.group ;
CREATE TABLE IF NOT EXISTS easyrmt.app_group (
  `group_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`group_id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table easyrmt.group_user
-- -----------------------------------------------------
--DROP TABLE IF EXISTS easyrmt.group_user ;
CREATE TABLE IF NOT EXISTS easyrmt.group_user (
  `group_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `isPM` TINYINT NOT NULL DEFAULT 0,
  `isStakeholder` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`group_id`, `user_id`),
  INDEX `fk_user_idx` (`user_id` ASC),
  CONSTRAINT `fk_group`
    FOREIGN KEY (`group_id`)
    REFERENCES `easyrmt`.`app_group` (`group_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_user_to_group`
    FOREIGN KEY (`user_id`)
    REFERENCES `easyrmt`.`app_user` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;