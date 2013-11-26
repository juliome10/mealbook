# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table paciente (
  id                        bigint not null,
  email                     varchar(255),
  password                  varchar(255),
  fecha_reg                 bigint,
  nombre                    varchar(255),
  fecha_nac                 bigint,
  sexo                      varchar(255),
  altura                    float,
  medicamentos              varchar(255),
  terapeuta_id              bigint,
  constraint pk_paciente primary key (id))
;

create table terapeuta (
  id                        bigint not null,
  dni                       varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  nombre                    varchar(255),
  apellidos                 varchar(255),
  fecha_reg                 bigint,
  constraint uq_terapeuta_dni unique (dni),
  constraint pk_terapeuta primary key (id))
;

create sequence paciente_seq;

create sequence terapeuta_seq;

alter table paciente add constraint fk_paciente_terapeuta_1 foreign key (terapeuta_id) references terapeuta (id);
create index ix_paciente_terapeuta_1 on paciente (terapeuta_id);



# --- !Downs

drop table if exists paciente cascade;

drop table if exists terapeuta cascade;

drop sequence if exists paciente_seq;

drop sequence if exists terapeuta_seq;

