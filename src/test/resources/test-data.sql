INSERT INTO employee
VALUES (default, 'Andrey', 'CHIEF');
INSERT INTO employee
VALUES (default, 'Tagir', 'WORKER');
INSERT INTO employee
VALUES (default, 'Danya', 'ACCOUNTANT');
INSERT INTO employee
VALUES (default, 'Vanya', 'WORKER');
INSERT INTO employee
VALUES (default, 'Chief2', 'CHIEF');

INSERT INTO authority_relation
VALUES ('CHIEF', 'ACCOUNTANT');
INSERT INTO authority_relation
VALUES ('WORKER', 'CHIEF');
INSERT INTO authority_relation
VALUES ('WORKER', 'ACCOUNTANT');

INSERT INTO account
VALUES (default, 'Andrey', 5000.0, FALSE, 1);
INSERT INTO account
VALUES (default, 'Tagir', 10000.0, FALSE, 2);
INSERT INTO account
VALUES (default, 'Tinkoff', 100000.0, TRUE, NULL);
INSERT INTO account
VALUES (default, 'Danya', 'Infinity', FALSE, 3);
INSERT INTO account
VALUES (default, 'Vanya', 100, FALSE, 4);
INSERT INTO account
VALUES (default, 'Chief2', 5000.0, FALSE, 5);

INSERT INTO category
VALUES (default, 'transfer');
INSERT INTO category
VALUES (default, 'accounting');
INSERT INTO category
VALUES (default, 'salary');

INSERT INTO category_relation
VALUES (1, 'WORKER');
INSERT INTO category_relation
VALUES (1, 'CHIEF');
INSERT INTO category_relation
VALUES (2, 'ACCOUNTANT');
INSERT INTO category_relation
VALUES (3, 'CHIEF');

-- Operations from worker (3)
INSERT INTO operation
VALUES (default, 'Operation from common account', 500.0, 3, null, 1, 2, '2022-02-05 23.59.59', default);
INSERT INTO operation
VALUES (default, 'Another operation from common account', 1500.0, 3, null, 1, 2, '2022-02-05 23.59.59', default);
INSERT INTO operation
VALUES (default, 'Operation from worker account', 700.0, 2, 5, 1, 2, '2022-02-05 23.59.59', default);
-- Operations from chief (4)
INSERT INTO operation
VALUES (default, 'Operation from chief to Tagir', 500.0, 1, 2, 1, 1, '2022-02-05 23.59.59', default);
INSERT INTO operation
VALUES (default, 'Operation from chief to Vanya', 500.0, 1, 5, 1, 1, '2022-02-05 23.59.59', default);
INSERT INTO operation
VALUES (default, 'Very old operation from chief to Tagir', 500.0, 1, 2, 1, 1, '2022-02-05 23.59.59', default);
INSERT INTO operation
VALUES (default, 'Chief withdraws from common account', 150000.0, 3, NULL, 1, 1, '2022-02-05 23.59.59', default);
-- Operations from accountant to chief (2)
INSERT INTO operation
VALUES (default, 'Operation from accountant to chief', 20000.0, 4, 1, 2, 3, '2022-02-05 23.59.59', default);
INSERT INTO operation
VALUES (default, 'Operation from accountant to another chief', 20000.0, 4, 6, 2, 3, '2022-02-05 23.59.59', default);
