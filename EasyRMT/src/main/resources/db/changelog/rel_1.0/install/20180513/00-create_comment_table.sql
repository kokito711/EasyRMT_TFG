CREATE TABLE IF NOT EXISTS easyrmt.comment (
  idComment INT NOT NULL AUTO_INCREMENT,
  comment LONGTEXT NOT NULL,
  created DATETIME NOT NULL DEFAULT '1000-01-01 00:00:00',
  last_modified DATETIME NOT NULL DEFAULT '1000-01-01 00:00:00',
  objectId INT NOT NULL,
  userId INT NOT NULL,
  PRIMARY KEY (idComment),
  INDEX fk_comment_objectId_idx (objectId ASC),
  INDEX fk_comment_userId_idx (userId ASC),
  CONSTRAINT fk_comment_objectId
    FOREIGN KEY (objectId)
    REFERENCES easyrmt.object (idobject)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_comment_userId
    FOREIGN KEY (userId)
    REFERENCES easyrmt.app_user (user_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;