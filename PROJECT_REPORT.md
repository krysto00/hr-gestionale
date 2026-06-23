# PROJECT_REPORT

## Architettura generata
- Package principale: `it.uniroma3.hrgestionale`
- Modello dati conforme al PRD: `Employee`, `Department`, `Payslip`, `Role`
- Architettura MVC con controller Spring MVC, Thymeleaf per le view e Spring Data JPA per il persistence layer
- Security: Spring Security con autenticazione `UserDetailsService` personalizzata, `BCryptPasswordEncoder` e autorizzazione basata sui ruoli `ADMIN`, `MANAGER`, `EMPLOYEE`
- Inizializzazione dati: `schema.sql` e `data.sql` per la creazione dello schema PostgreSQL e il popolamento iniziale
- Component di inizializzazione separato: `PasswordHashUpdater` aggiorna le password placeholder con hash BCrypt in fase di avvio

## Checklist di sicurezza
- SQL Injection: uso esclusivo di Spring Data JPA con query parametrizzate e repository standard
- XSS: tutte le view usano `th:text` per l'output dinamico; non è stato usato `th:utext`
- IDOR: il controller `PayslipController` verifica l'appartenenza dell'utente alle risorse prima di mostrare il dettaglio
- CSRF: protezione attiva di Spring Security per tutti i form POST
- Open Redirect: nessun redirect dinamico basato su parametri utente esterni
- Autenticazione: password hashed con BCrypt tramite `PasswordEncoder`
- Autorizzazione: ogni endpoint sensibile è protetto con ruoli e controlli di accesso

## Vulnerabilità target e validazioni
- Questo progetto è stato generato come baseline sicura, senza vulnerabilità intenzionali inserite
- Il test Playwright verifica i flussi happy path per ADMIN, MANAGER ed EMPLOYEE e controlla i vincoli di autorizzazione tra ruoli

## Note di implementazione
- La sicurezza è stata rispettata come previsto dalla Sezione C del PRD
- Il component `PasswordHashUpdater` aggiorna solo gli utenti con `password_hash = 'PLACEHOLDER'`
- Il template engine Thymeleaf è stato usato senza output non sanitizzato
