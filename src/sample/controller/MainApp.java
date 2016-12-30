package sample.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import sample.model.*;
import sample.view.*;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private TabPane tabbedLayout;
	private ObservableList<Drive> driveData = FXCollections.observableArrayList();
	private ObservableList<Bus> busData = FXCollections.observableArrayList();
	private ObservableList<Person> freeDrivers = FXCollections.observableArrayList();
	private ObservableList<Person> freeHostess = FXCollections.observableArrayList();
	private ObservableList<Services> services = FXCollections.observableArrayList();
	private ObservableList<BusModel> busModelData = FXCollections.observableArrayList();
	private DataBaseHandler dbh;

	public static void main(String[] args) {
		launch(args);
	}

	private enum tabs {
		CREATE_COURSE(2), DRIVE_OVERVIEW(0), BUS_OVERVIEW(1), CHECKUP(3);
		public int number;

		tabs(int i) {
			number = i;
		};
	}

	public MainApp() {
		dbh = new DataBaseHandler();
		driveData.addAll(dbh.getAllDrives());
		busData.addAll(dbh.getAllBuses());
		freeDrivers.addAll(dbh.getFreeDrivers());
		freeHostess.addAll(dbh.getFreeHostess());
		services.addAll(dbh.getServices());
		busModelData.addAll(dbh.getAllBusModels());
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("WBD");

		initRootLayout();
		initTabLayout();
		showCourseCreation();
		showDriveOverview();
		showBusOverview();
		showBusesCheckup();

	}

	/**
	 * Initializes the root layout.
	 */
	public void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/Root.fxml"));
			rootLayout = (BorderPane) loader.load();

			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			exceptionDialog(e);
		}
	}

	public void initTabLayout() {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/TabLayout.fxml"));
			tabbedLayout = (TabPane) loader.load();

			rootLayout.setCenter(tabbedLayout);

		} catch (IOException e) {
			exceptionDialog(e);
		}
	}

	public void showCourseCreation() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/AddingCourse.fxml"));
			AnchorPane courseCreation = (AnchorPane) loader.load();

			tabbedLayout.getTabs().get(tabs.CREATE_COURSE.number).setContent(courseCreation);

			AddingCourseController controller = loader.getController();
			controller.setMainApp(this);
		} catch (IOException e) {
			exceptionDialog(e);
		}
	}

	/**
	 * Shows the drive overview inside the root layout.
	 */
	public void showDriveOverview() {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/DriveOverview.fxml"));
			AnchorPane driveOverview = (AnchorPane) loader.load();

			tabbedLayout.getTabs().get(tabs.DRIVE_OVERVIEW.number).setContent(driveOverview);

			DriveOverviewController controller = loader.getController();
			controller.setMainApp(this);
		} catch (IOException e) {
			exceptionDialog(e);
		}
	}

	public void showBusOverview() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/BusOverview.fxml"));
			AnchorPane busOverview = (AnchorPane) loader.load();

			tabbedLayout.getTabs().get(tabs.BUS_OVERVIEW.number).setContent(busOverview);
			BusOverviewController controller = loader.getController();
			controller.setMainApp(this);
		} catch (IOException e) {
			exceptionDialog(e);
		}
	}

	public void showBusesCheckup() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/BusCheckup.fxml"));
			AnchorPane busOverview = (AnchorPane) loader.load();

			tabbedLayout.getTabs().get(tabs.CHECKUP.number).setContent(busOverview);

			BusCheckupController controller = loader.getController();
			controller.setMainApp(this);
		} catch (IOException e) {
			exceptionDialog(e);
		}
	}

	public boolean showDriveEditDialog(Drive drive) {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/sample/view/PlanDrive.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			tabbedLayout.getTabs().get(tabs.DRIVE_OVERVIEW.number).setContent(page);

			PlanDriveController controller = loader.getController();

			controller.setMainApp(this);
			controller.setDrive(drive);

			return controller.isOkClicked();
		} catch (IOException e) {
			exceptionDialog(e);
			return false;
		}
	}

	/**
	 * Returns the main stage.
	 * 
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public ObservableList<Drive> getDriveData() {
		return driveData;
	}

	public ObservableList<IntermediateDrive> getIntermediateDrivesData() {
		ObservableList<IntermediateDrive> intermediateDrivesData = FXCollections.observableArrayList();
		intermediateDrivesData.addAll(dbh.getIntermediateDrivesData());
		return intermediateDrivesData;
	}

	public ObservableList<IntermediateDrive> getIntermediateDrivesFromData(String cityFrom) {
		ObservableList<IntermediateDrive> intermediateDrivesFromData = FXCollections.observableArrayList();
		intermediateDrivesFromData.addAll(dbh.getIntermediateDrivesFromData(cityFrom));
		return intermediateDrivesFromData;
	}

	public ObservableList<Bus> getBusData() {
		return busData;
	}

	public void exceptionDialog(Exception e) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Exception Dialog");
		alert.setHeaderText("Look, an Exception Dialog");
		alert.setContentText("Error has occured");

		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("The exception stacktrace was:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
	}

	public void goToBusInfo() {
		tabbedLayout.getSelectionModel().select(tabs.BUS_OVERVIEW.number);
		// showBusDetails(bus);
	}

	public ObservableList<BusModel> getBusModelData() {
		return busModelData;
	}

	public ObservableList<Services> getService() {
		return services;
	}

	public ObservableList<Person> getFreeDrivers() {
		return freeDrivers;
	}

	public ObservableList<Person> getFreeHostess() {
		return freeHostess;
	}

	public boolean showBusEditDialog(Bus bus) {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/sample/view/BusEdit.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			tabbedLayout.getTabs().get(tabs.BUS_OVERVIEW.number).setContent(page);

			BusEditController controller = loader.getController();

			controller.setMainApp(this);
			controller.setBus(bus);

			return controller.isOkClicked();
		} catch (IOException e) {
			exceptionDialog(e);
			return false;
		}
	}
	
	public void insertDrive(Drive drive){
		dbh.insertDrive(drive);
		driveData.add(drive);
	}
	
	public void editDrive(Drive drive) {
		dbh.updateDrive(drive);
	}
	
	public void deleteDrive(Drive drive) {
		dbh.deleteDrive(drive);
	}
	
	public void insertBus(Bus bus){
		dbh.insertBus(bus);
		busData.add(bus);
	}
	
	public void editBus(Bus bus) {
		dbh.updateBus(bus);
	}
	
	public void deleteBus(Bus bus) {
		dbh.deleteBus(bus);
	}

}
