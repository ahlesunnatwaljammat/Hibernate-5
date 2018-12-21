DROP TABLE IF EXISTS  EVENT;

CREATE TABLE EVENT (
  eventId int auto_increment,
  eventName varchar(255) not null,
  date timestamp not null,
  primary key (eventId)
);