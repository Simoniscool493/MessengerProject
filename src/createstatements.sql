CREATE TABLE User
(
UserId int NOT NULL AUTO_INCREMENT,
UserName varchar(255) NOT NULL,
Password varchar(255) NOT NULL,
PRIMARY KEY (UserId)
)

INSERT INTO user(UserName,Password)
VALUES ("a","apassword");
INSERT INTO user(UserName,Password)
VALUES ("b","bpassword");

commit;
select * from user;
commit;