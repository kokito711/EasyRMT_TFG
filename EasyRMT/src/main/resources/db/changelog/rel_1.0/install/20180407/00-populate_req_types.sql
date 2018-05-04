--liquibase formatted sql
--changeset {sergio}:{20180407-create_schema_1.0}
INSERT IGNORE INTO requirement_type (type) VALUES ('Functional  requirement');
INSERT IGNORE INTO requirement_type (type) VALUES ('Non Functional requirement');
INSERT IGNORE INTO requirement_type (type) VALUES ('Information requirement');
INSERT IGNORE INTO requirement_type (type) VALUES ('Bussiness requirement');
INSERT IGNORE INTO requirement_type (type) VALUES ('Bussiness rule');
INSERT IGNORE INTO requirement_type (type) VALUES ('User requirement');
INSERT IGNORE INTO requirement_type (type) VALUES ('Quality attribute');
INSERT IGNORE INTO requirement_type (type) VALUES ('External interface requirement');
INSERT IGNORE INTO requirement_type (type) VALUES ('Restriction');
INSERT IGNORE INTO requirement_type (type) VALUES ('Solution proposal');
INSERT IGNORE INTO requirement_type (type) VALUES ('Design requirement');