package sample.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.*;

/**
 * Created by Marek on 2016-12-25.
 */
public class Drive {

	private int id;
    private final StringProperty from;
    private final StringProperty to;
    private final IntegerProperty time;
    private final IntegerProperty distance;
    private final IntegerProperty price;
    private final List<IntermediateDrive> listOfIntermediateDrive;

    public Drive()
    {
        this(0, null,null);
    }

    public Drive(int id, String from, String to)
    {
    	this.id = id;
        this.from = new SimpleStringProperty(from);
        this.to = new SimpleStringProperty(to);
        this.time = new SimpleIntegerProperty(0);
        this.distance = new SimpleIntegerProperty(0);
        this.price = new SimpleIntegerProperty(0);
        this.listOfIntermediateDrive = null;
    }

    public Drive(int id, String from, String to, int time, int distance, int price)
    {
    	this.id = id;
        this.from = new SimpleStringProperty(from);
        this.to = new SimpleStringProperty(to);
        this.time = new SimpleIntegerProperty(time);
        this.distance = new SimpleIntegerProperty(distance);
        this.price = new SimpleIntegerProperty(price);
        this.listOfIntermediateDrive = null;
    }

    public Drive(int id, String from, String to, int time, int distance, int price, List<IntermediateDrive> listOfIntermediateDrive)
    {
    	this.id = id;
        this.from = new SimpleStringProperty(from);
        this.to = new SimpleStringProperty(to);
        this.time = new SimpleIntegerProperty(time);
        this.distance = new SimpleIntegerProperty(distance);
        this.price = new SimpleIntegerProperty(price);
        this.listOfIntermediateDrive = new ArrayList<>(listOfIntermediateDrive);
    }

    public String getFrom() {
        return from.get();
    }

    public StringProperty fromProperty() {
        return from;
    }

    public void setFrom(String from) {
        this.from.set(from);
    }

    public String getTo() {
        return to.get();
    }

    public StringProperty toProperty() {
        return to;
    }

    public void setTo(String to) {
        this.to.set(to);
    }

    public int getTime() {
        return time.get();
    }

    public IntegerProperty timeProperty() {
        return time;
    }

    public void setTime(int time) {
        this.time.set(time);
    }

    public int getDistance() {
        return distance.get();
    }

    public IntegerProperty distanceProperty() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance.set(distance);
    }

    public int getPrice() {
        return price.get();
    }

    public IntegerProperty priceProperty() {
        return price;
    }

    public void setPrice(int price) {
        this.price.set(price);
    }

    public List<IntermediateDrive> getListOfIntermediateDrive() {
        return listOfIntermediateDrive;
    }

	public int getId() {
		return id;
	}

}
