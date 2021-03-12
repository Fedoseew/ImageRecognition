package UI.Desktop;

import Configurations.ApplicationConfiguration;
import Database.DatabaseUtils;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Logger;


public class MainController {

    @FXML
    AnchorPane anchorPane;

    @FXML
    GridPane grid;

    @FXML
    private Button go;

    @FXML
    void initialize() {

        createGrid();

        try {

            DatabaseUtils.getConnection();
            Logger.getGlobal().info("Connected to database");

        } catch (SQLException exception) {

            Logger.getGlobal().warning(exception.getLocalizedMessage());
            exception.printStackTrace();

        }
    }

    private void createGrid() {

        grid.setCursor(Cursor.HAND);

        int sizeOfGrid = ApplicationConfiguration.getSizeOfGrid();

        for (int i = 1; i <= sizeOfGrid; i++) {

            for (int j = 1; j <= sizeOfGrid; j++) {

                grid.add(createCell(), i, j);

            }
        }
    }

    private ListView<?> createCell() {

        ListView<?> cell = new ListView<>();

        cell.setCursor(Cursor.HAND);
        cell.setStyle("-fx-background-color: inherit");
        cell.setStyle("-fx-border-color: black");

        cell.hoverProperty().addListener((ObservableValue<? extends Boolean> observable,
                                          Boolean oldValue, Boolean newValue) -> {
            if (newValue) {

                cell.setStyle("-fx-background-color: #797979");

            } else {

                cell.setStyle("-fx-background-color: inherit");
                cell.setStyle("-fx-border-color: black");

            }
        });

        return cell;
    }
}


