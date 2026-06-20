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
- **Appium 2.x**
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

## 🚀 Running the Tests

1. Start an Android emulator
2. Start Appium server
3. Run:

```
mvn clean test
```
---

## ⚠️ Known Issues in MyDemoApp (Important)

During testing, several **critical defects** were identified in the MyDemoApp product catalog.  
These issues are reproducible both manually and via automation and originate from the application itself.

---

### ❗ 1. The product **“Sauce Labs Backpack (green)”** crashes the app
- Reproducible manually and through automated tests
- Opening this product immediately closes the application
- This makes the product **untestable**
- The crash occurs consistently across emulator restarts and Appium sessions

---

### ❗ 2. Additional products may crash when scrolling further down the catalog
Although the catalog contains more than 4 products, only the first four are visible without scrolling.  
When scrolling down, some items exhibit defects such as:

- Missing or invalid image resources
- Missing color options
- Missing or incorrect layout IDs
- Null or invalid product data

Opening these items causes the application to close unexpectedly.

---

### Summary
These issues are **limitations of the MyDemoApp demo application** and not related to the automation framework.  
The test suite includes logic to avoid unstable products and ensure consistent, repeatable execution.

---

## 🔧 Future Improvements

- Add retry logic for flaky UI transitions
- Add device farm integration (BrowserStack / Sauce Labs)
- Add parallel execution
- Add API mocks for checkout flow
- Add visual regression tests

- **Integrate ExtentReports for HTML reporting**
    - Add Maven dependency
    - Implement `ExtentManager` and `ExtentTestListener`
    - Generate an HTML report after each test run
    - Attach screenshots on failure
    - Include device/emulator information in the report  
