--liquibase formatted sql

--changeset andrey_pylypchuk:1 splitStatements:false runOnChange:true

INSERT INTO users(email, first_name, last_name, password, disable, locked, removed, super_admin )
VALUES ('admin@datapath.com', 'Иван', 'Иванов', '$2a$10$HZZ/FSl2ijIRArq4J/t5MupaLHSZOUulzqivgh5Su.uXwLuJ4TCne', false, false, false, true)
ON CONFLICT DO NOTHING;

INSERT INTO users(email, first_name, last_name, password, disable, locked, removed, super_admin )
VALUES ('auditor@datapath.com', 'Антон', 'Антонов', '$2a$10$ZATc1/JoMDWr6tRVZXXrQONrBb91yDLEcLMH1jELb8ANwP09SBpK.', false, false, false, false)
ON CONFLICT DO NOTHING;

INSERT INTO users(email, first_name, last_name, password, disable, locked, removed, super_admin)
VALUES ('methodolog@datapath.com', 'Сергей', 'Сергеев', '$2a$10$ZATc1/JoMDWr6tRVZXXrQONrBb91yDLEcLMH1jELb8ANwP09SBpK.', false, false, false, false)
ON CONFLICT DO NOTHING;
