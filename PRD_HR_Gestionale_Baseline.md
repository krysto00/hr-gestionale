# Project Requirement Document
## HR Gestionale — Sistema Aziendale Pulito (Baseline)

---

## Sezione A — Specifica Funzionale

### Descrizione dell'applicazione
HR Gestionale e una piattaforma web per la gestione aziendale del personale.
Permette agli amministratori di gestire dipendenti, reparti e buste paga,
ai manager di consultare i dati del proprio reparto, e ai dipendenti di
visualizzare le proprie informazioni personali e le proprie buste paga.

### Ruoli e permessi

| Ruolo | Permessi |
|-------|----------|
| **ADMIN** | Accesso completo: gestione dipendenti, reparti, buste paga di tutta l'azienda |
| **MANAGER** | Visualizza dipendenti e buste paga solo del proprio reparto |
| **EMPLOYEE** | Visualizza solo i propri dati personali e le proprie buste paga |

### Entita principali

**Dipendente (Employee)**
- Nome, cognome, email, username, password (hash)
- Ruolo (ADMIN / MANAGER / EMPLOYEE)
- Reparto di appartenenza
- Data di assunzione
- Posizione/qualifica

**Reparto (Department)**
- Nome reparto (es. "Sviluppo", "Risorse Umane", "Vendite", "Amministrazione")
- Manager responsabile (riferimento a un Employee con ruolo MANAGER)
- Descrizione

**Busta Paga (Payslip)**
- Riferimento al dipendente
- Mese/anno di riferimento
- Stipendio base (lordo)
- Bonus/straordinari
- Detrazioni (contributi, tasse)
- Netto in busta
- Data emissione

### Funzionalita principali

1. **Autenticazione**
   - Login con username e password
   - Logout
   - Sessione persistente (HttpSession)

2. **Dashboard** (varia per ruolo)
   - ADMIN: panoramica generale, numero dipendenti, reparti, statistiche aziendali
   - MANAGER: panoramica del proprio reparto, lista membri del team
   - EMPLOYEE: riepilogo personale, ultima busta paga

3. **Gestione Dipendenti** (ADMIN)
   - Lista completa dipendenti con filtri per reparto
   - Dettaglio dipendente (/employees/{id})
   - Creazione nuovo dipendente
   - Modifica dati dipendente
   - Assegnazione a reparto e ruolo

4. **Gestione Reparti** (ADMIN)
   - Lista reparti
   - Dettaglio reparto con lista membri (/departments/{id})
   - Creazione/modifica reparto
   - Assegnazione manager responsabile

5. **Gestione Buste Paga**
   - ADMIN: lista tutte le buste paga, creazione nuova busta paga per qualsiasi dipendente
   - MANAGER: lista buste paga dei membri del proprio reparto (solo visualizzazione)
   - EMPLOYEE: lista delle proprie buste paga, dettaglio busta paga (/payslips/{id})

### Flussi principali

1. **Flusso Admin**: Login → Dashboard Admin → Gestione Dipendenti → Crea/Modifica dipendente → Assegna a reparto
2. **Flusso Admin Buste Paga**: Login → Dashboard Admin → Buste Paga → Crea nuova busta paga → Seleziona dipendente → Inserisci importi
3. **Flusso Manager**: Login → Dashboard Manager → Lista Team → Dettaglio dipendente del team → Buste paga del team
4. **Flusso Employee**: Login → Dashboard Employee → Le Mie Buste Paga → Dettaglio busta paga (/payslips/{id})

### Dati di esempio (seed)

**Reparti**
- Sviluppo (manager: Luca Bianchi)
- Risorse Umane (manager: Sara Verdi)
- Vendite (manager: Marco Neri)

**Dipendenti**
| Username | Password | Nome | Ruolo | Reparto |
|----------|----------|------|-------|---------|
| admin | admin123 | Admin Sistema | ADMIN | Amministrazione |
| luca.bianchi | manager123 | Luca Bianchi | MANAGER | Sviluppo |
| sara.verdi | manager123 | Sara Verdi | MANAGER | Risorse Umane |
| marco.neri | manager123 | Marco Neri | MANAGER | Vendite |
| anna.gialli | dipendente123 | Anna Gialli | EMPLOYEE | Sviluppo |
| paolo.rossi | dipendente123 | Paolo Rossi | EMPLOYEE | Sviluppo |
| giulia.blu | dipendente123 | Giulia Blu | EMPLOYEE | Vendite |

**Buste paga**
- Ogni dipendente ha 2-3 buste paga (mesi diversi) con importi realistici
- Esempio: Anna Gialli — Gennaio 2026, stipendio base 2200, netto 1750

---

## Sezione B — Specifica Tecnica

### Stack tecnologico
- **Linguaggio**: Java 17
- **Framework**: Spring Boot 3.2
- **Database**: PostgreSQL (database: hrgestionale)
- **Template engine**: Thymeleaf
- **ORM**: Spring Data JPA + Hibernate
- **Sessione**: HttpSession
- **Sicurezza password**: BCrypt (password hashate, NON in chiaro)
- **Package**: it.uniroma3.hrgestionale
- **Porta**: 8080

