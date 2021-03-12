package UI.Desktop;


import Database.DatabaseUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class DesktopStarter extends Application {

    private static Parent root;

    static {

        try {

            root = FXMLLoader.load(DesktopStarter.class.getResource("/JavaFX/main.fxml"));

        } catch (IOException ioException) {

            ioException.printStackTrace();

        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        stage.setFullScreen(false);
        stage.setMaxHeight(800);
        stage.setMaxWidth(800);
        stage.setResizable(false);
        stage.setTitle("Image Recognition App");
        stage.setScene(new Scene(root, 600, 600));
        stage.show();
    }
}
