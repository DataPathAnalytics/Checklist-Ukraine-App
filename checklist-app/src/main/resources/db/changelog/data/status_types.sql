--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true

INSERT INTO activity_status
VALUES (1, 'В процесі')
ON CONFLICT DO NOTHING;

INSERT INTO activity_status
VALUES (2, 'Завершено')
ON CONFLICT DO NOTHING;


INSERT INTO session_status
VALUES (1, 'В процесі')
ON CONFLICT DO NOTHING;

INSERT INTO session_status
VALUES (2, 'Завершено')
ON CONFLICT DO NOTHING;


INSERT INTO template_config_type
VALUES (1, 'Control Activity')
ON CONFLICT DO NOTHING;

INSERT INTO template_config_type
VALUES (2, 'Response Session')
ON CONFLICT DO NOTHING;