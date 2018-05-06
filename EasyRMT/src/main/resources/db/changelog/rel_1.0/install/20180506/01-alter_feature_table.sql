ALTER TABLE easyrmt.feature
CHANGE COLUMN author author INT(11) NULL DEFAULT '0' ,
ADD INDEX fk_Feature_Assigned_UserId_idx (assignedto ASC),
ADD INDEX fk_Feature_Author_UserId_idx (author ASC);
ALTER TABLE easyrmt.feature
ADD CONSTRAINT fk_Feature_Assigned_UserId
  FOREIGN KEY (assignedto)
  REFERENCES easyrmt.app_user (user_id)
  ON DELETE SET NULL
  ON UPDATE SET NULL,
ADD CONSTRAINT fk_Feature_Author_UserId
  FOREIGN KEY (author)
  REFERENCES easyrmt.app_user (user_id)
  ON DELETE SET NULL
  ON UPDATE SET NULL;
