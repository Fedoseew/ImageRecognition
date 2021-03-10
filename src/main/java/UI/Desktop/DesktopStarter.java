package UI.Desktop;


import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;


public class DesktopStarter extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/JavaFX/main.fxml"));
        primaryStage.setFullScreen(false);
        primaryStage.setMaxHeight(600);
        primaryStage.setMaxWidth(600);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Image Recognition App");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }
}
