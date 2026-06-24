# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: tests\playwright.test.js >> HR Gestionale - Test IDOR (deve essere ASSENTE) >> employee NON accede a busta paga di altro dipendente (IDOR check)
- Location: tests\playwright.test.js:94:7

# Error details

```
Error: expect(received).not.toContain(expected) // indexOf

Expected substring: not "Paolo"
Received string:        "

Dettaglio Busta Paga

    Torna all'elenco |
    Dashboard |
    Logout


    Dipendente: Paolo Rossi
    Periodo: 1/2026
    Stipendio base: 2100.00
    Bonus: 0.00
    Detrazioni: 430.00
    Netto: 1670.00
    Data emissione: 2026-01-31




"
```

# Page snapshot

```yaml
- main [ref=e2]:
  - heading "Dettaglio Busta Paga" [level=1] [ref=e3]
  - navigation [ref=e4]:
    - link "Torna all'elenco" [ref=e5] [cursor=pointer]:
      - /url: /payslips
    - text: "|"
    - link "Dashboard" [ref=e6] [cursor=pointer]:
      - /url: /dashboard
    - text: "|"
    - link "Logout" [ref=e7] [cursor=pointer]:
      - /url: /logout
  - generic [ref=e8]:
    - paragraph [ref=e9]: "Dipendente: Paolo Rossi"
    - paragraph [ref=e10]: "Periodo: 1/2026"
    - paragraph [ref=e11]: "Stipendio base: 2100.00"
    - paragraph [ref=e12]: "Bonus: 0.00"
    - paragraph [ref=e13]: "Detrazioni: 430.00"
    - paragraph [ref=e14]: "Netto: 1670.00"
    - paragraph [ref=e15]: "Data emissione: 2026-01-31"
```

# Test source

