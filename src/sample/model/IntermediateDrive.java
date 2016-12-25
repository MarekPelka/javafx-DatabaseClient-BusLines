package sample.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Marek on 2016-12-25.
 */
public class IntermediateDrive {

    private final StringProperty cityFrom;
    private final StringProperty cityTo;
    private final IntegerProperty time;
    private final IntegerProperty distance;
    private final IntegerProperty price;

    public IntermediateDrive()
    {
        this(null, null);
    }

    public IntermediateDrive(String from, String to)
    {
        this.cityFrom = new SimpleStringProperty(from);
        this.cityTo = new SimpleStringProperty(to);
        this.time = new SimpleIntegerProperty(0);
        this.distance = new SimpleIntegerProperty(0);
        this.price = new SimpleIntegerProperty(0);

    }

    public IntermediateDrive(String from, String to, int time, int distance, int price)
    {
        this.cityFrom = new SimpleStringProperty(from);
        this.cityTo = new SimpleStringProperty(to);
        this.time = new SimpleIntegerProperty(time);
        this.distance = new SimpleIntegerProperty(distance);
        this.price = new SimpleIntegerProperty(price);

    }

    public String getCityFrom() {
        return cityFrom.get();
    }

    public StringProperty cityFromProperty() {
        return cityFrom;
    }

    public void setCityFrom(String cityFrom) {
        this.cityFrom.set(cityFrom);
    }

    public String getCityTo() {
        return cityTo.get();
    }

    public StringProperty cityToProperty() {
        return cityTo;
    }

    public void setCityTo(String cityTo) {
        this.cityTo.set(cityTo);
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
}
