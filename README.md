# qacodes-selenium-java

A reference Selenium automation project demonstrating real-world patterns: Page Object Model, parallel TestNG execution, data-driven tests, and HTML reporting — targeting the public Sauce Demo site.

---

## Overview

| Stack layer | Choice |
|-------------|--------|
| Language | Java 17 |
| Build | Maven 3.8+ |
| Browser automation | Selenium 4 |
| Driver management | WebDriverManager 5 |
| Test framework | TestNG 7 (parallel methods) |
| Reporting | ExtentReports 5 (HTML) |
| Target app | [Sauce Demo](https://www.saucedemo.com) |

Key patterns applied:
- **Page Object Model** — each page is a class with action-level methods; assertions stay in tests
- **ThreadLocal WebDriver** — safe parallel execution with no shared state
- **DataProvider** — data-driven login and checkout tests
- **Explicit waits only** — implicit wait is 0; all waiting via `WaitUtils`/`WebDriverWait`
- **Headless via CI env var** — no code change needed between local and CI runs

---

## Prerequisites

- JDK 17 (`java -version`)
- Maven 3.8+ (`mvn -version`)
- Git
- Google Chrome (latest stable)

---

## Folder structure

```
qacodes-selenium-java/
├── pom.xml
├── testng.xml
├── .github/workflows/selenium.yml
└── src/
    ├── main/
    │   ├── java/
    │   │   ├── base/
    │   │   │   ├── BaseTest.java          # ThreadLocal driver + @Before/@After
    │   │   │   └── BrowserFactory.java    # WebDriver creation + headless options
    │   │   ├── pages/
    │   │   │   ├── BasePage.java          # Shared driver + WaitUtils
    │   │   │   ├── LoginPage.java
    │   │   │   ├── ProductPage.java
    │   │   │   └── CheckoutPage.java
    │   │   ├── utils/
    │   │   │   ├── ConfigReader.java      # Singleton; env var > sys prop > file
    │   │   │   ├── WaitUtils.java         # Explicit-wait helpers
    │   │   │   └── ScreenshotUtil.java    # PNG capture helpers
    │   │   └── listeners/
    │   │       └── ExtentReportListener.java
    │   └── resources/
    │       └── config.properties.example
    └── test/
        └── java/
            ├── tests/
            │   ├── LoginTest.java
            │   └── CheckoutTest.java
            └── data/
                └── TestData.java          # DataProvider (loginData, checkoutData)
```

---

## Setup & run

```bash
# 1. Clone
git clone https://github.com/qacodes-dev/qacodes-selenium-java.git
cd qacodes-selenium-java

# 2. (Optional) create config.properties to override defaults
cp src/main/resources/config.properties.example src/main/resources/config.properties
# edit config.properties as needed

# 3. Run all tests
mvn clean test
```

A fresh clone runs without copying the config file — `ConfigReader` falls back to `config.properties.example` automatically.

---

## Environment / config.properties keys

| Key | Default | Env var override | System property |
|-----|---------|-----------------|-----------------|
| `base.url` | `https://www.saucedemo.com` | `BASE_URL` | `-Dbase.url` |
| `browser` | `chrome` | `BROWSER` | `-Dbrowser` |
| `test.username` | `standard_user` | `TEST_USERNAME` | `-Dtest.username` |
| `test.password` | `secret_sauce` | `TEST_PASSWORD` | `-Dtest.password` |
| `implicit.wait.seconds` | `0` | — | — |

Resolution order: **system property > env var > config file > default**

---

## Commands

```bash
# Run all tests
mvn clean test

# Run smoke group only
mvn clean test -Dgroups=smoke

# Run a specific test class
mvn clean test -Dtest=LoginTest

# Run headless locally
mvn clean test -Dheadless=true

# Run with a different browser
mvn clean test -Dbrowser=firefox
```

---

## CI/CD

The GitHub Actions workflow (`.github/workflows/selenium.yml`) runs on every push to `main` and on pull requests. It:

1. Checks out the code and sets up JDK 17 (Temurin)
2. Runs `mvn clean test -Dbrowser=chrome` — Chrome is pre-installed on `ubuntu-latest`
3. Sets `CI=true` so `BrowserFactory` enables `--headless=new` automatically
4. Uploads `test-output/` (Extent HTML report + screenshots) as a build artifact retained for 14 days

Credentials are read from GitHub Secrets (`BASE_URL`, `TEST_USERNAME`, `TEST_PASSWORD`) with sane defaults so the workflow passes without any secrets configured.

---

## Common issues

**`SessionNotCreatedException` — Chrome version mismatch**
WebDriverManager downloads the matching ChromeDriver automatically. If you see a version error, run `mvn clean test` again; WDM caches drivers in `~/.cache/selenium/`.

**Tests pass individually but fail in parallel**
Check that your page object does not hold a static `WebDriver` reference. All driver access must go through `BaseTest.getDriver()` which reads from `ThreadLocal`.

**`FileNotFoundException` for config.properties**
The file is gitignored intentionally. Either copy `.example` → `config.properties`, or pass credentials as system properties (`-Dtest.username=...`).

**Headless Chrome crashes with `--no-sandbox` errors in Docker/CI**
`BrowserFactory` already adds `--no-sandbox`, `--disable-dev-shm-usage`, and `--disable-gpu` when `CI=true`. Ensure the `CI` env var is set.

**`NoSuchElementException` despite correct selector**
Implicit wait is 0 by design. Use `WaitUtils.waitForVisible(By)` before interacting with any element that may not be immediately present.

---

## Learn more

Full project walkthrough and architecture notes:
https://qa.codes/practice/project-samples/selenium-java

---

## License

MIT
