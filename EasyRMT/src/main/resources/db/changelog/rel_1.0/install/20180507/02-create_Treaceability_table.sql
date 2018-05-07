CREATE TABLE easyrmt.Traceability (
  Object1 INT NOT NULL,
  Object2 INT NOT NULL,
  PRIMARY KEY (Object1, Object2),
  INDEX fk_traceability_object2_idx (Object2 ASC),
  CONSTRAINT fk_traceability_object1
    FOREIGN KEY (Object1)
    REFERENCES easyrmt.object (idobject)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_traceability_object2
    FOREIGN KEY (Object2)
    REFERENCES easyrmt.object (idobject)
    ON DELETE CASCADE
    ON UPDATE CASCADE);