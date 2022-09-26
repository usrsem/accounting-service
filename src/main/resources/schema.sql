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

INSERT INTO employee
VALUES (default, 'Andrey', 'CHIEF');
INSERT INTO employee
VALUES (default, 'Tagir', 'WORKER');

INSERT INTO account
VALUES (default, 'Andrey', 5000.0, FALSE, 1);
INSERT INTO account
VALUES (default, 'Tagir', 10000.0, FALSE, 2);
INSERT INTO account
VALUES (default, 'Tinkoff', 0.0, TRUE, NULL);

INSERT INTO category
VALUES (default, 'transfer');

INSERT INTO operation
VALUES (default, 'Some description', 500.0, 1, 2, 1, 1, default);
INSERT INTO operation
VALUES (default, 'Some description', 500.0, 3, 2, 1, 2, default);
INSERT INTO operation
VALUES (default, 'Some description', 500.0, 1, 2, 1, 1, default);

SELECT o.id           as operation_id,
       o.employee_id  as employee_id,
       ta.employee_id as to_employee_id,
       o.sum          as operation_sum,
       o.created_at   as operation_created_at,
       c.name         as category_name,
       fa.name        as from_account_name,
       ta.name        as to_account_name,
       e.authority    as employee_authority,
       description
from operation as o
         LEFT JOIN category c ON o.category_id = c.id
         LEFT JOIN account fa on o.from_account_id = fa.id
         LEFT JOIN account ta on o.to_account_id = ta.id
         LEFT JOIN employee e on o.employee_id = e.id;
