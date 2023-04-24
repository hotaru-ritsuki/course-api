CREATE TABLE IF NOT EXISTS course_management.users
(
    user_id       SERIAL constraint pk_users primary key,
    first_name    VARCHAR(50)  NOT NULL,
    last_name     VARCHAR(50)  NOT NULL,
    email         VARCHAR(50)  NOT NULL,
    password      VARCHAR(255) NOT NULL,
    role          VARCHAR(20),
    created_date  timestamp    NOT NULL,
    modified_date timestamp    NOT NULL,
    created_by    VARCHAR(50)  NOT NULL,
    modified_by   VARCHAR(50)  NOT NULL
);