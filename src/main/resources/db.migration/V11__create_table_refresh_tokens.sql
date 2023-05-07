CREATE TABLE IF NOT EXISTS course_management.refresh_tokens
(
    id              SERIAL constraint pk_refresh_tokens primary key,
    user_id         INTEGER,
    token           VARCHAR     NOT NULL,
    created_date    timestamp   NOT NULL,
    modified_date   timestamp   NOT NULL,
    created_by      VARCHAR(50) NOT NULL,
    modified_by     VARCHAR(50) NOT NULL,
    constraint fk_refresh_tokens_user_id foreign key (user_id) references course_management.users (id) ON DELETE SET NULL,
    CONSTRAINT pk_refresh_tokens_user_id UNIQUE (user_id, token)
);
