package sample.model;

public class TimeTablePosition {
	private int id;
	private int driveId;
	private int categoryId;
	private String weekDay;
	private String leavingHour;
	private String registerPhone;
	
	public TimeTablePosition(int id, int driveId,
			int categoryId, String weekDay, String leavingHour, String registerPhone) 
	{
		this.id = id;
		this.driveId = driveId;
		this.categoryId = categoryId;
		this.weekDay = weekDay;
		this.leavingHour = leavingHour;
		this.registerPhone = registerPhone;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDriveId() {
		return driveId;
	}

	public void setDriveId(int driveId) {
		this.driveId = driveId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}

	public String getLeavingHour() {
		return leavingHour;
	}

	public void setLeavingHour(String leavingHour) {
		this.leavingHour = leavingHour;
	}

	public String getRegisterPhone() {
		return registerPhone;
	}

	public void setRegisterPhone(String registerPhone) {
		this.registerPhone = registerPhone;
	}
}
