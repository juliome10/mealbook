# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table nota (
  id                        bigint not null,
  pensamiento               varchar(255),
  emocion                   varchar(255),
  fecha_nota                bigint,
  paciente_id               bigint,
  constraint pk_nota primary key (id))
;

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
  constraint uq_paciente_email unique (email),
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
  constraint uq_terapeuta_email unique (email),
  constraint pk_terapeuta primary key (id))
;

create sequence nota_seq;

create sequence paciente_seq;

create sequence terapeuta_seq;

alter table nota add constraint fk_nota_paciente_1 foreign key (paciente_id) references paciente (id);
create index ix_nota_paciente_1 on nota (paciente_id);
alter table paciente add constraint fk_paciente_terapeuta_2 foreign key (terapeuta_id) references terapeuta (id);
create index ix_paciente_terapeuta_2 on paciente (terapeuta_id);



# --- !Downs

drop table if exists nota cascade;

drop table if exists paciente cascade;

drop table if exists terapeuta cascade;

drop sequence if exists nota_seq;

drop sequence if exists paciente_seq;

drop sequence if exists terapeuta_seq;

