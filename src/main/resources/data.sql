INSERT INTO departments (name, description) VALUES
('Sviluppo', 'Reparto dedicato a sviluppo software'),
('Risorse Umane', 'Gestione amministrativa del personale'),
('Vendite', 'Commercializzazione e supporto clienti'),
('Amministrazione', 'Supporto generale e supervisione');

INSERT INTO employees (username, password_hash, first_name, last_name, email, role, position, hire_date, department_id) VALUES
('admin', 'PLACEHOLDER', 'Admin', 'Sistema', 'admin@azienda.it', 'ADMIN', 'Amministratore', '2024-01-01', 4),
('luca.bianchi', 'PLACEHOLDER', 'Luca', 'Bianchi', 'luca.bianchi@azienda.it', 'MANAGER', 'Manager Sviluppo', '2023-03-01', 1),
('sara.verdi', 'PLACEHOLDER', 'Sara', 'Verdi', 'sara.verdi@azienda.it', 'MANAGER', 'Manager HR', '2023-05-10', 2),
('marco.neri', 'PLACEHOLDER', 'Marco', 'Neri', 'marco.neri@azienda.it', 'MANAGER', 'Manager Vendite', '2023-06-12', 3),
('anna.gialli', 'PLACEHOLDER', 'Anna', 'Gialli', 'anna.gialli@azienda.it', 'EMPLOYEE', 'Sviluppatrice', '2024-02-15', 1),
('paolo.rossi', 'PLACEHOLDER', 'Paolo', 'Rossi', 'paolo.rossi@azienda.it', 'EMPLOYEE', 'Sviluppatore', '2024-02-20', 1),
('giulia.blu', 'PLACEHOLDER', 'Giulia', 'Blu', 'giulia.blu@azienda.it', 'EMPLOYEE', 'Venditrice', '2024-04-05', 3);

UPDATE departments SET manager_id = 2 WHERE id = 1;
UPDATE departments SET manager_id = 3 WHERE id = 2;
UPDATE departments SET manager_id = 4 WHERE id = 3;
UPDATE departments SET manager_id = 1 WHERE id = 4;

INSERT INTO payslips (employee_id, period_month, period_year, base_salary, bonus, deductions, net_amount, issued_date) VALUES
(5, 1, 2026, 2200.00, 150.00, 600.00, 1750.00, '2026-01-31'),
(5, 2, 2026, 2200.00, 100.00, 560.00, 1740.00, '2026-02-28'),
(6, 1, 2026, 2100.00, 120.00, 540.00, 1680.00, '2026-01-31'),
(6, 2, 2026, 2100.00, 100.00, 545.00, 1655.00, '2026-02-28'),
(7, 1, 2026, 2000.00, 180.00, 520.00, 1660.00, '2026-01-31');
