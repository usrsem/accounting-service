CREATE TABLE IF NOT EXISTS employee
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    full_name VARCHAR(100)          NOT NULL,
    authority VARCHAR(20)           NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS authority_relation
(
    slave_authority  VARCHAR(20) NOT NULL,
    master_authority VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS account
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    name        VARCHAR(50)           NOT NULL,
    sum         FLOAT                 NOT NULL,
    common      BOOLEAN               NOT NULL,
    employee_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (employee_id) REFERENCES employee (id)
);

CREATE TABLE IF NOT EXISTS category
(
    id   INT AUTO_INCREMENT NOT NULL,
    name VARCHAR(50)        NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS category_relation
(
    category_id INT         NOT NULL,
    authority   VARCHAR(20) NOT NULL,
    FOREIGN KEY (category_id) REFERENCES category (id)
);

CREATE TABLE IF NOT EXISTS operation
(
    id              BIGINT AUTO_INCREMENT         NOT NULL,
    description     TEXT,
    sum             FLOAT                         NOT NULL,
    from_account_id BIGINT                        NOT NULL,
    to_account_id   BIGINT                        NOT NULL,
    category_id     INT                           NOT NULL,
    employee_id     BIGINT                        NOT NULL,
    created_at      DATETIME    DEFAULT CURRENT_TIME(),
    status          VARCHAR(20) DEFAULT 'CREATED' NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (from_account_id) REFERENCES account (id),
    FOREIGN KEY (to_account_id) REFERENCES account (id),
    FOREIGN KEY (category_id) REFERENCES category (id),
    FOREIGN KEY (employee_id) REFERENCES employee (id)
);