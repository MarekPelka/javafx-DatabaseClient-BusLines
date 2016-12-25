package sample.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import sample.controller.MainApp;
import sample.model.Drive;

/**
 * Created by Marek on 2016-12-25.
 */
public class DriveOverviewController {
    @FXML
    private TableView<Drive> tableDrive;
    @FXML
    private TableColumn<Drive, String> columnFrom;
    @FXML
    private TableColumn<Drive, String> columnTo;

    @FXML
    private Label labelFrom;
    @FXML
    private Label labelTo;
    @FXML
    private Label labelTime;
    @FXML
    private Label labelDistance;
    @FXML
    private Label labelPrice;
    @FXML
    private ListView<String> listIntermediateDrive;

    private MainApp mainApp;

    public DriveOverviewController() {}

    public void initialize()
    {
        columnFrom.setCellValueFactory(cellData -> cellData.getValue().fromProperty());
        columnTo.setCellValueFactory(cellData -> cellData.getValue().toProperty());

        showDriveDetails(null);

        tableDrive.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showDriveDetails(newValue));
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        tableDrive.setItems(mainApp.getDriveData());
    }

    public void showDriveDetails(Drive drive)
    {
        if(drive != null) {
            labelFrom.setText(drive.getFrom());
            labelTo.setText(drive.getTo());
            labelTime.setText(String.valueOf(drive.getTime()));
            labelDistance.setText(String.valueOf(drive.getDistance()));
            labelPrice.setText(String.valueOf(drive.getPrice()));
        }else
        {
            labelFrom.setText("");
            labelTo.setText("");
            labelTime.setText("");
            labelDistance.setText("");
            labelPrice.setText("");
        }

    }

    @FXML
    private void handleDeleteDrive() {
        int selectedIndex = tableDrive.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            tableDrive.getItems().remove(selectedIndex);
        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Drive Selected");
            alert.setContentText("Please select a drive in the table.");

            alert.showAndWait();
        }
    }

    @FXML
    private void handleNewDrive() {
        Drive tempDrive = new Drive();
        boolean okClicked = mainApp.showDriveEditDialog(tempDrive);
        if (okClicked) {
            mainApp.getDriveData().add(tempDrive);
        }
    }

    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected person.
     */
    @FXML
    private void handleEditDrive() {
        Drive selectedDrive = tableDrive.getSelectionModel().getSelectedItem();
        if (selectedDrive != null) {
            boolean okClicked = mainApp.showDriveEditDialog(selectedDrive);
            if (okClicked) {
                showDriveDetails(selectedDrive);
            }

        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Drive Selected");
            alert.setContentText("Please select a drive in the table.");

            alert.showAndWait();
        }
    }
}
