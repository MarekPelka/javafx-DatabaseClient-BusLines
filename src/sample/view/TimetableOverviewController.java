package sample.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.controller.MainApp;
import sample.model.IntermediateDrive;

/**
 * Created by Marek on 2017-01-02.
 */
public class TimetableOverviewController {

    @FXML
    private TableView<IntermediateDrive> tableTimetable;
    @FXML
    private TableColumn<IntermediateDrive, String> columnTimetable;

    @FXML
    private TableColumn<IntermediateDrive, String> columnMon;
    @FXML
    private TableColumn<IntermediateDrive, String> columnTue;
    @FXML
    private TableColumn<IntermediateDrive, String> columnWen;
    @FXML
    private TableColumn<IntermediateDrive, String> columnThu;
    @FXML
    private TableColumn<IntermediateDrive, String> columnFri;
    @FXML
    private TableColumn<IntermediateDrive, String> columnSat;
    @FXML
    private TableColumn<IntermediateDrive, String> columnSun;

    private MainApp mainApp;

    @FXML
    private void initialize() {

    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
