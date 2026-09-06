module com.marketplace.studentmarketplace1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    // নিচের এই দুটি লাইন পরিবর্তন করতে হবে
    opens com.marketplace1 to javafx.fxml;
    exports com.marketplace1;
}