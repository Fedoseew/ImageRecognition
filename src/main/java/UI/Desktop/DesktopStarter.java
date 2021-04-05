package UI.Desktop;

import Configurations.ApplicationConfiguration;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.logging.Logger;


public class DesktopStarter extends Application {

    private double xOffset;
    private double yOffset;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        Parent root = FXMLLoader.load(DesktopStarter.class.getResource("/JavaFX/main.fxml"));

        double windowHeight = ApplicationConfiguration.getApplicationWindowHeight();
        double windowWidth = ApplicationConfiguration.getApplicationWindowWidth();

        Scene scene = new Scene(root, windowWidth, windowHeight);

        stage.setFullScreen(false);
        stage.setResizable(false);
        stage.setTitle("Image Recognition App");
        stage.setScene(scene);
        stage.setIconified(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);

        scene.setOnMousePressed(event -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });

        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });

        stage.show();

        Logger.getGlobal().info("APPLICATION STARTED...");
    }

    public static void restartApplication(Node node) throws IOException {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
        Logger.getGlobal().info("RESTARTING...");
        new DesktopStarter().start(new Stage());
    }
}
