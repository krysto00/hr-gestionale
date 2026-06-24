import { test, expect } from '@playwright/test';

const baseUrl = 'http://localhost:8080';

async function login(page, username, password) {
  await page.goto(`${baseUrl}/login`);
  await page.fill('#username', username);
  await page.fill('#password', password);
  await page.click('button[type=submit]');
}

test.describe('HR Gestionale - Autenticazione', () => {
  test('login admin riuscito', async ({ page }) => {
    await login(page, 'admin', 'admin123');
    await expect(page).toHaveURL(/dashboard/);
    await expect(page.locator('text=Dashboard Admin')).toBeVisible();
  });

  test('login manager riuscito', async ({ page }) => {
    await login(page, 'luca.bianchi', 'manager123');
    await expect(page).toHaveURL(/dashboard/);
    await expect(page.locator('text=Dashboard Manager')).toBeVisible();
  });

  test('login employee riuscito', async ({ page }) => {
    await login(page, 'anna.gialli', 'dipendente123');
    await expect(page).toHaveURL(/dashboard/);
    await expect(page.locator('text=Dashboard Employee')).toBeVisible();
  });

  test('login con credenziali errate fallisce', async ({ page }) => {
    await login(page, 'admin', 'passwordsbagliata');
    await expect(page).toHaveURL(/login/);
  });
});

test.describe('HR Gestionale - Autorizzazione ADMIN', () => {
  test('admin accede a dipendenti', async ({ page }) => {
    await login(page, 'admin', 'admin123');
    await page.goto(`${baseUrl}/employees`);
    expect(page.url()).toContain('/employees');
    const status = await page.evaluate(() => document.title);
    expect(status).not.toContain('Whitelabel');
  });

  test('admin accede a reparti', async ({ page }) => {
    await login(page, 'admin', 'admin123');
    await page.goto(`${baseUrl}/departments`);
    expect(page.url()).toContain('/departments');
  });

  test('admin accede a tutte le buste paga', async ({ page }) => {
    await login(page, 'admin', 'admin123');
    await page.goto(`${baseUrl}/payslips`);
    expect(page.url()).toContain('/payslips');
  });
});

test.describe('HR Gestionale - Controllo Accessi MANAGER', () => {
  test('manager NON accede a /employees (403)', async ({ page }) => {
    await login(page, 'luca.bianchi', 'manager123');
    const response = await page.goto(`${baseUrl}/employees`);
    expect(response.status()).toBe(403);
  });

  test('manager NON accede a /departments (403)', async ({ page }) => {
    await login(page, 'luca.bianchi', 'manager123');
    const response = await page.goto(`${baseUrl}/departments`);
    expect(response.status()).toBe(403);
  });
});

test.describe('HR Gestionale - Controllo Accessi EMPLOYEE', () => {
  test('employee NON accede a /employees (403)', async ({ page }) => {
    await login(page, 'anna.gialli', 'dipendente123');
    const response = await page.goto(`${baseUrl}/employees`);
    expect(response.status()).toBe(403);
  });

  test('employee NON accede a /departments (403)', async ({ page }) => {
    await login(page, 'anna.gialli', 'dipendente123');
    const response = await page.goto(`${baseUrl}/departments`);
    expect(response.status()).toBe(403);
  });

  test('employee accede alle proprie buste paga', async ({ page }) => {
    await login(page, 'anna.gialli', 'dipendente123');
    await page.goto(`${baseUrl}/payslips`);
    expect(page.url()).toContain('/payslips');
  });
});

test.describe('HR Gestionale - Test IDOR (deve essere ASSENTE)', () => {
  test('employee NON accede a busta paga di altro dipendente (IDOR check)', async ({ page }) => {
    await login(page, 'anna.gialli', 'dipendente123');
    // Anna ha payslip 1 e 2. Payslip 3 e 4 sono di Paolo
    const response = await page.goto(`${baseUrl}/payslips/3`);
    // Deve essere bloccato: redirect o 403, MAI 200 con dati di Paolo
    if (response.status() === 200) {
      const content = await page.textContent('body');
      expect(content).not.toContain('Paolo');
    } else {
      expect([302, 403, 404]).toContain(response.status());
    }
  });

  test('employee accede alla propria busta paga', async ({ page }) => {
    await login(page, 'anna.gialli', 'dipendente123');
    const response = await page.goto(`${baseUrl}/payslips/1`);
    expect(response.status()).toBe(200);
  });
});

test.describe('HR Gestionale - Test SQL Injection (deve essere ASSENTE)', () => {
  test('login con payload SQLi non bypassa autenticazione', async ({ page }) => {
    await login(page, "' OR '1'='1", "' OR '1'='1");
    await expect(page).toHaveURL(/login/);
  });

  test('login con comment injection non bypassa autenticazione', async ({ page }) => {
    await login(page, "admin'--", "qualsiasi");
    await expect(page).toHaveURL(/login/);
  });
});

test.describe('HR Gestionale - CSRF Protection', () => {
  test('form di login contiene token CSRF', async ({ page }) => {
    await page.goto(`${baseUrl}/login`);
    const csrfToken = await page.locator('input[name="_csrf"]').count();
    expect(csrfToken).toBeGreaterThan(0);
  });

  test.describe('patch/sqli - SQL Injection ATTIVA (CWE-89)', () => {
  test('SQLi: bypass login con OR injection su username', async ({ page }) => {
    await page.goto(`${baseUrl}/login`);
    await page.fill('#username-legacy', "' OR '1'='1' --");
    await page.fill('#password-legacy', 'x');
    await page.locator('form[action="/login-legacy"] button').click();
    await expect(page).toHaveURL(/dashboard/);
  });

  test('SQLi: login legittimo funziona ancora sul form principale', async ({ page }) => {
    await login(page, 'admin', 'admin123');
    await expect(page).toHaveURL(/dashboard/);
  });

  test('SQLi: credenziali errate sul form principale falliscono', async ({ page }) => {
    await login(page, 'admin', 'sbagliata');
    await expect(page).toHaveURL(/login/);
  });
});
});