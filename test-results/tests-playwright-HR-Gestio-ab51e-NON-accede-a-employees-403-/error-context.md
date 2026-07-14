# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: tests\playwright.test.js >> HR Gestionale - Controllo Accessi MANAGER >> manager NON accede a /employees (403)
- Location: tests\playwright.test.js:60:7

# Error details

```
Error: expect(received).toBe(expected) // Object.is equality

Expected: 403
Received: 200
```

# Page snapshot

```yaml
- main [ref=e2]:
  - heading "Elenco Dipendenti" [level=1] [ref=e3]
  - navigation [ref=e4]:
    - link "Dashboard" [ref=e5] [cursor=pointer]:
      - /url: /dashboard
    - text: "|"
    - link "Nuovo Dipendente" [ref=e6] [cursor=pointer]:
      - /url: /employees/new
    - text: "|"
    - link "Logout" [ref=e7] [cursor=pointer]:
      - /url: /logout
  - table [ref=e8]:
    - rowgroup [ref=e9]:
      - row "Nome Ruolo Reparto Azioni" [ref=e10]:
        - columnheader "Nome" [ref=e11]
        - columnheader "Ruolo" [ref=e12]
        - columnheader "Reparto" [ref=e13]
        - columnheader "Azioni" [ref=e14]
    - rowgroup [ref=e15]:
      - row "Admin Sistema ADMIN Amministrazione Dettaglio | Modifica" [ref=e16]:
        - cell "Admin Sistema" [ref=e17]
        - cell "ADMIN" [ref=e18]
        - cell "Amministrazione" [ref=e19]
        - cell "Dettaglio | Modifica" [ref=e20]:
          - link "Dettaglio" [ref=e21] [cursor=pointer]:
            - /url: /employees/1
          - text: "|"
          - link "Modifica" [ref=e22] [cursor=pointer]:
            - /url: /employees/1/edit
      - row "Luca Bianchi MANAGER Sviluppo Dettaglio | Modifica" [ref=e23]:
        - cell "Luca Bianchi" [ref=e24]
        - cell "MANAGER" [ref=e25]
        - cell "Sviluppo" [ref=e26]
        - cell "Dettaglio | Modifica" [ref=e27]:
          - link "Dettaglio" [ref=e28] [cursor=pointer]:
            - /url: /employees/2
          - text: "|"
          - link "Modifica" [ref=e29] [cursor=pointer]:
            - /url: /employees/2/edit
      - row "Sara Verdi MANAGER Risorse Umane Dettaglio | Modifica" [ref=e30]:
        - cell "Sara Verdi" [ref=e31]
        - cell "MANAGER" [ref=e32]
        - cell "Risorse Umane" [ref=e33]
        - cell "Dettaglio | Modifica" [ref=e34]:
          - link "Dettaglio" [ref=e35] [cursor=pointer]:
            - /url: /employees/3
          - text: "|"
          - link "Modifica" [ref=e36] [cursor=pointer]:
            - /url: /employees/3/edit
      - row "Marco Neri MANAGER Vendite Dettaglio | Modifica" [ref=e37]:
        - cell "Marco Neri" [ref=e38]
        - cell "MANAGER" [ref=e39]
        - cell "Vendite" [ref=e40]
        - cell "Dettaglio | Modifica" [ref=e41]:
          - link "Dettaglio" [ref=e42] [cursor=pointer]:
            - /url: /employees/4
          - text: "|"
          - link "Modifica" [ref=e43] [cursor=pointer]:
            - /url: /employees/4/edit
      - row "Paolo Rossi EMPLOYEE Sviluppo Dettaglio | Modifica" [ref=e44]:
        - cell "Paolo Rossi" [ref=e45]
        - cell "EMPLOYEE" [ref=e46]
        - cell "Sviluppo" [ref=e47]
        - cell "Dettaglio | Modifica" [ref=e48]:
          - link "Dettaglio" [ref=e49] [cursor=pointer]:
            - /url: /employees/6
          - text: "|"
          - link "Modifica" [ref=e50] [cursor=pointer]:
            - /url: /employees/6/edit
      - row "Giulia Blu EMPLOYEE Vendite Dettaglio | Modifica" [ref=e51]:
        - cell "Giulia Blu" [ref=e52]
        - cell "EMPLOYEE" [ref=e53]
        - cell "Vendite" [ref=e54]
        - cell "Dettaglio | Modifica" [ref=e55]:
          - link "Dettaglio" [ref=e56] [cursor=pointer]:
            - /url: /employees/7
          - text: "|"
          - link "Modifica" [ref=e57] [cursor=pointer]:
            - /url: /employees/7/edit
      - row "Anna Gialli EMPLOYEE Sviluppo Dettaglio | Modifica" [ref=e58]:
        - cell "Anna Gialli" [ref=e59]
        - cell "EMPLOYEE" [ref=e60]
        - cell "Sviluppo" [ref=e61]
        - cell "Dettaglio | Modifica" [ref=e62]:
          - link "Dettaglio" [ref=e63] [cursor=pointer]:
            - /url: /employees/5
          - text: "|"
          - link "Modifica" [ref=e64] [cursor=pointer]:
            - /url: /employees/5/edit
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
> 63  |     expect(response.status()).toBe(403);
      |                               ^ Error: expect(received).toBe(expected) // Object.is equality
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
  101 |       expect(content).not.toContain('Paolo');
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
  132 | });
  133 | 
  134 | test.describe('patch/xss - XSS Stored ATTIVO (CWE-79)', () => {
  135 |   test('XSS: payload nel campo position viene eseguito come script', async ({ page }) => {
  136 |     await login(page, 'admin', 'admin123');
  137 | 
  138 |     let alertFired = false;
  139 |     page.on('dialog', async dialog => {
  140 |       alertFired = true;
  141 |       await dialog.dismiss();
  142 |     });
  143 | 
  144 |     await page.goto(`${baseUrl}/employees/5`);
  145 |     await page.waitForTimeout(1000);
  146 | 
  147 |     expect(alertFired).toBe(true);
  148 |   });
  149 | 
  150 |   test('XSS: admin può modificare position di un dipendente', async ({ page }) => {
  151 |     await login(page, 'admin', 'admin123');
  152 |     await page.goto(`${baseUrl}/employees/5`);
  153 |     expect(page.url()).toContain('/employees/5');
  154 |   });
  155 | });
```