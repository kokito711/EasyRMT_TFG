ALTER TABLE easyrmt.use_case
CHANGE COLUMN author author INT(11) NULL DEFAULT '0' ,
ADD INDEX fk_UseCase_author_User_userId_idx (author ASC),
ADD INDEX fk_UseCase_assigned_User_userId_idx (assignedto ASC);
ALTER TABLE easyrmt.use_case
ADD CONSTRAINT fk_UseCase_author_User_userId
  FOREIGN KEY (author)
  REFERENCES easyrmt.app_user (user_id)
  ON DELETE SET NULL
  ON UPDATE SET NULL,
ADD CONSTRAINT fk_UseCase_assigned_User_userId
  FOREIGN KEY (assignedto)
  REFERENCES easyrmt.app_user (user_id)
  ON DELETE SET NULL
  ON UPDATE SET NULL;
