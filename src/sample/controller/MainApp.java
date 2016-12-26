package sample.controller;

import java.io.IOException;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.model.Bus;
import sample.model.Drive;
import sample.model.IntermediateDrive;
import sample.view.BusOverviewController;
import sample.view.DriveOverviewController;
import sample.view.PlanDriveController;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private TabPane tabbedLayout;
    private ObservableList<Drive> driveData = FXCollections.observableArrayList();
    private ObservableList<Bus> busData = FXCollections.observableArrayList();
    //private ObservableList<IntermediateDrive> intermediateDrivesData = FXCollections.observableArrayList();
    private DataBaseHandler dbh;
    public MainApp()
    {
    	dbh = new DataBaseHandler();
        driveData.addAll(dbh.getAllDrives());
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("WBD");

        initRootLayout();
        initTabLayout();
        showDriveOverview();
        showBusOverview();
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
            e.printStackTrace();
        }
    }

    public void initTabLayout() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/TabLayout.fxml"));
            tabbedLayout = (TabPane) loader.load();

            rootLayout.setCenter(tabbedLayout);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the drive overview inside the root layout.
     */
    public void showDriveOverview() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/DriveOverview.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            tabbedLayout.getTabs().get(0).setContent(personOverview);

            DriveOverviewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showBusOverview() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/BusOverview.fxml"));
            AnchorPane busOverview = (AnchorPane) loader.load();

            tabbedLayout.getTabs().get(1).setContent(busOverview);
            BusOverviewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean showDriveEditDialog(Drive drive) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/sample/view/PlanDrive.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            tabbedLayout.getTabs().get(0).setContent(page);

            PlanDriveController controller = loader.getController();

            controller.setMainApp(this);
            controller.setDrive(drive);

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
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
}
