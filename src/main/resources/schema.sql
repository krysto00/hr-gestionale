CREATE TABLE departments (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    manager_id INTEGER
);

CREATE TABLE employees (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL,
    position VARCHAR(100),
    hire_date DATE NOT NULL,
    department_id INTEGER REFERENCES departments(id)
);

ALTER TABLE departments ADD CONSTRAINT fk_departments_manager FOREIGN KEY(manager_id) REFERENCES employees(id);

CREATE TABLE payslips (
    id SERIAL PRIMARY KEY,
    employee_id INTEGER NOT NULL REFERENCES employees(id),
    period_month INTEGER NOT NULL,
    period_year INTEGER NOT NULL,
    base_salary DECIMAL(10,2) NOT NULL,
    bonus DECIMAL(10,2) DEFAULT 0,
    deductions DECIMAL(10,2) NOT NULL,
    net_amount DECIMAL(10,2) NOT NULL,
    issued_date DATE NOT NULL
);
