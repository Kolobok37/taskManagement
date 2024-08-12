create sequence IF NOT EXISTS hibernate_sequence start with 1 increment by 1;
create sequence IF NOT EXISTS user_id_seq start with 1 increment by 1;
create TABLE IF NOT EXISTS users (
id integer GENERATED BY DEFAULT AS IDENTITY NOT NULL,
user_id_seq integer,
username VARCHAR(63) NOT NULL UNIQUE,
email VARCHAR(63) NOT NULL UNIQUE,
password VARCHAR(63) NOT NULL,
role VARCHAR(63) NOT NULL,
CONSTRAINT pk_users PRIMARY KEY (id)
);
create TABLE IF NOT EXISTS comments(
id integer GENERATED BY DEFAULT AS IDENTITY NOT NULL,
id_task integer NOT NULL,
text VARCHAR(255) NOT NULL,
creator_id integer NOT NULL,
creation_date timestamptz NOT NULL,
CONSTRAINT pk_comments PRIMARY KEY (id)
);
create TABLE IF NOT EXISTS tasks (
id integer GENERATED BY DEFAULT AS IDENTITY NOT NULL,
title VARCHAR(127) NOT NULL,
description VARCHAR(511) NOT NULL,
status VARCHAR(63),
priority VARCHAR(63),
creation_date timestamptz NOT NULL,
completion_date timestamptz,
creator_id integer NOT NULL,
performer_id integer,
CONSTRAINT pk_tasks PRIMARY KEY (id)
);

