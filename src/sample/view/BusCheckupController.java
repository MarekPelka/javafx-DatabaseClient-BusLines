package sample.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import sample.controller.MainApp;
import sample.model.Bus;

/**
 * Created by Marek on 2016-12-28.
 */
public class BusCheckupController {

    @FXML
    private TableView<Bus> tableServices;
    @FXML
    private TableColumn<Bus, String> columnServices;

    @FXML
    private ChoiceBox choiceBoxBus;
    @FXML
    private TextField textFieldAge;
    @FXML
    private TextField textFieldMileage;
    @FXML
    private ChoiceBox choiceBoxService;

    private MainApp mainApp;

    @FXML
    private void initialize() {
        //columnServices.setCellValueFactory(cellData -> cellData.getValue().licensePlateProperty());
        //tableServices.getSelectionModel().selectedItemProperty().addListener(
                //(observable, oldValue, newValue) -> showBusDetails(newValue));
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        //tableServices.setItems(mainApp.getServiceData());
    }

}
