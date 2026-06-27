# Mobile UI Automation – MyDemoApp (Android)

Automated UI test suite for the **Sauce Labs MyDemoApp** Android application.  
The project covers end‑to‑end guest checkout flows, cart behavior, product interactions, and navigation scenarios using **Appium**, **Java**, and **JUnit 5**.

---

## 📱 Application Under Test

The tests automate the official **MyDemoApp** Android application provided by Sauce Labs.

### Key characteristics:
- Simple e‑commerce demo app
- Product catalog with multiple items (only the first 4 are initially visible)
- Product details page with color selection, quantity selector, and Add to Cart
- Cart, Login, Checkout, Payment, Review Order, and Order Complete screens

---

## 🧪 Test Scope

The suite includes:

- Guest checkout (single and multiple products)
- Cart persistence after login
- Adding/removing items
- Product details validation
- Navigation via menu and navbar
- Price calculations and totals

---

## 🛠️ Technologies Used

- **Java 17**
- **Maven**
- **Appium 3.x**
- **JUnit 5**
- **Selenium WebDriver**
- **Page Object Model (POM)**
- **ExtentReports** for reporting

---

## 📂 Project Structure (Maven)

```
src/
│
├── main/java/
│   ├── config/                 # Reads configuration values (device, platform, app paths)
│   ├── drivers/                # Appium driver creation and lifecycle management
│   ├── factories/              # Test data generators (Checkout, Payment)
│   ├── models/                 # Data models used across tests (POJOs)
│   ├── pages/                  # Page Objects (Catalog, ProductDetails, Cart, etc.)
│   └── utils/                  # Helpers (scrolling, price parsing, product selection)
│
└── test/java/tests/            # Test classes (Login, Checkout, Cart, Guest flows)
```

---

## 🧩 Page Object Model

Each screen in the application is represented by a dedicated Page Object:

- CatalogPage
- ProductDetailsPage
- CartPage
- LoginPage
- CheckoutPage
- PaymentPage
- ReviewOrderPage
- OrderCompletePage
- MenuComponent
- NavBar

All interactions follow the Page Object Model best practices:

- Explicit waits for stable synchronization
- Stable locators (resource-id, accessibility-id, or XPath only when necessary)
- Scroll gestures using `mobile: scrollGesture`
- Index‑based product selection for consistent behavior across devices

---

## 📊 Reporting

The automation framework includes full HTML reporting using **ExtentReports**.

### Features:
- Automatic HTML report generation after each test run
- Detailed logs for each test (steps, status, duration)
- Screenshot capture on failure
- Category grouping using JUnit 5 @Tag annotations
- Thread-safe reporting via a custom JUnit 5 extension (ExtentReportExtension)

The report includes:
- Dashboard with execution statistics
- Passed / Failed / Skipped test lists
- Stack traces for failures
- Embedded screenshots
- Category-based filtering (smoke, regression, validation)

---

## 🚀 Running the Tests

1. Start an Android emulator
2. Start Appium server
3. Run:

```
mvn clean test
```
---

## ⚠️ Known Issues in MyDemoApp (Important) 

<p>During testing, two reproducible crashes were identified in the Product Catalog screen.
These issues originate from the application itself and are not related to the automation framework.</p>
---

### ❗ 1. The product at index 1 (e.g., “Sauce Labs Backpack (green)”) crashes the app
- Caused by an intentionally introduced null value in the app’s internal meta[] array
- Tapping this product triggers a NullPointerException
- The product cannot be opened and is untestable

---

### ❗ 2. Products after scrolling may also crash (index > 5)
The catalog contains more items than the hardcoded meta[] array (size 6).
When tapping a product with index greater than 5, the app attempts to access an invalid array position, resulting in:
- ArrayIndexOutOfBoundsException
- Immediate application closure

---

### 🔍 Logcat Evidence
Both crashes are clearly visible in **Logcat**, showing stack traces such as:

- `NullPointerException` when opening the product at index **1**
- `ArrayIndexOutOfBoundsException` when opening products with index **> 5** after scrolling

These logs confirm that the issues originate from the hardcoded `meta[]` array inside `ProductCatalogFragment`.

---

### Summary

<p>These crashes are caused by the hardcoded meta[] array inside ProductCatalogFragment and are part of the demo behavior of MyDemoApp.
The automation suite avoids unstable product indices to ensure consistent, repeatable execution.</p>
---

## 🔧 Future Improvements

- Add retry logic for flaky UI transitions
- Add device farm integration (BrowserStack / Sauce Labs)
- Add parallel execution
- Add API mocks for checkout flow
- Add visual regression tests

