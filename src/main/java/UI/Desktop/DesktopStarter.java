package UI.Desktop;

import Configurations.ApplicationConfiguration;
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


    public static void main(String[] args) {
        launch(args);
    }

    public static void restartApplication(Node node) throws IOException {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
        new DesktopStarter().start(new Stage());
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(DesktopStarter.class.getResource("/JavaFX/main.fxml"));
        double windowHeight = ApplicationConfiguration.getApplicationWindowHeight();
        double windowWidth = ApplicationConfiguration.getApplicationWindowWidth();
        stage.setFullScreen(false);
        stage.setResizable(false);
        stage.setTitle("Image Recognition App");
        stage.setScene(new Scene(root, windowWidth, windowHeight));
        stage.setIconified(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        Logger.getGlobal().info("APPLICATION STARTED...");
    }
}