### Schema database

```sql
CREATE TABLE departments (
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    manager_id  INTEGER
);

CREATE TABLE employees (
    id            SERIAL PRIMARY KEY,
    username      VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name    VARCHAR(100) NOT NULL,
    last_name     VARCHAR(100) NOT NULL,
    email         VARCHAR(150) NOT NULL UNIQUE,
    role          VARCHAR(20) NOT NULL,  -- ADMIN, MANAGER, EMPLOYEE
    position      VARCHAR(100),
    hire_date     DATE NOT NULL,
    department_id INTEGER REFERENCES departments(id)
);

CREATE TABLE payslips (
    id          SERIAL PRIMARY KEY,
    employee_id INTEGER NOT NULL REFERENCES employees(id),
    period_month INTEGER NOT NULL,
    period_year  INTEGER NOT NULL,
    base_salary  DECIMAL(10,2) NOT NULL,
    bonus        DECIMAL(10,2) DEFAULT 0,
    deductions   DECIMAL(10,2) NOT NULL,
    net_amount   DECIMAL(10,2) NOT NULL,
    issued_date  DATE NOT NULL
);
```

### Struttura file da generare

```
src/main/java/it/uniroma3/hrgestionale/
├── model/
│   ├── Employee.java
│   ├── Department.java
│   ├── Payslip.java
│   └── Role.java (enum: ADMIN, MANAGER, EMPLOYEE)
├── repository/
│   ├── EmployeeRepository.java
│   ├── DepartmentRepository.java
│   └── PayslipRepository.java
├── controller/
│   ├── AuthController.java       (login, logout)
│   ├── DashboardController.java  (dashboard differenziata per ruolo)
│   ├── EmployeeController.java   (CRUD dipendenti, solo ADMIN)
│   ├── DepartmentController.java (CRUD reparti, solo ADMIN)
│   └── PayslipController.java    (gestione buste paga)
├── security/
│   ├── SecurityConfig.java       (Spring Security: autenticazione e autorizzazione)
│   └── PasswordEncoderConfig.java (BCrypt)
└── HrGestionaleApplication.java

src/main/resources/templates/
├── login.html
├── dashboard-admin.html
├── dashboard-manager.html
├── dashboard-employee.html
├── employees/
│   ├── list.html
│   ├── detail.html
│   └── form.html
├── departments/
│   ├── list.html
│   └── detail.html
└── payslips/
    ├── list.html
    └── detail.html

tests/
└── playwright.test.js  (test funzionali per ogni ruolo)
```

### Dipendenze Maven aggiuntive
- spring-boot-starter-security (per autenticazione e autorizzazione basata su ruoli)
- spring-boot-starter-validation (validazione input form)

---

## Sezione C — Specifica Sicurezza

### Stato: SISTEMA PULITO — BASELINE

Questo sistema NON deve contenere alcuna vulnerabilita intenzionale.
E la baseline sicura su cui verranno applicate le patch di vulnerabilita
nelle fasi successive del progetto (Cyber Range).

### Requisiti di sicurezza obbligatori

- **Autenticazione robusta**: password hashate con BCrypt, mai in chiaro
- **Autorizzazione basata su ruoli**: ogni endpoint verifica il ruolo dell'utente
  tramite Spring Security (@PreAuthorize o configurazione SecurityConfig)
- **Controllo di accesso sulle risorse**:
  * EMPLOYEE puo vedere SOLO le proprie buste paga (controllo ownership)
  * MANAGER puo vedere SOLO i dipendenti e le buste paga del proprio reparto
  * ADMIN ha accesso completo
- **Query parametrizzate**: SEMPRE usare JpaRepository o query con @Param,
  MAI concatenazione di stringhe
- **Output sicuro**: SEMPRE usare th:text in Thymeleaf, MAI th:utext
- **CSRF protection**: ATTIVA (Spring Security default)
- **Validazione input**: usare @Valid e annotazioni Bean Validation
  (@NotNull, @Email, @Size, ecc.)
- **Redirect sicuri**: nessun parametro "next" o redirect controllato dall'utente

### Checklist di verifica pre-consegna

| Controllo | Requisito |
|-----------|-----------|
| SQL Injection | Assente — solo query JPA/JPQL parametrizzate |
| XSS | Assente — th:text ovunque |
| IDOR | Assente — controllo ownership su payslips/{id} |
| CSRF | Protezione attiva su tutti i form POST |
| Open Redirect | Assente — nessun redirect dinamico da parametri utente |
| Autenticazione | Password hashate con BCrypt |
| Autorizzazione | Ogni endpoint verifica il ruolo richiesto |

### Note per Copilot

Questo e un progetto di BASELINE SICURA. A differenza dei progetti precedenti
del laboratorio, qui le buone pratiche di sicurezza vanno SEMPRE applicate.
Il codice generato sara successivamente usato come base per applicare patch
di vulnerabilita una alla volta in branch Git separati — per questo motivo
e fondamentale che il codice base sia il piu sicuro e pulito possibile,
cosi che ogni vulnerabilita introdotta in seguito sia chiaramente isolabile
e attribuibile a una patch specifica.
