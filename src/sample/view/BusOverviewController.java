package sample.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import sample.controller.MainApp;
import sample.model.Bus;
import sample.model.Drive;

import java.util.ArrayList;
import java.util.List;

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
    private Label labelMilage;
    @FXML
    private Label labelSereialNumber;

    private MainApp mainApp;

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
            labelCategory.setText(String.valueOf(bus.getCategory()));
            LabelModel.setText(String.valueOf(bus.getBusModelId()));
            labelSeats.setText(String.valueOf(bus.getSeats()));
            labelMilage.setText(String.valueOf(bus.getMileage()));
            labelSereialNumber.setText(String.valueOf(bus.getSereialNumber()));

        }else
        {
            labelPlate.setText("");
            labelCategory.setText("");
            LabelModel.setText("");
            labelSeats.setText("");
            labelMilage.setText("");
            labelSereialNumber.setText("");
        }

    }
}
