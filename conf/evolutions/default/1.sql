# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table terapeuta (
  id                        bigint not null,
  dni                       varchar(255),
  email                     varchar(255),
  constraint pk_terapeuta primary key (id))
;

create sequence terapeuta_seq;




# --- !Downs

drop table if exists terapeuta cascade;

drop sequence if exists terapeuta_seq;

