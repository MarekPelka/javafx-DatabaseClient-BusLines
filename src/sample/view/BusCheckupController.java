package sample.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import sample.controller.MainApp;
import sample.model.Bus;
import sample.model.Drive;
import sample.model.Person;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Created by Marek on 2016-12-28.
 */
public class BusCheckupController {

    @FXML
    private TableView<Bus> tableServices;
    @FXML
    private TableColumn<Bus, String> columnServices;

    @FXML
    private ChoiceBox<Bus> choiceBoxBus;
    @FXML
    private TextField textFieldAge;
    @FXML
    private TextField textFieldMileage;
    @FXML
    private ChoiceBox<String> choiceBoxService;

    private MainApp mainApp;

    @FXML
    private void initialize() {
        choiceBoxBus.selectionModelProperty().addListener(
                (observable, oldValue, newValue) -> showBusDetails(newValue)
        );
    }

    private void showBusDetails(SingleSelectionModel<Bus> bus)
    {
        if(bus != null)
        {
            Date date = new Date(System.currentTimeMillis() - bus.getSelectedItem().getDateOfBuy().getTime());
            textFieldAge.setText(new SimpleDateFormat("yyyy").format(date));
            textFieldMileage.setText(String.valueOf(bus.getSelectedItem().getMileage()));
        }
        else
        {
            textFieldAge.setText("");
            textFieldMileage.setText("");
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        choiceBoxBus.setItems(mainApp.getBusData());

        choiceBoxService.setItems(mainApp.getService().stream().map(s -> s.getOperation())
                .collect(Collectors.toCollection(FXCollections::observableArrayList)));
    }


    @FXML
    private void selectionChange() {

    }


    @FXML
    private void busDetails() {
        Bus selectedBus = choiceBoxBus.getSelectionModel().getSelectedItem();
        if (selectedBus != null) {
            Date date = new Date(System.currentTimeMillis() - selectedBus.getDateOfBuy().getTime());
            textFieldAge.setText(String.valueOf(Integer.parseInt(new SimpleDateFormat("yyyy").format(date)) - 1970) + " years");
            textFieldMileage.setText(String.valueOf(selectedBus.getMileage()));
        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Bus Selected");
            alert.setContentText("Please select a bus in the drop down menu.");

            alert.showAndWait();
        }
    }
}
