package sample.view;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import sample.controller.MainApp;
import sample.model.Bus;

public class BusEditController {
	@FXML
	private TextField txtPlate;
	@FXML
	private TextField txtSerialNumber;
	@FXML
	private TextField txtMileage;
	@FXML
	private TextField txtSeats;
	@FXML
	private ComboBox<String> comboBoxModel;
	@FXML
	private TextField txtCategory;
	@FXML
    private DatePicker datePicker;
	
	private MainApp mainApp;
	private Bus bus;
	
	private boolean okClicked;
	
	 @FXML
    private void initialize() {
	//TODO
    }

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;		
		
		comboBoxModel.setItems(mainApp.getBusModelData().stream().map(bm -> bm.getModelName())
    			.collect(Collectors.toCollection(FXCollections::observableArrayList)));
	}

	public void setBus(Bus bus) {
		this.bus = bus;
		
		if(bus.getBusId() != -1)
		{
			txtPlate.setText(bus.getLicensePlate());
			txtSerialNumber.setText(bus.getSereialNumber());
			txtMileage.setText("" + bus.getMileage());
			txtSeats.setText("" + bus.getSeats());
			comboBoxModel.selectionModelProperty().get().select(bus.getModelName());
			txtCategory.setText("" + bus.getClassRate());
		}
		
	}

	public boolean isOkClicked() {
		return okClicked;
	}
	
	@FXML
    private void handleOk() {
		bus.setLicensePlate(txtPlate.getText());
		bus.setSereialNumber(txtSerialNumber.getText());
		bus.setMileage(Integer.valueOf(txtMileage.getText()));
		bus.setSeats(Integer.valueOf(txtSeats.getText()));
		LocalDate ld = datePicker.getValue();
		Calendar c =  Calendar.getInstance();
		c.set(ld.getYear(), ld.getMonthValue(), ld.getDayOfMonth());
		Date date = new Date(c.getTimeInMillis());
		bus.setDateOfBuy(date);
		bus.setClassRate(Float.valueOf(txtCategory.getText()));
		
        okClicked = true;
        mainApp.showBusOverview();
    }
	
   @FXML
    private void handleCancel() {
        mainApp.showDriveOverview();
    }
}
