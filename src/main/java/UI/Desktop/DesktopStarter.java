package UI.Desktop;


import Database.DatabaseUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.logging.Logger;


public class DesktopStarter extends Application {

    private Parent root;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        root = FXMLLoader.load(DesktopStarter.class.getResource("/JavaFX/main.fxml"));
        stage.setFullScreen(false);
        stage.setResizable(false);
        stage.setTitle("Image Recognition App");
        stage.setScene(new Scene(root, 600, 600));
        stage.setIconified(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        Logger.getGlobal().info("APPLICATION STARTED...");
    }

    public static void restartApplication(Node node) throws IOException {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
        new DesktopStarter().start(new Stage());
    }
}
