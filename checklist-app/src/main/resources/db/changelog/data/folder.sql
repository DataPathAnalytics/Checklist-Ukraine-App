--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true

INSERT INTO template_folder(name)
VALUES ('Директива для темплейтов')
ON CONFLICT DO NOTHING;

INSERT INTO template_config_folder(name)
VALUES ('Директива для конфигов темплейтов')
ON CONFLICT DO NOTHING;