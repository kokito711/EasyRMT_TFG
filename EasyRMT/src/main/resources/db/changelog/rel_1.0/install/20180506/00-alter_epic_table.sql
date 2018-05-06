ALTER TABLE easyrmt.epic
CHANGE COLUMN author author INT(11) NULL DEFAULT '0' ,
ADD INDEX fk_Epic_Author_UserId_idx (author ASC),
ADD INDEX fk_Epic_Assigned_UserId_idx (assignedto ASC);
ALTER TABLE easyrmt.epic
ADD CONSTRAINT fk_Epic_Author_UserId
  FOREIGN KEY (author)
  REFERENCES easyrmt.app_user (user_id)
  ON DELETE SET NULL
  ON UPDATE SET NULL,
ADD CONSTRAINT fk_Epic_Assigned_UserId
  FOREIGN KEY (assignedto)
  REFERENCES easyrmt.app_user (user_id)
  ON DELETE SET NULL
  ON UPDATE SET NULL;

