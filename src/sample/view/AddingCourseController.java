package sample.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.DateTimeStringConverter;
import sample.controller.MainApp;
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
    private ChoiceBox<String> choiceBoxDrivers;
    @FXML 
    private Button addDriverBtn;
    @FXML
    private ChoiceBox<String> choiceBoxHostess;
    @FXML 
    private Button addHostessBtn;
    @FXML
    private ChoiceBox<String> choiceBoxDrive;
    @FXML
    private ChoiceBox choiceBoxDate;
    
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField timePicker;

    private MainApp mainApp;

    @FXML
    private void initialize() {
    	addDriverBtn.setOnAction(new EventHandler<ActionEvent>() {
    	    @Override public void handle(ActionEvent e) {
    	    	 ObservableList<Person> observableList = tableWorkers.getItems();
    	    	 observableList.addAll(mainApp.getFreeDrivers().get(choiceBoxDrivers.getSelectionModel().getSelectedIndex()));
    	    	tableWorkers.setItems(observableList);
    	    }
    	});
    	
    	addHostessBtn.setOnAction(new EventHandler<ActionEvent>() {
    	    @Override public void handle(ActionEvent e) {
    	    	 ObservableList<Person> observableList = tableWorkers.getItems();
    	    	 observableList.addAll(mainApp.getFreeHostess().get(choiceBoxHostess.getSelectionModel().getSelectedIndex()));
    	    	tableWorkers.setItems(observableList);
    	    }
    	});
    	
    	columnWorkers.setCellValueFactory(cellData -> new SimpleStringProperty( cellData.getValue().getName() + " "
    			+ cellData.getValue().getSurname()));
        //columnWorkers.setCellValueFactory(cellData -> cellData.getValue().licensePlateProperty());
        //tableWorkers.getSelectionModel().selectedItemProperty().addListener(
                //(observable, oldValue, newValue) -> showBusDetails(newValue));
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        
        choiceBoxDrivers.setItems(mainApp.getFreeDrivers().stream().map(d -> d.getName()+ " " + d.getSurname())
    			.collect(Collectors.toCollection(FXCollections::observableArrayList)));
    	
    	choiceBoxHostess.setItems(mainApp.getFreeHostess().stream().map(h -> h.getName()+ " " + h.getSurname())
    			.collect(Collectors.toCollection(FXCollections::observableArrayList)));
    	
    	choiceBoxDrive.setItems(mainApp.getDriveData().stream().map(d -> d.getFrom()+ " ->" + d.getTo())
    			.collect(Collectors.toCollection(FXCollections::observableArrayList)));
    	
    	SimpleDateFormat format = new SimpleDateFormat("HH:mm");
    	try {
			timePicker.setTextFormatter(new TextFormatter<>(new DateTimeStringConverter(format), format.parse("00:00")));
		} catch (ParseException e) {
			mainApp.exceptionDialog(e);
		}
    }

}
