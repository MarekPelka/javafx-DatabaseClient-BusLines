package sample.view;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import sample.controller.MainApp;
import sample.model.Bus;
import sample.model.Service;

/**
 * Created by Marek on 2016-12-28.
 */
public class BusCheckupController {

    @FXML
    private TableView<Service> tableServices;
    @FXML
    private TableColumn<Service, String> columnServices;

    @FXML
    private TableView<Service> tableDoneServices;
    @FXML
    private TableColumn<Service, String> columnDoneServices;
    
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
    	columnServices.setCellValueFactory(cellData ->  new SimpleStringProperty(cellData.getValue().getOperation() + 
    			" (after:" + cellData.getValue().getDoneServiceOptionalDate() + " days )" + 
    			" (after:" + cellData.getValue().getDoneMileageOptionalValue() + " km )"));
    	columnDoneServices.setCellValueFactory(cellData -> new SimpleStringProperty(
    			"Date: " + cellData.getValue().getDoneServiceOptionalDate()
    			+"Mileage: " + cellData.getValue().getDoneMileageOptionalValue()
    			+"Operation: "+ cellData.getValue().getOperation()));
        choiceBoxBus.selectionModelProperty().addListener(
                (observable, oldValue, newValue) -> showBusDetails(newValue)
        );
    }

    private void showBusDetails(SingleSelectionModel<Bus> bus)
    {
        if(bus != null)
        {         
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(bus.getSelectedItem().getDateOfBuy().getTime());
            LocalDate today = LocalDate.now();
            LocalDate birthdayOfBus = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

            long periodMonths = ChronoUnit.MONTHS.between(birthdayOfBus, today);
            textFieldAge.setText(periodMonths + " months");
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
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(selectedBus.getDateOfBuy().getTime());
            LocalDate today = LocalDate.now();
            LocalDate birthdayOfBus = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));

            long periodMonths = ChronoUnit.MONTHS.between(birthdayOfBus, today);
            textFieldAge.setText(periodMonths + " months");
            textFieldMileage.setText(String.valueOf(selectedBus.getMileage()));
            tableDoneServices.setItems(mainApp.getServiceHistory(selectedBus));
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
    
    @FXML
    private void handleCheck() {
    	ObservableList<Service> tmpNeeded =mainApp.getServicesFromPlan(choiceBoxBus.getSelectionModel().getSelectedItem(), 
    			Integer.valueOf(textFieldAge.getText().substring(0,textFieldAge.getText().indexOf(' '))),
    			Integer.valueOf(textFieldMileage.getText()));
    	ObservableList<Service> tmpDone  = mainApp.getServiceHistory(choiceBoxBus.getSelectionModel().getSelectedItem());
    	
    	for(Service sn : tmpNeeded) {
    		for(Service sd : tmpDone) {
    			if(sn.getOperation().equals(sd.getOperation())) {
    				Calendar cal = Calendar.getInstance();
    		        cal.setTimeInMillis(sd.getDoneServiceOptionalDate().getTime());
    				LocalDate todayDate = LocalDate.now();
    	            LocalDate doneDate = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
    	            long periodDays = ChronoUnit.DAYS.between(doneDate, todayDate);
    	            sn.setDaysToDo((int)periodDays);
    			}
    		}
    	}
    	tableServices.setItems(tmpNeeded);
    }
    
    @FXML 
    private void handleServiceAdding() {
    	ObservableList<Service> tmp = tableServices.getItems();
    	tmp.add(mainApp.getService().get(choiceBoxService.getSelectionModel().getSelectedIndex()));
    }
}
