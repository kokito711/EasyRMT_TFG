-- -----------------------------------------------------
-- Table `easyrmt`.`user`
-- -----------------------------------------------------
--DROP TABLE IF EXISTS `easyrmt`.`user` ;

ALTER table easyrmt.user ADD enabled INT NOT NULL DEFAULT 1;

INSERT INTO easyrmt.user (`username`,`email`,`password`,`name`,`lastname`,`phone`,`enabled`)
VALUES ('Admin','admin@admin.ad','$2a$10$FShcGbidU4RcOlgPIPGNwuoRk.LgRBl/.yUkp5qIeCzI0123jpUvy','','',NULL,1);
INSERT INTO easyrmt.user_role(`user_id`,`role_Id`) SELECT userId, idrole from easyrmt.user join role where username = 'Admin' and role.role = 'ADMIN';