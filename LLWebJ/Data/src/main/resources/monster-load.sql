--
-- File generated with SQLiteStudio v3.2.1 on Thu Dec 26 21:44:36 2019
--
-- Text encoding used: UTF-8
--
PRAGMA foreign_keys = off;
BEGIN TRANSACTION;

-- Table: Monster
CREATE TABLE Monster (
    MonsterId   BIGINT        PRIMARY KEY
                             UNIQUE
                             NOT NULL,
    MonsterName VARCHAR (200) NOT NULL,
    MonsterBlob BLOB
);

INSERT INTO Monster (
                       MonsterId,
                       MonsterName,
                       MonsterBlob
                   )
                   VALUES (
                       1,
                       'Gryphon',
                       NULL
                   );

INSERT INTO Monster (
                       MonsterId,
                       MonsterName,
                       MonsterBlob
                   )
                   VALUES (
                       2,
                       'Phoenix',
                       NULL
                   );

INSERT INTO Monster (
                       MonsterId,
                       MonsterName,
                       MonsterBlob
                   )
                   VALUES (
                       3,
                       'Gelatinous Cube',
                       NULL
                   );

INSERT INTO Monster (
                       MonsterId,
                       MonsterName,
                       MonsterBlob
                   )
                   VALUES (
                       4,
                       'Orc',
                       NULL
                   );


-- Table: MonsterTag
CREATE TABLE MonsterTag (
    MonsterTagId BIGINT PRIMARY KEY
                       UNIQUE
                       NOT NULL,
    MonsterId    BIGINT REFERENCES Monster (MonsterId)
                       NOT NULL,
    TagId       BIGINT REFERENCES Tag (TagId) 
                       NOT NULL
);

INSERT INTO MonsterTag (
                          MonsterTagId,
                          MonsterId,
                          TagId
                      )
                      VALUES (
                          1,
                          1,
                          1
                      );

INSERT INTO MonsterTag (
                          MonsterTagId,
                          MonsterId,
                          TagId
                      )
                      VALUES (
                          2,
                          1,
                          2
                      );

INSERT INTO MonsterTag (
                          MonsterTagId,
                          MonsterId,
                          TagId
                      )
                      VALUES (
                          3,
                          1,
                          3
                      );

INSERT INTO MonsterTag (
                          MonsterTagId,
                          MonsterId,
                          TagId
                      )
                      VALUES (
                          4,
                          2,
                          1
                      );

INSERT INTO MonsterTag (
                          MonsterTagId,
                          MonsterId,
                          TagId
                      )
                      VALUES (
                          5,
                          2,
                          4
                      );

INSERT INTO MonsterTag (
                          MonsterTagId,
                          MonsterId,
                          TagId
                      )
                      VALUES (
                          6,
                          2,
                          5
                      );

INSERT INTO MonsterTag (
                          MonsterTagId,
                          MonsterId,
                          TagId
                      )
                      VALUES (
                          7,
                          3,
                          2
                      );

INSERT INTO MonsterTag (
                          MonsterTagId,
                          MonsterId,
                          TagId
                      )
                      VALUES (
                          8,
                          3,
                          6
                      );

INSERT INTO MonsterTag (
                          MonsterTagId,
                          MonsterId,
                          TagId
                      )
                      VALUES (
                          9,
                          3,
                          7
                      );

INSERT INTO MonsterTag (
                          MonsterTagId,
                          MonsterId,
                          TagId
                      )
                      VALUES (
                          10,
                          4,
                          2
                      );

INSERT INTO MonsterTag (
                          MonsterTagId,
                          MonsterId,
                          TagId
                      )
                      VALUES (
                          11,
                          4,
                          8
                      );


-- Table: Tag
CREATE TABLE Tag (
    TagId   BIGINT        PRIMARY KEY
                          UNIQUE
                          NOT NULL,
    TagName VARCHAR (200) NOT NULL
);

INSERT INTO Tag (
                    TagId,
                    TagName
                )
                VALUES (
                    1,
                    'Flying'
                );

INSERT INTO Tag (
                    TagId,
                    TagName
                )
                VALUES (
                    2,
                    'Walking'
                );

INSERT INTO Tag (
                    TagId,
                    TagName
                )
                VALUES (
                    3,
                    'Yellow'
                );

INSERT INTO Tag (
                    TagId,
                    TagName
                )
                VALUES (
                    4,
                    'Rainbow'
                );

INSERT INTO Tag (
                    TagId,
                    TagName
                )
                VALUES (
                    5,
                    'Fast'
                );

INSERT INTO Tag (
                    TagId,
                    TagName
                )
                VALUES (
                    6,
                    'Pink'
                );

INSERT INTO Tag (
                    TagId,
                    TagName
                )
                VALUES (
                    7,
                    'Slow'
                );

INSERT INTO Tag (
                    TagId,
                    TagName
                )
                VALUES (
                    8,
                    'Green'
                );


COMMIT TRANSACTION;
PRAGMA foreign_keys = on;
