drop table IF EXISTS user_table;
drop table IF EXISTS appointment_table;
drop table IF EXISTS lesson_table;
drop table IF EXISTS confirmation_token;

create sequence hibernate_sequence start 1 increment 1;

 create table appointment_table (
 id int8 not null,
  final_price float8,
  date_from timestamp,
  status int4,
  date_to timestamp,
  lesson_id int8,
  student_id int8,
  primary key (id));

 create table confirmation_token (
  token_id int8 not null,
  confirmation_token varchar(255),
  created_date timestamp,
  user_id int8 not null,
     primary key (token_id));

 create table lesson_table (
  id int8 not null,
  created_at timestamp,
  discount float8,
  discount_start int4,
  field_of_study varchar(255),
  date_from timestamp,
  price_per_hour int4,
  date_to timestamp,
  lecturer_id int8,
      primary key (id));

 create table user_table (
 id int8 not null,
 birthday date,
 email varchar(255) not null,
 first_name varchar(255),
 is_enabled boolean not null,
 last_name varchar(255),
 password varchar(255) not null,
 role varchar(255),
  user_name varchar(255),
          primary key (id));

 alter table appointment_table
 add constraint app_les
 foreign key (lesson_id)
 references lesson_table;

 alter table appointment_table
 add constraint app_usr
 foreign key (student_id)
 references user_table;

 alter table confirmation_token
 add constraint ct_usr
 foreign key (user_id)
 references user_table;

 alter table lesson_table
 add constraint les_usr
 foreign key (lecturer_id)
 references user_table;