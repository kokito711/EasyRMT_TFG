delete from requirement_type;

ALTER TABLE easyrmt.requirement_type CHANGE COLUMN requirement_type.type requirement_name
VARCHAR(64) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL DEFAULT '""' ;
ALTER TABLE easyrmt.requirement_type ADD COLUMN requirement_type ENUM('SCOPE', 'ENGINEERING', 'QA', 'USER_EXP') NOT NULL DEFAULT 'ENGINEERING' AFTER requirement_name;