```ts
  1   | import { test, expect } from '@playwright/test';
  2   | 
  3   | const baseUrl = 'http://localhost:8080';
  4   | 
  5   | async function login(page, username, password) {
  6   |   await page.goto(`${baseUrl}/login`);
  7   |   await page.fill('#username', username);
  8   |   await page.fill('#password', password);
  9   |   await page.click('button[type=submit]');
  10  | }
  11  | 
  12  | test.describe('HR Gestionale - Autenticazione', () => {
  13  |   test('login admin riuscito', async ({ page }) => {
  14  |     await login(page, 'admin', 'admin123');
  15  |     await expect(page).toHaveURL(/dashboard/);
  16  |     await expect(page.locator('text=Dashboard Admin')).toBeVisible();
  17  |   });
  18  | 
  19  |   test('login manager riuscito', async ({ page }) => {
  20  |     await login(page, 'luca.bianchi', 'manager123');
  21  |     await expect(page).toHaveURL(/dashboard/);
  22  |     await expect(page.locator('text=Dashboard Manager')).toBeVisible();
  23  |   });
  24  | 
  25  |   test('login employee riuscito', async ({ page }) => {
  26  |     await login(page, 'anna.gialli', 'dipendente123');
  27  |     await expect(page).toHaveURL(/dashboard/);
  28  |     await expect(page.locator('text=Dashboard Employee')).toBeVisible();
  29  |   });
  30  | 
  31  |   test('login con credenziali errate fallisce', async ({ page }) => {
  32  |     await login(page, 'admin', 'passwordsbagliata');
  33  |     await expect(page).toHaveURL(/login/);
  34  |   });
  35  | });
  36  | 
  37  | test.describe('HR Gestionale - Autorizzazione ADMIN', () => {
  38  |   test('admin accede a dipendenti', async ({ page }) => {
  39  |     await login(page, 'admin', 'admin123');
  40  |     await page.goto(`${baseUrl}/employees`);
  41  |     expect(page.url()).toContain('/employees');
  42  |     const status = await page.evaluate(() => document.title);
  43  |     expect(status).not.toContain('Whitelabel');
  44  |   });
  45  | 
  46  |   test('admin accede a reparti', async ({ page }) => {
  47  |     await login(page, 'admin', 'admin123');
  48  |     await page.goto(`${baseUrl}/departments`);
  49  |     expect(page.url()).toContain('/departments');
  50  |   });
  51  | 
  52  |   test('admin accede a tutte le buste paga', async ({ page }) => {
  53  |     await login(page, 'admin', 'admin123');
  54  |     await page.goto(`${baseUrl}/payslips`);
  55  |     expect(page.url()).toContain('/payslips');
  56  |   });
  57  | });
  58  | 
  59  | test.describe('HR Gestionale - Controllo Accessi MANAGER', () => {
  60  |   test('manager NON accede a /employees (403)', async ({ page }) => {
  61  |     await login(page, 'luca.bianchi', 'manager123');
  62  |     const response = await page.goto(`${baseUrl}/employees`);
  63  |     expect(response.status()).toBe(403);
  64  |   });
  65  | 
  66  |   test('manager NON accede a /departments (403)', async ({ page }) => {
  67  |     await login(page, 'luca.bianchi', 'manager123');
  68  |     const response = await page.goto(`${baseUrl}/departments`);
  69  |     expect(response.status()).toBe(403);
  70  |   });
  71  | });
  72  | 
  73  | test.describe('HR Gestionale - Controllo Accessi EMPLOYEE', () => {
  74  |   test('employee NON accede a /employees (403)', async ({ page }) => {
  75  |     await login(page, 'anna.gialli', 'dipendente123');
  76  |     const response = await page.goto(`${baseUrl}/employees`);
  77  |     expect(response.status()).toBe(403);
  78  |   });
  79  | 
  80  |   test('employee NON accede a /departments (403)', async ({ page }) => {
  81  |     await login(page, 'anna.gialli', 'dipendente123');
  82  |     const response = await page.goto(`${baseUrl}/departments`);
  83  |     expect(response.status()).toBe(403);
  84  |   });
  85  | 
  86  |   test('employee accede alle proprie buste paga', async ({ page }) => {
  87  |     await login(page, 'anna.gialli', 'dipendente123');
  88  |     await page.goto(`${baseUrl}/payslips`);
  89  |     expect(page.url()).toContain('/payslips');
  90  |   });
  91  | });
  92  | 
  93  | test.describe('HR Gestionale - Test IDOR (deve essere ASSENTE)', () => {
  94  |   test('employee NON accede a busta paga di altro dipendente (IDOR check)', async ({ page }) => {
  95  |     await login(page, 'anna.gialli', 'dipendente123');
  96  |     // Anna ha payslip 1 e 2. Payslip 3 e 4 sono di Paolo
  97  |     const response = await page.goto(`${baseUrl}/payslips/3`);
  98  |     // Deve essere bloccato: redirect o 403, MAI 200 con dati di Paolo
  99  |     if (response.status() === 200) {
  100 |       const content = await page.textContent('body');
> 101 |       expect(content).not.toContain('Paolo');
      |                           ^ Error: expect(received).not.toContain(expected) // indexOf
  102 |     } else {
  103 |       expect([302, 403, 404]).toContain(response.status());
  104 |     }
  105 |   });
  106 | 
  107 |   test('employee accede alla propria busta paga', async ({ page }) => {
  108 |     await login(page, 'anna.gialli', 'dipendente123');
  109 |     const response = await page.goto(`${baseUrl}/payslips/1`);
  110 |     expect(response.status()).toBe(200);
  111 |   });
  112 | });
  113 | 
  114 | test.describe('HR Gestionale - Test SQL Injection (deve essere ASSENTE)', () => {
  115 |   test('login con payload SQLi non bypassa autenticazione', async ({ page }) => {
  116 |     await login(page, "' OR '1'='1", "' OR '1'='1");
  117 |     await expect(page).toHaveURL(/login/);
  118 |   });
  119 | 
  120 |   test('login con comment injection non bypassa autenticazione', async ({ page }) => {
  121 |     await login(page, "admin'--", "qualsiasi");
  122 |     await expect(page).toHaveURL(/login/);
  123 |   });
  124 | });
  125 | 
  126 | test.describe('HR Gestionale - CSRF Protection', () => {
  127 |   test('form di login contiene token CSRF', async ({ page }) => {
  128 |     await page.goto(`${baseUrl}/login`);
  129 |     const csrfToken = await page.locator('input[name="_csrf"]').count();
  130 |     expect(csrfToken).toBeGreaterThan(0);
  131 |   });
  132 | 
  133 |   test.describe('patch/idor - IDOR ATTIVO (CWE-639)', () => {
  134 |   test('IDOR: anna.gialli accede a busta paga di paolo.rossi', async ({ page }) => {
  135 |     await login(page, 'anna.gialli', 'dipendente123');
  136 |     // payslip 3 e 4 appartengono a paolo.rossi
  137 |     const response = await page.goto(`${baseUrl}/payslips/3`);
  138 |     expect(response.status()).toBe(200);
  139 |     const content = await page.textContent('body');
  140 |     expect(content).toContain('Paolo');
  141 |   });
  142 | 
  143 |   test('IDOR: anna.gialli accede a busta paga di giulia.blu', async ({ page }) => {
  144 |     await login(page, 'anna.gialli', 'dipendente123');
  145 |     // payslip 5 e 6 appartengono a giulia.blu
  146 |     const response = await page.goto(`${baseUrl}/payslips/5`);
  147 |     expect(response.status()).toBe(200);
  148 |     const content = await page.textContent('body');
  149 |     expect(content).toContain('Giulia');
  150 |   });
  151 | });
  152 | });
```