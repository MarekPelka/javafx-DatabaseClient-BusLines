package sample.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import sample.controller.MainApp;
import sample.model.Bus;

/**
 * Created by Marek on 2016-12-26.
 */
public class BusOverviewController {

    @FXML
    private TableView<Bus> tableBus;
    @FXML
    private TableColumn<Bus, String> columnBusPlate;

    @FXML
    private Label labelPlate;
    @FXML
    private Label labelCategory;
    @FXML
    private Label LabelModel;
    @FXML
    private Label labelSeats;
    @FXML
    private Label labelMileage;
    @FXML
    private Label labelSerialNumber;

    private MainApp mainApp;

    @FXML
    private void initialize() {
        columnBusPlate.setCellValueFactory(cellData -> cellData.getValue().licensePlateProperty());
        showBusDetails(null);
        tableBus.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showBusDetails(newValue));
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        tableBus.setItems(mainApp.getBusData());
    }

    public void showBusDetails(Bus bus)
    {
        if(bus != null) {
            labelPlate.setText(bus.getLicensePlate());
            labelCategory.setText(String.valueOf(bus.getClassRate()));
            LabelModel.setText(String.valueOf(bus.getModelName()));
            labelSeats.setText(String.valueOf(bus.getSeats()));
            labelMileage.setText(String.valueOf(bus.getMileage()));
            labelSerialNumber.setText(String.valueOf(bus.getSereialNumber()));

        }else
        {
            labelPlate.setText("");
            labelCategory.setText("");
            LabelModel.setText("");
            labelSeats.setText("");
            labelMileage.setText("");
            labelSerialNumber.setText("");
        }
    }
}
