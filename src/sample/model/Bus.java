package sample.model;

import javafx.beans.property.*;

import java.sql.Date;

/**
 * Created by Marek on 2016-12-25.
 */
public class Bus {

    private final int busId;
    private final IntegerProperty category;
    private final int busModelId;
    private final Date dateOfBuy;
    private final StringProperty licensePlate;
    private final StringProperty sereialNumber;
    private final IntegerProperty seats;
    private final IntegerProperty mileage;

    public Bus(){this(null);}

    public Bus(String licensePlate){
        this.licensePlate = new SimpleStringProperty(licensePlate);

        this.busModelId = 0;
        this.dateOfBuy = new Date(0);
        this.busId = -1;
        this.category = new SimpleIntegerProperty(0);
        this.sereialNumber = new SimpleStringProperty("null");
        this.seats = new SimpleIntegerProperty(0);
        this.mileage = new SimpleIntegerProperty(0);
    }

    public Bus(int id, int category, int busModelId, Date date, String licensePlate, String sereialNumber, int seats, int mileage){

        this.busId = id;
        this.category = new SimpleIntegerProperty(category);
        this.busModelId = busModelId;
        this.dateOfBuy = new Date(date.getTime());
        this.licensePlate = new SimpleStringProperty(licensePlate);
        this.sereialNumber = new SimpleStringProperty(sereialNumber);
        this.seats = new SimpleIntegerProperty(seats);
        this.mileage = new SimpleIntegerProperty(mileage);
    }

    public int getMileage() {
        return mileage.get();
    }

    public IntegerProperty mileageProperty() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage.set(mileage);
    }

    public int getBusId() {
        return busId;
    }

    public int getCategory() {
        return category.get();
    }

    public IntegerProperty categoryProperty() {
        return category;
    }

    public void setCategory(int category) {
        this.category.set(category);
    }

    public int getBusModelId() {
        return busModelId;
    }

    public Date getDateOfBuy() {
        return dateOfBuy;
    }

    public String getLicensePlate() {
        return licensePlate.get();
    }

    public StringProperty licensePlateProperty() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate.set(licensePlate);
    }

    public String getSereialNumber() {
        return sereialNumber.get();
    }

    public StringProperty sereialNumberProperty() {
        return sereialNumber;
    }

    public void setSereialNumber(String sereialNumber) {
        this.sereialNumber.set(sereialNumber);
    }

    public int getSeats() {
        return seats.get();
    }

    public IntegerProperty seatsProperty() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats.set(seats);
    }
}
