CREATE TABLE accounts(
  id number NOT NULL,
  owner varchar(255),
  balance long NOT NULL DEFAULT 0, -- cents
  number varchar(26) UNIQUE,
  PRIMARY KEY(id),
  CHECK(balance>0)
)