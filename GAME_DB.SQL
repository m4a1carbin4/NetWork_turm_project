# ...

DROP DATABASE GAME_DB;
CREATE DATABASE GAME_DB;
USE GAME_DB;

CREATE TABLE PLAYER (
# Base Settings
ID INT UNIQUE KEY NOT NULL,
USER_ID VARCHAR(100) PRIMARY KEY,
PASSWORD VARCHAR(500) NOT NULL,
USER_TYPE SMALLINT NOT NULL,

# Private data
NAME VARCHAR(200) NOT NULL,
NICKNAME VARCHAR(200) UNIQUE KEY NOT NULL,
EMAIL VARCHAR(200),
PRIVATE_SITE VARCHAR(200),

# Status
CONNECTION BOOLEAN NOT NULL, # connection status
CON_DATE DATETIME, # Last connection date
ROOM_JOINING INT NOT NULL,
WIN INT DEFAULT 0,
LOSS INT DEFAULT 0);

# A Table for save data of chatting
CREATE TABLE CHAT (
SENDER_ID VARCHAR(100) NOT NULL,
RECEIVER_ID VARCHAR(100) NOT NULL,
TYPE SMALLINT NOT NULL DEFAULT 0,
TIME DATETIME,
CONTEXT VARCHAR(500) NOT NULL);

# A Table for save data of reports
CREATE TABLE REPORT (
REPORTER VARCHAR(100) NOT NULL,
TARGET VARCHAR(100) NOT NULL,
TYPE SMALLINT NOT NULL DEFAULT 0,
CONTEXT VARCHAR(500),
FOREIGN KEY (TARGET) REFERENCES PLAYER(USER_ID) ON DELETE CASCADE);
# Forcely expire a report since user data is not exists.

# A Table for saving data of room
CREATE TABLE ROOM (
ID INT PRIMARY KEY,
TIME_CREATED DATE NOT NULL,
TIME_DELETED DATE NOT NULL);

# A Table for saving data of room history of user
CREATE TABLE ROOM_HISTORY (
ID INT UNIQUE KEY NOT NULL,
TIME_JOINED DATE NOT NULL,
TIME_EXITED DATE NOT NULL,
FOREIGN KEY (ID) REFERENCES PLAYER(ID));

# A View for accessing personal data
CREATE VIEW PLAYER_DATA AS
SELECT ID, USER_ID, NAME, NICKNAME, EMAIL, PRIVATE_SITE
FROM PLAYER;

# A View for accessing player status
CREATE VIEW PLAYER_STAT AS
SELECT NICKNAME, CONNECTION, CON_DATE, ROOM_JOINING, WIN, LOSS
FROM PLAYER;

# Insert a data of administrator
INSERT INTO PLAYER VALUES (
0, "administrator", hex(aes_encrypt("admin_password_teamb", "team2")), 0,
"Admin", "Administrator", "nw_teamb@gachon.ac.kr", "https://www.gachon.ac.kr/",
false, null, 0, 0, 0);

INSERT INTO PLAYER VALUES (
1, "kimkyeongmin", hex(aes_encrypt("kimkimkim", "team2")), 0,
"KyeongMin Kim", "Kim", "k001202@gachon.ac.kr", NULL,
false, null, 0, 0, 0);

INSERT INTO PLAYER VALUES (
2, "test1", hex(aes_encrypt("test", "team2")), 0,
"Temp", "Temp1", "k001202@gachon.ac.kr", NULL,
false, null, 0, 0, 0);

INSERT INTO PLAYER VALUES (
3, "test2", hex(aes_encrypt("test", "team2")), 0,
"Temp", "Temp2", "k001202@gachon.ac.kr", NULL,
false, null, 0, 0, 0);

COMMIT;