--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true

INSERT INTO permission(id, role, value)
VALUES (1, 'admin', 'Адміністратор')
ON CONFLICT DO NOTHING;

INSERT INTO permission(id, role, value)
VALUES (2, 'auditor', 'Аудитор')
ON CONFLICT DO NOTHING;

INSERT INTO permission(id, role, value)
VALUES (3, 'methodologist', 'Методолог')
ON CONFLICT DO NOTHING;

-- Predefined permissions for admin and default user

INSERT INTO user_permission
VALUES (1, 1)
ON CONFLICT DO NOTHING;

INSERT INTO user_permission
VALUES (2, 2)
ON CONFLICT DO NOTHING;

INSERT INTO user_permission
VALUES (3, 3)
ON CONFLICT DO NOTHING;

INSERT INTO user_permission
VALUES (4, 2)
ON CONFLICT DO NOTHING;