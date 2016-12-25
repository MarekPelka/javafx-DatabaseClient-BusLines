package sample.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.controller.MainApp;
import sample.model.Drive;

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
    private ListView stopsOnRoute;
    @FXML
    private ListView possibleStops;

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

        //this.stopsOnRoute =
        this.labelTime.setText(String.valueOf(drive.getTime()));
        this.labelDistance.setText(String.valueOf(drive.getDistance()));
        this.labelPrice.setText(String.valueOf(drive.getPrice()));
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

    private boolean isInputValid() {
        String errorMessage = "";

        if (stopsOnRoute.getItems().isEmpty()) {
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

    public ListView getStopsOnRoute() {
        return stopsOnRoute;
    }

    public void setStopsOnRoute(ListView stopsOnRoute) {
        this.stopsOnRoute = stopsOnRoute;
    }

    public ListView getPossibleStops() {
        return possibleStops;
    }

    public void setPossibleStops(ListView possibleStops) {
        this.possibleStops = possibleStops;
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
