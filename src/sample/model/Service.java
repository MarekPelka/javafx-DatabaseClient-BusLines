package sample.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Marek on 2016-12-28.
 */
public class Service {

    private final IntegerProperty id;
    private final StringProperty operation;
    private final StringProperty importance;
    private final StringProperty doneServiceOptionalDate;
    public Service(){this(null);}

    public Service(String operation)
    {
    	this(-1,operation,null,null);
    }

    public Service(int id, String operation, String importance)
    {
    	this(id,operation,importance,null);
    }
    
    public Service(int id, String operation, String importance, String doneServiceOptionalDate) 
    {
    	this.id = new SimpleIntegerProperty(id);
        this.operation = new SimpleStringProperty(operation);
        this.importance = new SimpleStringProperty(importance);
        this.doneServiceOptionalDate = new SimpleStringProperty(doneServiceOptionalDate);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getOperation() {
        return operation.get();
    }

    public StringProperty operationProperty() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation.set(operation);
    }

    public String getImportance() {
        return importance.get();
    }

    public StringProperty importanceProperty() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance.set(importance);
    }

	public StringProperty doneServiceOptionalDateProperty() {
		return doneServiceOptionalDate;
	}
	
	public String getDoneServiceOptionalDate () {
		return doneServiceOptionalDate.get();
	}
}
