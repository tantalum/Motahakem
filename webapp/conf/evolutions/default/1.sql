# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table ping (
  id                        bigint auto_increment not null,
  server_id                 bigint,
  time                      datetime,
  reachable                 tinyint(1) default 0,
  constraint pk_ping primary key (id))
;

create table server (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  address                   varchar(255),
  username                  varchar(255),
  password                  varchar(255),
  to_auto_update            varchar(255),
  constraint pk_server primary key (id))
;

create table user (
  username                  varchar(255) not null,
  password                  varchar(255),
  constraint pk_user primary key (username))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table ping;

drop table server;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

