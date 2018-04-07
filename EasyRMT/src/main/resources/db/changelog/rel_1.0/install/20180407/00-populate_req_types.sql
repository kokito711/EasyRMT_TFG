--liquibase formatted sql
--changeset {sergio}:{20180407-create_schema_1.0}
INSERT INTO requirement_type (type) VALUES ('Functional  requirement');
INSERT INTO requirement_type (type) VALUES ('Non Functional requirement');
INSERT INTO requirement_type (type) VALUES ('Information requirement');
INSERT INTO requirement_type (type) VALUES ('Bussiness requirement');
INSERT INTO requirement_type (type) VALUES ('Bussiness rule');
INSERT INTO requirement_type (type) VALUES ('User requirement');
INSERT INTO requirement_type (type) VALUES ('Quality attribute');
INSERT INTO requirement_type (type) VALUES ('External interface requirement');
INSERT INTO requirement_type (type) VALUES ('Restriction');
INSERT INTO requirement_type (type) VALUES ('Solution proposal');
INSERT INTO requirement_type (type) VALUES ('Design requirement');