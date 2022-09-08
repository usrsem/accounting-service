DROP TABLE IF EXISTS operation;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS employee;

CREATE TABLE employee
(
    id        BIGINT auto_increment NOT NULL,
    full_name VARCHAR(100)          NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE account
(
    id          BIGINT auto_increment NOT NULL,
    name        VARCHAR(50)           NOT NULL,
    sum         FLOAT                 NOT NULL,
    common      BOOLEAN               NOT NULL,
    employee_id BIGINT                NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (employee_id) REFERENCES employee (id)
);

CREATE TABLE category
(
    id   INT auto_increment NOT NULL,
    name VARCHAR(50)        NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE operation
(
    id              BIGINT auto_increment NOT NULL,
    description     TEXT,
    sum             FLOAT                 NOT NULL,
    from_account_id BIGINT                NOT NULL,
    to_account_id   BIGINT                NOT NULL,
    category_id     INT                   NOT NULL,
    employee_id     BIGINT                NOT NULL,
    created_at      DATETIME DEFAULT CURRENT_TIME(),
    PRIMARY KEY (id),
    FOREIGN KEY (from_account_id) REFERENCES account (id),
    FOREIGN KEY (to_account_id) REFERENCES account (id),
    FOREIGN KEY (category_id) REFERENCES category (id),
    FOREIGN KEY (employee_id) REFERENCES employee (id)
);

INSERT INTO employee
VALUES (1, 'Andrey');
INSERT INTO employee
VALUES (2, 'Tagir');

INSERT INTO account
VALUES (1, 'Andrey', 5000.0, FALSE, 1);
INSERT INTO account
VALUES (2, 'Tagir', 10000.0, FALSE, 2);

INSERT INTO category
VALUES (1, 'transfer');

INSERT INTO operation
VALUES (default, 'Some description', 500.0, 2, 1, 1, 1, default);
