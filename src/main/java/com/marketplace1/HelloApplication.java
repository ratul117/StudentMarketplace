package com.marketplace1;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        DatabaseHandler.initializeDatabase();

        HelloController controller = new HelloController();

        try {
            java.lang.reflect.Method method = HelloController.class.getDeclaredMethod("openLoginWindow", Stage.class);
            method.setAccessible(true);
            method.invoke(controller, stage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}