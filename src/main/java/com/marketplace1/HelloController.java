package com.marketplace1;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.List;

public class HelloController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    protected void onLoginButtonClick() {
        String email = emailField.getText();
        String password = passwordField.getText();

        boolean isValidUser = DatabaseHandler.loginUser(email, password);

        if (isValidUser) {
            Stage stage = (Stage) emailField.getScene().getWindow();
            openDashboard(stage, email);
        } else {
            showAlert("Error", "Invalid Email or Password! Please try again.", AlertType.ERROR);
        }
    }

    private void openDashboard(Stage stage, String userEmail) {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(600, 450);

        Label welcomeLabel = new Label("Welcome to Marketplace Dashboard, " + userEmail + "!");
        welcomeLabel.setLayoutX(30);
        welcomeLabel.setLayoutY(30);
        welcomeLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

        Button viewProdBtn = new Button("View Products");
        viewProdBtn.setLayoutX(30); viewProdBtn.setLayoutY(80); viewProdBtn.setPrefWidth(180);
        viewProdBtn.setOnAction(e -> openViewProductsWindow(stage, userEmail));

        Button myProdBtn = new Button("My Listed Products");
        myProdBtn.setLayoutX(30); myProdBtn.setLayoutY(130); myProdBtn.setPrefWidth(180);
        myProdBtn.setOnAction(e -> openMyProductsWindow(stage, userEmail));

        Button addProdBtn = new Button("Add New Product");
        addProdBtn.setLayoutX(30); addProdBtn.setLayoutY(180); addProdBtn.setPrefWidth(180);
        addProdBtn.setOnAction(e -> openAddProductWindow(stage, userEmail));

        Button myOrdersBtn = new Button("My Purchases");
        myOrdersBtn.setLayoutX(30); myOrdersBtn.setLayoutY(230); myOrdersBtn.setPrefWidth(180);
        myOrdersBtn.setOnAction(e -> openMyPurchasesWindow(stage, userEmail));

        Button soldNotifBtn = new Button("Sold History & Buyers");
        soldNotifBtn.setLayoutX(30); soldNotifBtn.setLayoutY(280); soldNotifBtn.setPrefWidth(180);
        soldNotifBtn.setStyle("-fx-background-color: #e1306c; -fx-text-fill: white;");
        soldNotifBtn.setOnAction(e -> openSoldNotificationsWindow(stage, userEmail));

        Button logoutBtn = new Button("Logout");
        logoutBtn.setLayoutX(30); logoutBtn.setLayoutY(330); logoutBtn.setPrefWidth(180);
        logoutBtn.setOnAction(e -> openLoginWindow(stage));

        root.getChildren().addAll(welcomeLabel, viewProdBtn, myProdBtn, addProdBtn, myOrdersBtn, soldNotifBtn, logoutBtn);
        Scene scene = new Scene(root, 600, 450);

        try {
            scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        } catch (Exception ignored) {}

        stage.setTitle("Student Marketplace - Dashboard");
        stage.setScene(scene);
    }

    public void openLoginWindow(Stage stage) {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(600, 480);

        AnchorPane cardBox = new AnchorPane();
        cardBox.setLayoutX(140);
        cardBox.setLayoutY(30);
        cardBox.setPrefSize(320, 410);
        cardBox.getStyleClass().add("card-box");

        Label title = new Label("Student Marketplace");
        title.setLayoutX(40);
        title.setLayoutY(40);
        title.getStyleClass().add("header-title");

        Label subTitle = new Label("Sign in to access your account");
        subTitle.setLayoutX(65);
        subTitle.setLayoutY(78);
        subTitle.getStyleClass().add("sub-title");

        TextField emailF = new TextField();
        emailF.setPromptText("Phone number, username, or email");
        emailF.setLayoutX(30);
        emailF.setLayoutY(120);
        emailF.setPrefWidth(260);

        PasswordField passF = new PasswordField();
        passF.setPromptText("Password");
        passF.setLayoutX(30);
        passF.setLayoutY(165);
        passF.setPrefWidth(260);

        Button loginBtn = new Button("Log In");
        loginBtn.setLayoutX(30);
        loginBtn.setLayoutY(220);
        loginBtn.setPrefWidth(260);
        loginBtn.setPrefHeight(34);
        loginBtn.getStyleClass().add("btn-primary");

        loginBtn.setOnAction(e -> {
            boolean isValidUser = DatabaseHandler.loginUser(emailF.getText(), passF.getText());
            if (isValidUser) {
                openDashboard(stage, emailF.getText());
            } else {
                showAlert("Error", "Invalid Email or Password!", AlertType.ERROR);
            }
        });

        Button regGoBtn = new Button("Don't have an account? Sign up");
        regGoBtn.setLayoutX(30);
        regGoBtn.setLayoutY(340);
        regGoBtn.setPrefWidth(260);
        regGoBtn.getStyleClass().add("btn-secondary");

        regGoBtn.setOnAction(e -> openRegisterWindow(stage));

        cardBox.getChildren().addAll(title, subTitle, emailF, passF, loginBtn, regGoBtn);
        root.getChildren().add(cardBox);

        Scene scene = new Scene(root, 600, 480);

        try {
            scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("CSS file not found: " + e.getMessage());
        }

        stage.setTitle("Student Marketplace - Login");
        stage.setScene(scene);
    }

    private void openViewProductsWindow(Stage stage, String userEmail) {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(600, 400);

        Label title = new Label("Available Products in Marketplace");
        title.setLayoutX(170); title.setLayoutY(20);
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField searchField = new TextField();
        searchField.setPromptText("Search products by name...");
        searchField.setLayoutX(30); searchField.setLayoutY(60);
        searchField.setPrefWidth(540);

        ListView<String> listView = new ListView<>();
        listView.setLayoutX(30); listView.setLayoutY(100);
        listView.setPrefSize(540, 235);

        List<String> allProducts = DatabaseHandler.getAllProducts();
        listView.getItems().addAll(allProducts);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            listView.getItems().clear();
            for (String product : allProducts) {
                if (product.toLowerCase().contains(newValue.toLowerCase())) {
                    listView.getItems().add(product);
                }
            }
        });

        Button buyBtn = new Button("Buy Selected Product");
        buyBtn.setLayoutX(410); buyBtn.setLayoutY(345);
        buyBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");

        buyBtn.setOnAction(e -> {
            String selectedItem = listView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                if (selectedItem.contains("Seller: " + userEmail)) {
                    showAlert("Warning", "You cannot buy your own product!", AlertType.WARNING);
                    return;
                }

                String[] parts = selectedItem.split("\\|");
                String pTitle = parts[0].split(":")[1].trim();

                boolean success = DatabaseHandler.buyProduct(pTitle, userEmail);

                if (success) {
                    listView.getItems().remove(selectedItem);
                    showAlert("Success", "Product purchased successfully!", AlertType.INFORMATION);
                } else {
                    showAlert("Error", "Product is no longer available!", AlertType.ERROR);
                }
            } else {
                showAlert("Warning", "Please select a product to buy!", AlertType.WARNING);
            }
        });

        Button backBtn = new Button("Back to Dashboard");
        backBtn.setLayoutX(30); backBtn.setLayoutY(345);
        backBtn.setOnAction(e -> openDashboard(stage, userEmail));

        root.getChildren().addAll(title, searchField, listView, buyBtn, backBtn);
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("View Products");
        stage.setScene(scene);
    }

    private void openMyProductsWindow(Stage stage, String userEmail) {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(600, 400);

        Label title = new Label("My Listed Products");
        title.setLayoutX(220); title.setLayoutY(20);
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ListView<String> listView = new ListView<>();
        listView.setLayoutX(30); listView.setLayoutY(60);
        listView.setPrefSize(540, 250);

        List<String> allProducts = DatabaseHandler.getAllProducts();
        for (String product : allProducts) {
            if (product.contains(userEmail)) {
                listView.getItems().add(product);
            }
        }

        Button deleteBtn = new Button("Delete Selected Product");
        deleteBtn.setLayoutX(410); deleteBtn.setLayoutY(345);
        deleteBtn.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white;");

        deleteBtn.setOnAction(e -> {
            String selectedItem = listView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                String[] parts = selectedItem.split("\\|");
                String pTitle = parts[0].split(":")[1].trim();

                boolean isDeleted = DatabaseHandler.deleteProduct(pTitle, userEmail);
                if (isDeleted) {
                    listView.getItems().remove(selectedItem);
                    showAlert("Success", "Product permanently deleted from Database!", AlertType.INFORMATION);
                } else {
                    showAlert("Error", "Could not delete product from Database!", AlertType.ERROR);
                }
            } else {
                showAlert("Warning", "Please select a product to delete!", AlertType.WARNING);
            }
        });

        Button backBtn = new Button("Back to Dashboard");
        backBtn.setLayoutX(30); backBtn.setLayoutY(345);
        backBtn.setOnAction(e -> openDashboard(stage, userEmail));

        root.getChildren().addAll(title, listView, deleteBtn, backBtn);
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("My Products");
        stage.setScene(scene);
    }

    private void openMyPurchasesWindow(Stage stage, String userEmail) {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(600, 400);

        Label title = new Label("My Purchases");
        title.setLayoutX(240); title.setLayoutY(20);
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ListView<String> listView = new ListView<>();
        listView.setLayoutX(30); listView.setLayoutY(60);
        listView.setPrefSize(540, 270);

        List<String> purchases = DatabaseHandler.getMyPurchases(userEmail);
        listView.getItems().addAll(purchases);

        Button backBtn = new Button("Back to Dashboard");
        backBtn.setLayoutX(30); backBtn.setLayoutY(345);
        backBtn.setOnAction(e -> openDashboard(stage, userEmail));

        root.getChildren().addAll(title, listView, backBtn);
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("My Purchases");
        stage.setScene(scene);
    }

    private void openSoldNotificationsWindow(Stage stage, String userEmail) {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(600, 400);

        Label title = new Label("Sold Products & Buyer Info");
        title.setLayoutX(200); title.setLayoutY(20);
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ListView<String> listView = new ListView<>();
        listView.setLayoutX(30); listView.setLayoutY(60);
        listView.setPrefSize(540, 270);

        List<String> soldNotifs = DatabaseHandler.getSoldNotifications(userEmail);
        listView.getItems().addAll(soldNotifs);

        Button backBtn = new Button("Back to Dashboard");
        backBtn.setLayoutX(30); backBtn.setLayoutY(345);
        backBtn.setOnAction(e -> openDashboard(stage, userEmail));

        root.getChildren().addAll(title, listView, backBtn);
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Sold History");
        stage.setScene(scene);
    }

    private void openAddProductWindow(Stage stage, String userEmail) {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(600, 400);

        Label title = new Label("Add New Product");
        title.setLayoutX(230); title.setLayoutY(30);
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField nameF = new TextField(); nameF.setPromptText("Product Name"); nameF.setLayoutX(200); nameF.setLayoutY(90); nameF.setPrefWidth(200);
        TextField descF = new TextField(); descF.setPromptText("Description"); descF.setLayoutX(200); descF.setLayoutY(140); descF.setPrefWidth(200);
        TextField priceF = new TextField(); priceF.setPromptText("Price"); priceF.setLayoutX(200); priceF.setLayoutY(190); priceF.setPrefWidth(200);

        Button saveBtn = new Button("Save Product");
        saveBtn.setLayoutX(250); saveBtn.setLayoutY(250);

        saveBtn.setOnAction(e -> {
            try {
                String pName = nameF.getText();
                String pDesc = descF.getText();
                double pPrice = Double.parseDouble(priceF.getText());

                boolean success = DatabaseHandler.addProduct(pName, pDesc, pPrice, userEmail);
                if (success) {
                    showAlert("Success", "Product Added Successfully!", AlertType.INFORMATION);
                    openDashboard(stage, userEmail);
                } else {
                    showAlert("Error", "Failed to add product!", AlertType.ERROR);
                }
            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter a valid price!", AlertType.ERROR);
            }
        });

        Button backBtn = new Button("Back to Dashboard");
        backBtn.setLayoutX(30); backBtn.setLayoutY(30);
        backBtn.setOnAction(e -> openDashboard(stage, userEmail));

        root.getChildren().addAll(title, nameF, descF, priceF, saveBtn, backBtn);
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Add New Product");
        stage.setScene(scene);
    }

    private void openRegisterWindow(Stage stage) {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(600, 400);

        Label title = new Label("Student Registration");
        title.setLayoutX(220); title.setLayoutY(30);
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField nameF = new TextField(); nameF.setPromptText("Full Name"); nameF.setLayoutX(200); nameF.setLayoutY(80); nameF.setPrefWidth(200);
        TextField idF = new TextField(); idF.setPromptText("Student ID"); idF.setLayoutX(200); idF.setLayoutY(120); idF.setPrefWidth(200);
        TextField emailF = new TextField(); emailF.setPromptText("Email Address"); emailF.setLayoutX(200); emailF.setLayoutY(160); emailF.setPrefWidth(200);
        PasswordField passF = new PasswordField(); passF.setPromptText("Password"); passF.setLayoutX(200); passF.setLayoutY(200); passF.setPrefWidth(200);
        TextField roleF = new TextField(); roleF.setPromptText("Role (e.g., Student)"); roleF.setLayoutX(200); roleF.setLayoutY(240); roleF.setPrefWidth(200);

        Button regBtn = new Button("Register Now");
        regBtn.setLayoutX(250); regBtn.setLayoutY(290);

        regBtn.setOnAction(e -> {
            boolean success = DatabaseHandler.registerUser(nameF.getText(), idF.getText(), emailF.getText(), passF.getText(), roleF.getText());
            if (success) {
                showAlert("Success", "Registration Successful! Please login.", AlertType.INFORMATION);
                openLoginWindow(stage);
            } else {
                showAlert("Error", "Registration Failed! Try a different Email or ID.", AlertType.ERROR);
            }
        });

        Button backBtn = new Button("Back to Login");
        backBtn.setLayoutX(30); backBtn.setLayoutY(30);
        backBtn.setOnAction(e -> openLoginWindow(stage));

        root.getChildren().addAll(title, nameF, idF, emailF, passF, roleF, regBtn, backBtn);
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Student Registration");
        stage.setScene(scene);
    }

    @FXML
    protected void onRegisterButtonClick() {
        Stage stage = (Stage) emailField.getScene().getWindow();
        openRegisterWindow(stage);
    }

    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}