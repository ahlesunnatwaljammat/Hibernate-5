DROP TABLE IF EXISTS USERS;

CREATE TABLE USERS (
  userId int auto_increment,
  username varchar(255) unique not null,
  password varchar(255) not null,
  date timestamp default current_timestamp,
  primary key (userId)
);

DROP TABLE IF EXISTS  EVENT;

CREATE TABLE EVENT (
  eventId int auto_increment,
  eventName varchar(255) not null,
  date timestamp not null,
  primary key (eventId)
);