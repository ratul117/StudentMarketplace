package com.marketplace1;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField studentIdField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField roleField;

    @FXML
    protected void handleRegister() {
        String name = nameField.getText();
        String studentId = studentIdField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String role = roleField.getText();

        if (name.isEmpty() || studentId.isEmpty() || email.isEmpty() || password.isEmpty() || role.isEmpty()) {
            showAlert("Error", "Please fill in all fields!", AlertType.ERROR);
            return;
        }

        // ডেটাবেসের হ্যান্ডলার কল করে ইউজার সেভ করা হচ্ছে
        boolean success = DatabaseHandler.registerUser(name, studentId, email, password, role);

        if (success) {
            showAlert("Success", "Registration Successful! You can now log in.", AlertType.INFORMATION);
        } else {
            showAlert("Error", "Registration Failed! Email or Student ID might already exist.", AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}