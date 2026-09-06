# Student Marketplace

Student Marketplace is a desktop-based e-commerce application built using **JavaFX** and **SQLite**. It allows university students to easily list, buy, and sell items, manage their listed products, track purchase histories, and view sale notifications within a campus environment.

---

## Features

- **User Authentication**: Student registration and login functionality.
- **Product Listing**: Sell items by providing details like title, description, and price.
- **Marketplace Feed**: Browse all available products with real-time search filtering.
- **Purchase System**: Buy products listed by other students (with built-in validation preventing self-purchases).
- **My Listed Products**: View and delete your own active listings.
- **My Purchases**: Access history of items you have bought.
- **Sales & Buyer Info**: View sales history along with buyer details for sold items.
- **Modern UI**: Styled with clean custom CSS inspired by modern flat and Instagram-style aesthetics.

---

## Tech Stack

- **Language**: Java 17+
- **GUI Framework**: JavaFX
- **Database**: SQLite (managed via JDBC / DatabaseHandler)
- **Build Tool**: Maven

---

## Project Structure

```text
src/
└── main/
    ├── java/
    │   └── com/
    │       └── marketplace1/
    │           ├── HelloApplication.java      # Main entry point
    │           ├── HelloController.java       # Primary controller & UI navigation
    │           ├── DashboardController.java   # Dashboard logic
    │           ├── RegisterController.java    # Registration logic
    │           ├── DatabaseHandler.java       # SQLite database operations
    │           └── Launcher.java              # Application launcher
    └── resources/
        ├── style.css                          # Custom UI styling
        ├── dashboard-view.fxml
        ├── hello-view.fxml
        └── register-view.fxml
