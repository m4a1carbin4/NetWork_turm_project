# Test
# Use SOURCE command to run this script

DROP DATABASE GAME_DB;
CREATE DATABASE GAME_DB;
USE GAME_DB;

CREATE TABLE PLAYER (
# Base Settings
USER_ID VARCHAR(100) PRIMARY KEY,
PASSWORD VARCHAR(500) NOT NULL,
USER_TYPE SMALLINT,

# Private data
NAME VARCHAR(200),
NICKNAME VARCHAR(200) UNIQUE KEY,
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

# A View for accessing personal data
CREATE VIEW PLAYER_DATA AS
SELECT USER_ID, NAME, NICKNAME, EMAIL, PRIVATE_SITE
FROM PLAYER;

# A View for accessing player status
CREATE VIEW PLAYER_STAT AS
SELECT USER_ID, CONNECTION, CON_DATE, WIN, LOSS
FROM PLAYER;

# Insert a data of administrator
INSERT INTO PLAYER VALUES (
"administrator", hex(aes_encrypt("admin_password_teamb", "team2")), 0,
"Admin", "Administrator", "nw_teamb@gachon.ac.kr", "https://www.gachon.ac.kr/",
false, null, 0, 0, 0);

# Note: recent status of game_db on 211108:
# +---------------+------------------------------------------------------------------+-----------+-------+---------------+-----------------------+---------------------------+------------+----------+--------------+------+------+
# | USER_ID       | PASSWORD                                                         | USER_TYPE | NAME  | NICKNAME      | EMAIL                 | PRIVATE_SITE              | CONNECTION | CON_DATE | ROOM_JOINING | WIN  | LOSS |
# +---------------+------------------------------------------------------------------+-----------+-------+---------------+-----------------------+---------------------------+------------+----------+--------------+------+------+
# | administrator | 641E1C537965796F5D26A047486D865C231E76455D938F7B23A78F45E1947080 |         0 | Admin | Administrator | nw_teamb@gachon.ac.kr | https://www.gachon.ac.kr/ |          0 | NULL     |            0 |    0 |    0 |
# +---------------+------------------------------------------------------------------+-----------+-------+---------------+-----------------------+---------------------------+------------+----------+--------------+------+------+
# Other tables are all empty.

COMMIT;