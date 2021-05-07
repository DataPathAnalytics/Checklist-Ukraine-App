INSERT INTO users(email, firstName, lastName, password, disable, locked, removed, superAdmin, registeredDate, dateModified)
VALUES ('admin@datapath.com', 'Иван', 'Иванов', '$2a$10$HZZ/FSl2ijIRArq4J/t5MupaLHSZOUulzqivgh5Su.uXwLuJ4TCne', false, false, false, true, now(), now())
ON CONFLICT DO NOTHING;

INSERT INTO users(email, firstName, lastName, password, disable, locked, removed, superAdmin, registeredDate, dateModified)
VALUES ('auditor@datapath.com', 'Антон', 'Антонов', '$2a$10$ZATc1/JoMDWr6tRVZXXrQONrBb91yDLEcLMH1jELb8ANwP09SBpK.', false, false, false, false, now(), now())
ON CONFLICT DO NOTHING;

INSERT INTO users(email, firstName, lastName, password, disable, locked, removed, superAdmin, registeredDate, dateModified)
VALUES ('methodolog@datapath.com', 'Сергей', 'Сергеев', '$2a$10$ZATc1/JoMDWr6tRVZXXrQONrBb91yDLEcLMH1jELb8ANwP09SBpK.', false, false, false, false, now(), now())
ON CONFLICT DO NOTHING;
