package sample.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.controller.MainApp;
import sample.model.Drive;
import sample.model.IntermediateDrive;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marek on 2016-12-25.
 */
public class PlanDriveController {

    @FXML
    private TableView<Drive> tableDrive;
    @FXML
    private TableColumn<Drive, String> columnFrom;
    @FXML
    private TableColumn<Drive, String> columnTo;

    @FXML
    private TableView<IntermediateDrive> tableStops;
    @FXML
    private TableColumn<IntermediateDrive, String> columnStops;
    @FXML
    private TableView<IntermediateDrive> tablePossibleStops;
    @FXML
    private TableColumn<IntermediateDrive, String> columnPossibleStops;

    @FXML
    private Text driveStart;
    @FXML
    private Label labelTime;
    @FXML
    private Label labelDistance;
    @FXML
    private Label labelPrice;

    private MainApp mainApp;
    private Stage dialogStage;
    private Drive drive;
    private boolean okClicked = false;
    private List<IntermediateDrive> driveInProgres;

    @FXML
    private void initialize() {

        columnFrom.setCellValueFactory(cellData -> cellData.getValue().fromProperty());
        columnTo.setCellValueFactory(cellData -> cellData.getValue().toProperty());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        tableDrive.setItems(mainApp.getDriveData());


    }

    public void setDrive(Drive drive)
    {
        this.drive = drive;
        tableDrive.getSelectionModel().select(drive);


        if(drive.getListOfIntermediateDrive() != null)
        {
            this.labelTime.setText(String.valueOf(drive.getTime()));
            this.labelDistance.setText(String.valueOf(drive.getDistance()));
            this.labelPrice.setText(String.valueOf(drive.getPrice()));

            this.driveStart.setText(drive.getFrom().replace(" ", ""));
            this.driveStart.setFill(Color.DARKORANGE);

            tableStops.setItems(FXCollections.observableArrayList (drive.getListOfIntermediateDrive()));
            columnStops.setCellValueFactory(cellData -> cellData.getValue().cityToProperty());

            tablePossibleStops.setItems(mainApp.getIntermediateDrivesFromData(tableStops.getItems().get(tableStops.getItems().size() - 1).getCityTo()));
            columnPossibleStops.setCellValueFactory(cellData -> cellData.getValue().cityToProperty());
        }
        else
        {
            this.labelTime.setText("0");
            this.labelDistance.setText("0");
            this.labelPrice.setText("0");
            this.driveStart.setText("");

            tablePossibleStops.setItems(mainApp.getIntermediateDrivesData());
            columnPossibleStops.setCellValueFactory(cellData -> cellData.getValue().cityFromProperty());
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            drive.setFrom(null);
            drive.setTo(null);
            drive.setDistance(0);
            drive.setTime(0);
            drive.setPrice(0);

            okClicked = true;
            mainApp.showDriveOverview();
        }
    }

    @FXML
    private void addStop() {

//        driveInProgres.add((IntermediateDrive) possibleStops.getSelectionModel().getSelectedItem());
//        List<String> intermediateStopsNames = new ArrayList<>();
//        driveInProgres.forEach(d -> intermediateStopsNames.add(d.getCityFrom()));
//        ObservableList<String> items = FXCollections.observableArrayList (intermediateStopsNames);
//        stopsOnRoute.setItems(items);
//        List<String> intermediateNames = new ArrayList<>();
//        mainApp.getIntermediateDrivesFromData(driveInProgres.get(driveInProgres.size() - 1).getCityFrom());
//        items = FXCollections.observableArrayList (intermediateNames);
//        possibleStops.setItems(items);
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (tableStops.getItems().isEmpty()) {
            errorMessage += "No valid stops!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Stops");
            alert.setHeaderText("Please correct route");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

    @FXML
    private void frezzeSelection() {
        tableDrive.getSelectionModel().select(drive);
    }

    @FXML
    private void handleCancel() {
        mainApp.showDriveOverview();
    }

    public Label getLabelPrice() {
        return labelPrice;
    }

    public void setLabelPrice(Label labelPrice) {
        this.labelPrice = labelPrice;
    }

    public Label getLabelTime() {
        return labelTime;
    }

    public void setLabelTime(Label labelTime) {
        this.labelTime = labelTime;
    }

    public Label getLabelDistance() {
        return labelDistance;
    }

    public void setLabelDistance(Label labelDistance) {
        this.labelDistance = labelDistance;
    }

    public TableView<Drive> getTableDrive() {
        return tableDrive;
    }

    public void setTableDrive(TableView<Drive> tableDrive) {
        this.tableDrive = tableDrive;
    }

    public TableColumn<Drive, String> getColumnFrom() {
        return columnFrom;
    }

    public void setColumnFrom(TableColumn<Drive, String> columnFrom) {
        this.columnFrom = columnFrom;
    }

    public TableColumn<Drive, String> getColumnTo() {
        return columnTo;
    }

    public void setColumnTo(TableColumn<Drive, String> columnTo) {
        this.columnTo = columnTo;
    }
}
