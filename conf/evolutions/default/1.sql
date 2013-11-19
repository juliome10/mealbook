# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table terapeuta (
  id                        bigint auto_increment not null,
  dni                       varchar(255),
  email                     varchar(255),
  constraint pk_terapeuta primary key (id))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table terapeuta;

SET FOREIGN_KEY_CHECKS=1;

