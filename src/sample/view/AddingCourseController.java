package sample.view;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import sample.controller.MainApp;
import sample.model.Bus;
import sample.model.Person;

/**
 * Created by Marek on 2016-12-28.
 */
public class AddingCourseController {

    @FXML
    private TableView<Person> tableWorkers;
    @FXML
    private TableColumn<Person, String> columnWorkers;

    @FXML
    private ChoiceBox choiceBoxDrivers;
    @FXML
    private ChoiceBox choiceBoxHostess;
    @FXML
    private ChoiceBox choiceBoxDrive;
    @FXML
    private ChoiceBox choiceBoxDate;


    private MainApp mainApp;

    @FXML
    private void initialize() {
        //columnWorkers.setCellValueFactory(cellData -> cellData.getValue().licensePlateProperty());
        //tableWorkers.getSelectionModel().selectedItemProperty().addListener(
                //(observable, oldValue, newValue) -> showBusDetails(newValue));
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        //tableWorkers.setItems();
    }

}
