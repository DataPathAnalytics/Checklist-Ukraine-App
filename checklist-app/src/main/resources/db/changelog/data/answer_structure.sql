--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true

INSERT INTO answer_structure
VALUES (1, 'Текстове значення', false)
ON CONFLICT DO NOTHING;

INSERT INTO answer_structure
VALUES (2, 'Текстове значення (унікальний числовий ідентифікатор)', false)
ON CONFLICT DO NOTHING;

INSERT INTO answer_structure
VALUES (3, 'Текстове значення (унікальний текстовий ідентифікатор)', false)
ON CONFLICT DO NOTHING;

INSERT INTO answer_structure
VALUES (4, 'Період', false)
ON CONFLICT DO NOTHING;

INSERT INTO answer_structure
VALUES (5, 'Організація', false)
ON CONFLICT DO NOTHING;

INSERT INTO answer_structure
VALUES (6, 'Контракт', false)
ON CONFLICT DO NOTHING;

INSERT INTO answer_structure
VALUES (7, 'За замовчуванням', true)
ON CONFLICT DO NOTHING;

INSERT INTO answer_structure
VALUES (8, 'Сума', false)
ON CONFLICT DO NOTHING;

INSERT INTO answer_structure
VALUES (9, 'Дата', false)
ON CONFLICT DO NOTHING;

INSERT INTO answer_structure
VALUES (10, 'Аудитор', false)
ON CONFLICT DO NOTHING;

-- field description
INSERT INTO field_description
VALUES (1, 'value', null, 'STRING', 'INPUT', false, false, false, 1)
ON CONFLICT DO NOTHING;

INSERT INTO field_description
VALUES (2, 'identifier', 'Идентификатор', 'NUMBER', 'INPUT', false, false, true, 2)
ON CONFLICT DO NOTHING;
INSERT INTO field_description
VALUES (3, 'name', 'Значення', 'STRING', 'INPUT', false, false, false, 2)
ON CONFLICT DO NOTHING;

INSERT INTO field_description
VALUES (4, 'identifier', 'Идентификатор', 'STRING', 'INPUT', false, false, true, 3)
ON CONFLICT DO NOTHING;
INSERT INTO field_description
VALUES (5, 'name', 'Значення', 'STRING', 'INPUT', false, false, false, 3)
ON CONFLICT DO NOTHING;

INSERT INTO field_description
VALUES (6, 'startDate', 'Початок періоду', 'DATE', 'DATE', false, false, false, 4)
ON CONFLICT DO NOTHING;
INSERT INTO field_description
VALUES (7, 'endDate', 'Кінець періоду', 'DATE', 'DATE', false, false, false, 4)
ON CONFLICT DO NOTHING;

INSERT INTO field_description
VALUES (8, 'identifier', 'Идентификатор', 'STRING', 'INPUT', false, false, true, 5)
ON CONFLICT DO NOTHING;
INSERT INTO field_description
VALUES (9, 'name', 'Назва', 'STRING', 'INPUT', false, false, false, 5)
ON CONFLICT DO NOTHING;

INSERT INTO field_description
VALUES (10, 'contractNumber', 'Номер контракту', 'STRING', 'INPUT', false, false, true, 6)
ON CONFLICT DO NOTHING;
INSERT INTO field_description
VALUES (11, 'amount', 'Сума договору', 'NUMBER', 'INPUT', false, false, false, 6)
ON CONFLICT DO NOTHING;
INSERT INTO field_description
VALUES (12, 'currency', 'Валюта договору', 'STRING', 'INPUT', false, false, false, 6)
ON CONFLICT DO NOTHING;
INSERT INTO field_description
VALUES (13, 'startDate', 'Початок дії договору', 'DATE', 'DATE', false, false, false, 6)
ON CONFLICT DO NOTHING;
INSERT INTO field_description
VALUES (14, 'endDate', 'Кiнець дії договору', 'DATE', 'DATE', false, false, false, 6)
ON CONFLICT DO NOTHING;

INSERT INTO field_description
VALUES (15, 'value', null, 'STRING', 'RADIOS', false, false, false, 7)
ON CONFLICT DO NOTHING;

INSERT INTO field_description
VALUES (16, 'amount', 'Сума', 'NUMBER', 'INPUT', false, false, false, 8)
ON CONFLICT DO NOTHING;
INSERT INTO field_description
VALUES (17, 'currency', 'Валюта', 'STRING', 'INPUT', false, false, false, 8)
ON CONFLICT DO NOTHING;

INSERT INTO field_description
VALUES (18, 'value', null, 'DATE', 'DATE', false, false, false, 9)
ON CONFLICT DO NOTHING;

INSERT INTO field_description
VALUES (19, 'id', 'Идентификатор', 'NUMBER', 'INPUT', false, false, true, 10)
ON CONFLICT DO NOTHING;
INSERT INTO field_description
VALUES (20, 'name', 'Имя', 'STRING', 'INPUT', false, false, false, 10)
ON CONFLICT DO NOTHING;
INSERT INTO field_description
VALUES (21, 'email', 'Електронная почта', 'STRING', 'INPUT', false, false, false, 10)
ON CONFLICT DO NOTHING;

-- values
INSERT INTO value
VALUES (1, 'Так', 15)
ON CONFLICT DO NOTHING;
INSERT INTO value
VALUES (2, 'Нi', 15)
ON CONFLICT DO NOTHING;
INSERT INTO value
VALUES (3, 'Не застосовується', 15)
ON CONFLICT DO NOTHING;