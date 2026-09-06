package com.marketplace1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    private static final String URL = "jdbc:sqlite:marketplace.db";

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            String createUsersTable = "CREATE TABLE IF NOT EXISTS Users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "student_id TEXT UNIQUE NOT NULL, " +
                    "email TEXT UNIQUE NOT NULL, " +
                    "password TEXT NOT NULL, " +
                    "role TEXT NOT NULL" +
                    ");";
            stmt.execute(createUsersTable);

            String createProductsTable = "CREATE TABLE IF NOT EXISTS Products (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "seller_id INTEGER, " +
                    "title TEXT NOT NULL, " +
                    "description TEXT, " +
                    "category TEXT, " +
                    "price REAL NOT NULL, " +
                    "status TEXT DEFAULT 'Available', " +
                    "buyer_email TEXT, " +
                    "FOREIGN KEY(seller_id) REFERENCES Users(id)" +
                    ");";
            stmt.execute(createProductsTable);

            try {
                stmt.execute("ALTER TABLE Products ADD COLUMN buyer_email TEXT;");
            } catch (SQLException ignored) {}

            System.out.println("Database and tables initialized successfully!");

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static boolean registerUser(String name, String studentId, String email, String password, String role) {
        String insertQuery = "INSERT INTO Users(name, student_id, email, password, role) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

            pstmt.setString(1, name);
            pstmt.setString(2, studentId);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            pstmt.setString(5, role);

            pstmt.executeUpdate();
            System.out.println("Registration Successful!");
            return true;

        } catch (SQLException e) {
            System.out.println("Registration Error: " + e.getMessage());
            return false;
        }
    }

    public static boolean loginUser(String email, String password) {
        String loginQuery = "SELECT * FROM Users WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(loginQuery)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    if (storedPassword.equals(password)) {
                        System.out.println("Login Successful! Welcome " + rs.getString("name"));
                        return true;
                    }
                }
                System.out.println("Invalid email or password.");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Login Error: " + e.getMessage());
            return false;
        }
    }

    public static boolean addProduct(String title, String description, double price, String sellerEmail) {
        String getUserIdQuery = "SELECT id FROM Users WHERE email = ?";
        String insertProductQuery = "INSERT INTO Products(seller_id, title, description, price) VALUES(?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmtUser = conn.prepareStatement(getUserIdQuery)) {

            pstmtUser.setString(1, sellerEmail);
            try (ResultSet rs = pstmtUser.executeQuery()) {
                if (rs.next()) {
                    int sellerId = rs.getInt("id");

                    try (PreparedStatement pstmtProd = conn.prepareStatement(insertProductQuery)) {
                        pstmtProd.setInt(1, sellerId);
                        pstmtProd.setString(2, title);
                        pstmtProd.setString(3, description);
                        pstmtProd.setDouble(4, price);

                        pstmtProd.executeUpdate();
                        System.out.println("Product Added Successfully!");
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Add Product Error: " + e.getMessage());
        }
        return false;
    }

    public static List<String> getAllProducts() {
        List<String> productList = new ArrayList<>();
        String query = "SELECT p.title, p.description, p.price, u.email as seller FROM Products p JOIN Users u ON p.seller_id = u.id WHERE p.status = 'Available'";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String title = rs.getString("title");
                String desc = rs.getString("description");
                double price = rs.getDouble("price");
                String seller = rs.getString("seller");

                String productInfo = "Title: " + title + " | Desc: " + desc + " | Price: BDT " + price + " | Seller: " + seller;
                productList.add(productInfo);
            }

        } catch (SQLException e) {
            System.out.println("Fetch Products Error: " + e.getMessage());
        }
        return productList;
    }

    public static boolean deleteProduct(String title, String sellerEmail) {
        String deleteQuery = "DELETE FROM Products WHERE title = ? AND seller_id = (SELECT id FROM Users WHERE email = ?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {

            pstmt.setString(1, title);
            pstmt.setString(2, sellerEmail);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product Deleted Successfully!");
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Delete Product Error: " + e.getMessage());
        }
        return false;
    }

    // প্রোডাক্ট কিনার লজিক (প্যারামিটার অর্ডার ফিক্স করা হয়েছে)
    public static boolean buyProduct(String title, String buyerEmail) {
        String sql = "UPDATE Products SET status = 'Sold', buyer_email = ? WHERE title = ? AND status = 'Available'";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, buyerEmail); // ১ম ? = buyer_email
            pstmt.setString(2, title);      // ২য় ? = title
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Buy Product Error: " + e.getMessage());
            return false;
        }
    }

    public static List<String> getMyPurchases(String buyerEmail) {
        List<String> purchases = new ArrayList<>();
        String query = "SELECT p.title, p.price, u.email as seller FROM Products p JOIN Users u ON p.seller_id = u.id WHERE p.buyer_email = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, buyerEmail);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    purchases.add("Bought: " + rs.getString("title") + " | Price: BDT " + rs.getDouble("price") + " | Seller Contact: " + rs.getString("seller"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Fetch Purchases Error: " + e.getMessage());
        }
        return purchases;
    }

    public static List<String> getSoldNotifications(String sellerEmail) {
        List<String> notifications = new ArrayList<>();
        String query = "SELECT p.title, p.price, p.buyer_email FROM Products p JOIN Users u ON p.seller_id = u.id WHERE u.email = ? AND p.status = 'Sold'";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, sellerEmail);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    notifications.add("SOLD: " + rs.getString("title") + " | Price: BDT " + rs.getDouble("price") + " | Buyer Contact: " + rs.getString("buyer_email"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Fetch Notifications Error: " + e.getMessage());
        }
        return notifications;
    }
}