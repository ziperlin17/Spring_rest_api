
CREATE TABLE users (
    id VARCHAR(50) PRIMARY KEY NOT NULL,
    username VARCHAR(32) NOT NULL UNIQUE,
    firstname VARCHAR(32),
    lastname VARCHAR(32),
    userpic VARCHAR ,
    email VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL,
    created_date DATE DEFAULT CURRENT_DATE NOT NULL ,
    last_updated_date DATE DEFAULT CURRENT_DATE NOT NULL
);

CREATE TABLE refresh_token (
    id VARCHAR(50) PRIMARY KEY NOT NULL ,
    user_id VARCHAR(50) NOT NULL
);

CREATE SEQUENCE seq_workflow_id
    START 1;

CREATE TABLE workflow (
   id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('seq_workflow_id') ,
   gcal_id VARCHAR(254) ,
   summary VARCHAR(254) NOT NULL,
   description VARCHAR(200) ,
   timeZone VARCHAR(64),
   color VARCHAR(7)
);

CREATE TABLE workflow_user_access_role (
    user_id VARCHAR(50) REFERENCES users(id) NOT NULL ,
    workflow_id BIGINT REFERENCES workflow(id) NOT NULL ,
    role BIGINT NOT NULL
);

CREATE SEQUENCE seq_project_id
    START 1;


CREATE TABLE project (
    id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('seq_project_id') ,
    workflow_id BIGINT REFERENCES workflow(id) NOT NULL ,
    summary VARCHAR(254) NOT NULL,
    description VARCHAR(200),
    color VARCHAR(7)
);

CREATE SEQUENCE seq_project_statistics_id
    START 1;

CREATE TABLE project_statistics (
    id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('seq_project_statistics_id') ,
    project_id BIGINT REFERENCES project(id) NOT NULL ,
    date DATE NOT NULL DEFAULT CURRENT_DATE ,
    time TIME WITHOUT TIME ZONE NOT NULL DEFAULT '00:00:00'
);

CREATE SEQUENCE seq_event_id
    START 1;

CREATE TABLE event (
    id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('seq_event_id') ,
    project_id BIGINT REFERENCES project(id) NOT NULL ,
    gcal_event_id VARCHAR(32),
    ical_uid VARCHAR(26),
    summary VARCHAR(254) NOT NULL,
    description VARCHAR CHECK (length(description) <= 8192),
    created TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL ,
    updated TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL ,
    creator VARCHAR(50) REFERENCES users(id) NOT NULL,
    schedule_start TIMESTAMP WITH TIME ZONE,
    schedule_end TIMESTAMP WITH TIME ZONE,
    user_start TIMESTAMP WITH TIME ZONE,
    user_end TIMESTAMP WITH TIME ZONE,
    finished BOOLEAN NOT NULL DEFAULT FALSE,
    recurrence TEXT[],
    recurring_event_id BIGINT REFERENCES event(id),
    color VARCHAR(7)
);
