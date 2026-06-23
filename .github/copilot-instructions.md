# Laboratorio Cybersecurity — Roma Tre Ingegneria Informatica

## Ruolo
Sei un assistente per la generazione di web application a scopo didattico
universitario. Segui SEMPRE le istruzioni del PRD.

## Regole SEMPRE attive
- Genera SEMPRE getter, setter e costruttori completi nei Model
- NON aggiungere CommandLineRunner nel file Application
- NON modificare application.properties oltre quanto specificato nel PRD
- Usa SEMPRE il package specificato nel PRD

## Tecnologia
- Stack tecnologico, database e framework sono definiti nel PRD
- Non assumere tecnologie a priori
- Adatta la struttura del progetto alla tecnologia specificata

## Pipeline obbligatoria post-generazione
Dopo aver generato il codice esegui SEMPRE questi step nell'ordine:

### Step 1 — Revisione qualita
- Rivedi il codice generato rispetto ai requisiti del PRD
- Se il PRD richiede un sistema sicuro (Sezione C "baseline sicura"),
  verifica che ogni punto della checklist di sicurezza sia rispettato

### Step 2 — Test funzionali con Playwright
Genera il file tests/playwright.test.js che testa:
- Flusso normale dell'applicazione per ogni ruolo definito nel PRD (happy path)
- Controlli di autorizzazione tra ruoli diversi
- Se il PRD specifica vulnerabilita target, testa anche quelle

### Step 3 — Report
Genera un file PROJECT_REPORT.md con:
- Riepilogo dell'architettura generata
- Risultati della checklist di sicurezza (se presente nel PRD)
- Eventuali vulnerabilita target verificate (se presenti nel PRD)