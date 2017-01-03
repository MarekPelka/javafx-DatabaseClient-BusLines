package sample.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import consts.BusConsts;
import consts.BusModelConsts;
import consts.DriveConsts;
import consts.IntermediateDriveConsts;
import consts.PersonConsts;
import consts.ServiceConsts;
import consts.TimeTablePositionConsts;
import sample.model.Bus;
import sample.model.BusModel;
import sample.model.Course;
import sample.model.Drive;
import sample.model.IntermediateDrive;
import sample.model.Person;
import sample.model.Service;
import sample.model.TimeTablePosition;

public class DataBaseHandler {
	/** Packet address for JDBC driver */
	private static String driver;
	/** URL data base address */
	private static String url;
	/** User login for data base */
	private static String login;
	/** User password for data base */
	private static String password;

	/**
	 * STATIC BLOCK - update default configuration data/download from
	 * dbconfig.properties file - start JDBC driver
	 */
	static {
		try {
			Configuration();
		} catch (IOException ex) {
			System.err.println("Configuration failed.\n" + ex.getMessage());
		}

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException ex) {
			System.err.println("Registration the JDBC/ODBC driver failed.\n" + ex.getMessage());
		}
	}

	private static void Configuration() throws IOException {
		File file = new File("dbconfig.properties");
		if (!file.exists()) {
			file.createNewFile();
			String defaultData = "#Driver for JDBC\n" + "driver=oracle.jdbc.driver.OracleDriver\n"
					+ "#Data Base adress (default on your localhost on 80 port)\n"
					+ "url=jdbc:oracle:thin:@//localhost:1522/orcl\n" + "#login on your database\n" + "login=xxx\n"
					+ "#your password on database\n" + "password=xxx";
			FileWriter fileWritter = new FileWriter(file.getName(), true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.write(defaultData);
			bufferWritter.close();
		}
		try (FileReader reader = new FileReader("dbconfig.properties")) {
			Properties prop = new Properties();
			prop.load(reader);
			driver = prop.getProperty("driver");
			url = prop.getProperty("url");
			login = prop.getProperty("login");
			password = prop.getProperty("password");

		}

	}

	/** Returns connection handler through driver */
	private Connection createConnection() throws SQLException {
		return DriverManager.getConnection(url, login, password);
	}

	/** Closing connection with local data base */
	private void endConnection(Connection connection) {
		if (connection == null) {
			return;
		}
		try {
			connection.close();
		} catch (SQLException ex) {
			System.err.println("Closing connection with local data base failed.\n" + ex.getSQLState());
		}
	}

	/**
	 * Getting all drives + price,time and distance summed within all
	 * intermediate drives in
	 */
	public List<Drive> getAllDrives() {
		String query = "select d.DRIVE_ID,d.CITY_FROM,d.CITY_TO, sum(idr.DISTANCE) as DISTANCE_SUM,"
				+ " sum(idr.PRICE) as PRICE_SUM, sum(idr.TIME) as TIME_SUM" + " from DRIVES d,INTERMEDIATE_DRIVES idr"
				+ " where  idr.INTERMEDIATE_DRIVE_ID in (SELECT INTERMEDIATE_DRIVE_ID "
				+ " from DRIVE_CONTENTS where DRIVE_ID=d.DRIVE_ID) group by d.DRIVE_ID,d.CITY_FROM,d.CITY_TO";
		List<Drive> result = new ArrayList<>();
		Connection c = null;
		try {
			c = createConnection();
			Statement statement = c.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				Drive drive = new Drive(resultSet.getInt(DriveConsts.ID), resultSet.getString(DriveConsts.CITY_FROM),
						resultSet.getString(DriveConsts.CITY_TO), resultSet.getInt("TIME_SUM"),
						resultSet.getInt("DISTANCE_SUM"), resultSet.getFloat("PRICE_SUM"),
						getAllIntermediateDrives(resultSet.getInt(DriveConsts.ID)));
				result.add(drive);
			}
		} catch (SQLException ex) {
			System.err.println("Selecting drives with intermediate drives data failed.\n" + ex.getSQLState());
		} finally {
			endConnection(c);
		}

		return result;
	}

	/** Getting all buses joining bus model related with each bus */
	public List<Bus> getAllBuses() {
		String query = "select * from BUSES join BUS_MODELS on BUSES.BUS_MODEL_ID = BUS_MODELS.BUS_MODEL_ID";
		List<Bus> result = new ArrayList<>();
		Connection c = null;
		try {
			c = createConnection();
			Statement statement = c.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				Bus bus = new Bus(resultSet.getInt(BusConsts.ID), resultSet.getInt(BusConsts.MODEL_ID),
						resultSet.getDate(BusConsts.BOUGHT_TIME), resultSet.getString(BusConsts.LICENSE_PLATE),
						resultSet.getString(BusConsts.SERIAL_NUMBER), resultSet.getInt(BusConsts.SEATS_NUM),
						resultSet.getInt(BusConsts.MILEAGE), resultSet.getFloat(BusConsts.CLASS_RATE),
						resultSet.getString(BusConsts.BUS_MODEL_NAME));
				result.add(bus);
			}
		} catch (SQLException ex) {
			System.err.println("Selecting all buses failed.\n" + ex.getSQLState());
		} finally {
			endConnection(c);
		}

		return result;
	}

	/** Getting all intermediate drives related with drive with given id */
	private List<IntermediateDrive> getAllIntermediateDrives(int driveId) {
		String query = "select * from INTERMEDIATE_DRIVES where INTERMEDIATE_DRIVE_ID in "
				+ "(select INTERMEDIATE_DRIVE_ID " + "from DRIVE_CONTENTS where DRIVE_ID=" + driveId + ")";
		List<IntermediateDrive> result = new ArrayList<>();
		Connection c = null;
		try {
			c = createConnection();
			Statement statement = c.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				IntermediateDrive iDrive = new IntermediateDrive(resultSet.getInt(IntermediateDriveConsts.ID),
						resultSet.getString(IntermediateDriveConsts.CITY_FROM),
						resultSet.getString(IntermediateDriveConsts.CITY_TO),
						resultSet.getInt(IntermediateDriveConsts.TIME),
						resultSet.getInt(IntermediateDriveConsts.DISTANCE),
						resultSet.getFloat(IntermediateDriveConsts.PRICE));
				result.add(iDrive);
			}
		} catch (SQLException ex) {
			System.err.println("Selecting intermediate drives of one drive failed.\n" + ex.getSQLState());
		} finally {
			endConnection(c);
		}

		return result;
	}
	
	/** Getting all data of services where mileage or age is greater than needed in plan service */
	public List<Service> getServicesFromPlan(Bus bus,int ageInMonths, int mileage) {
		String query =
				"select distinct s.SERVICE_ID,s.OPERATION,s.MEANINGNESS,spp.MILEAGE_KM,spp.MONTHS_TO_SERVICE from SERVICES s "
				+"inner join  POSITION_REQUIREMENTS pr on s.SERVICE_ID = pr.SERVICE_ID "
				+"inner join SERVICE_PLAN_POSITIONS spp on spp.SERVICE_PLAN_POSITION_ID = pr.SERVICE_PLAN_POSITION_ID  "
				+"where spp.BUS_MODEL_ID=? AND (spp.MILEAGE_KM < ? OR spp.MONTHS_TO_SERVICE < ?) ";
		List<Service> result = new ArrayList<>();
		Connection c = null;
		try {
			c = createConnection();
			PreparedStatement statement = c.prepareStatement(query);
			int counter = 1;
			statement.setInt(counter++, bus.getBusModelId());
			statement.setInt(counter++, ageInMonths);
			statement.setInt(counter++, mileage);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				Service service = new Service(resultSet.getInt(ServiceConsts.ID),
						resultSet.getString(ServiceConsts.OPERATION),
						resultSet.getString(ServiceConsts.MEANINGNESS),
						resultSet.getInt(ServiceConsts.NEEDED_MILEAGE),
						resultSet.getInt(ServiceConsts.NEEDED_MONTH));
				result.add(service);
			}
		} catch (SQLException ex) {
			System.err.println("Selecting services from service plan  failed.\n" + ex.getSQLState());
		} finally {
			endConnection(c);
		}

		return result;
	}
	
	/** Getting all data of done  services with date for given bus */
	public List<Service> getServiceHistory(Bus bus) {
		String query = "select s.SERVICE_ID, s.OPERATION, s.MEANINGNESS, sbp.SERVICE_DATE, sbp.MILEAGE_THIS_TIME"
				+ " from SERVICES_BOOK_POSITIONS sbp "
				+ "inner join SERVICE_ACCOMPLISHMENTS sa on sa.SERVICES_BOOK_POSITION_ID=sbp.SERVICES_BOOK_POSITION_ID "
				+ "inner join SERVICES s on s.SERVICE_ID=sa.SERVICE_ID where BUS_ID=" + bus.getBusId();
		List<Service> result = new ArrayList<>();
		Connection c = null;
		try {
			c = createConnection();
			Statement statement = c.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				Service service = new Service(resultSet.getInt(ServiceConsts.ID),
						resultSet.getString(ServiceConsts.OPERATION),
						resultSet.getString(ServiceConsts.MEANINGNESS),
						resultSet.getDate(ServiceConsts.OPTIONAL_DATE),
						resultSet.getInt(ServiceConsts.OPTIONAL_MILEAGE));
				result.add(service);
			}
		} catch (SQLException ex) {
			System.err.println("Selecting services with dates failed.\n" + ex.getSQLState());
		} finally {
			endConnection(c);
		}

		return result;
	}

	/** Getting all intermediate drives */
	public List<IntermediateDrive> getIntermediateDrivesData() {
		String query = "select * from INTERMEDIATE_DRIVES ORDER BY INTERMEDIATE_DRIVE_ID";
		List<IntermediateDrive> result = new ArrayList<>();
		Connection c = null;
		try {
			c = createConnection();
			Statement statement = c.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				IntermediateDrive iDrive = new IntermediateDrive(resultSet.getInt(IntermediateDriveConsts.ID),
						resultSet.getString(IntermediateDriveConsts.CITY_FROM),
						resultSet.getString(IntermediateDriveConsts.CITY_TO),
						resultSet.getInt(IntermediateDriveConsts.TIME),
						resultSet.getInt(IntermediateDriveConsts.DISTANCE),
						resultSet.getFloat(IntermediateDriveConsts.PRICE));
				result.add(iDrive);
			}
		} catch (SQLException ex) {
			System.err.println("Selecting intermediate drives data failed.\n" + ex.getSQLState());
		} finally {
			endConnection(c);
		}

		return result;
	}

	/** Getting intermediate drives where starting city is given in argument */
	public List<IntermediateDrive> getIntermediateDrivesFromData(String cityFrom) {
		String query = "select * from INTERMEDIATE_DRIVES where CITY_FROM = '" + cityFrom + "'";
		List<IntermediateDrive> result = new ArrayList<>();
		Connection c = null;
		try {
			c = createConnection();
			Statement statement = c.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				IntermediateDrive iDrive = new IntermediateDrive(resultSet.getInt(IntermediateDriveConsts.ID),
						resultSet.getString(IntermediateDriveConsts.CITY_FROM),
						resultSet.getString(IntermediateDriveConsts.CITY_TO),
						resultSet.getInt(IntermediateDriveConsts.TIME),
						resultSet.getInt(IntermediateDriveConsts.DISTANCE),
						resultSet.getFloat(IntermediateDriveConsts.PRICE));
				result.add(iDrive);
			}
		} catch (SQLException ex) {
			System.err.println("Selecting intermediate drvies from stating city failed.\n" + ex.getSQLState());
		} finally {
			endConnection(c);
		}

		return result;
	}

	

	/** Getting all services */
	public List<Service> getServices() {
		String query = "select * from SERVICES";
		List<Service> result = new ArrayList<>();
		Connection c = null;
		try {
			c = createConnection();
			Statement statement = c.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				Service service = new Service(resultSet.getInt("SERVICE_ID"), resultSet.getString("OPERATION"),
						resultSet.getString("MEANINGNESS"));
				result.add(service);
			}
		} catch (SQLException ex) {
			System.err.println("Selecting services from data failed.\n" + ex.getSQLState());
		} finally {
			endConnection(c);
		}

		return result;
	}

	/** Getting all bus models */
	public List<BusModel> getAllBusModels() {
		String query = "select * from BUS_MODELS";
		List<BusModel> result = new ArrayList<>();
		Connection c = null;
		try {
			c = createConnection();
			Statement statement = c.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				BusModel busModel = new BusModel(resultSet.getInt(BusModelConsts.ID),
						resultSet.getString(BusModelConsts.MODEL_NAME));
				result.add(busModel);
			}
		} catch (SQLException ex) {
			System.err.println("Selecting all bus models failed.\n" + ex.getSQLState());
		} finally {
			endConnection(c);
		}

		return result;
	}

	/** INSERT UPDATE DELETE DRIVE */
	public void insertDrive(Drive drive) {
		String query = "INSERT INTO " + "DRIVES" + "(DRIVE_ID,CITY_FROM,CITY_TO) " + "VALUES(DRIVE_SEQ.NEXTVAL,?,?)";
		Connection c = null;
		try {
			c = createConnection();
			PreparedStatement statement = c.prepareStatement(query);
			int counter = 1;
			statement.setString(counter++, drive.getFrom());
			statement.setString(counter++, drive.getTo());
			statement.executeUpdate();
			int id = getDriveId(drive);
			for (IntermediateDrive idr : drive.getListOfIntermediateDrive()) {
				connectIntermediateDriveWithDrive(idr.getId(), id);
			}
		} catch (SQLException ex) {
			System.err.println("Inserting drive failed.\n" + ex.getSQLState());
		} finally {
			endConnection(c);
		}
	}

	/** Inserting intermediate drive id and drive id in DRIVE_CONTENTS */
	private void connectIntermediateDriveWithDrive(int intermediateId, int driveId) {
		String query = "INSERT INTO " + "DRIVE_CONTENTS" + "(INTERMEDIATE_DRIVE_ID,DRIVE_ID) " + "VALUES(?,?)";
		Connection c = null;
		try {
			c = createConnection();
			PreparedStatement statement = c.prepareStatement(query);
			int counter = 1;
			statement.setInt(counter++, intermediateId);
			statement.setInt(counter++, driveId);
			statement.executeUpdate();
		} catch (SQLException ex) {
			System.err.println("Inserting in DRIVE_CONTENTS failed.\n" + ex.getSQLState());
		} finally {
			endConnection(c);
		}
	}

	public int getDriveId(Drive drive) {
		String query = "select DRIVE_ID from DRIVES where CITY_FROM=? AND CITY_TO=?";
		Connection c = null;
		try {
			c = createConnection();
			PreparedStatement statement = c.prepareStatement(query);
			int counter = 1;
			statement.setString(counter++, drive.getFrom());
			statement.setString(counter++, drive.getTo());
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				return resultSet.getInt("DRIVE_ID");
			}
		} catch (SQLException ex) {
			System.err.println("Selecting drive id via cities failed.\n" + ex.getSQLState());
		} finally {
			endConnection(c);
		}
		return -1;
	}

	public void updateDrive(Drive drive) {
		String query = "update DRIVES set CITY_FROM=?,CITY_TO=? " + "where DRIVE_ID=?";
		Connection c = null;
		try {
			c = createConnection();
			PreparedStatement statement = c.prepareStatement(query);
			int counter = 1;
			statement.setString(counter++, drive.getFrom());
			statement.setString(counter++, drive.getTo());
			statement.setInt(counter++, drive.getId());
			statement.executeUpdate();
		} catch (SQLException ex) {
			System.err.println("Updating drive failed.\n" + ex.getSQLState());
		} finally {
			endConnection(c);
		}
	}

	public void deleteDrive(Drive drive) {
		String query = "delete from DRIVES where DRIVE_ID=?";
		Connection c = null;
		try {
			c = createConnection();
			PreparedStatement statement = c.prepareStatement(query);
			statement.setInt(1, drive.getId());
			statement.executeUpdate();
		} catch (SQLException ex) {
			System.err.println("Deleting drive failed.\n" + ex.getSQLState());
		} finally {
			endConnection(c);
		}
	}

	/** INSERT UPDATE DELETE BUS */
	public void insertBus(Bus bus) {
		String query = "INSERT INTO " + "BUSES" + "(BUS_ID,BUS_MODEL_ID,BOUGHT_TIME,LICENSE_PLATE,"
				+ "SERIAL_NUMBER,SEATS_NUMBER,MILEAGE,CLASS_RATE) " + "VALUES (BUS_SEQ.NEXTVAL,?,?,?,?,?,?,?)";
		Connection c = null;
		try {
			c = createConnection();
			PreparedStatement statement = c.prepareStatement(query);
			int counter = 1;
			BusModel model = getBusModelByName(bus.getModelName());
			statement.setInt(counter++, model.getId());
			statement.setDate(counter++, bus.getDateOfBuy());
			statement.setString(counter++, bus.getLicensePlate());
			statement.setString(counter++, bus.getSereialNumber());
			statement.setInt(counter++, bus.getSeats());
			statement.setInt(counter++, bus.getMileage());
			statement.setFloat(counter++, bus.getClassRate());
			statement.executeUpdate();
		} catch (SQLException ex) {
			System.err.println("Inserting bus failed.\n" + ex.getSQLState());
		} finally {
			endConnection(c);
		}
	}

	public void updateBus(Bus bus) {
		String query = "update BUSES set BUS_MODEL_ID=?,BOUGHT_TIME=?,LICENSE_PLATE=?,"
				+ "SERIAL_NUMBER=?,SEATS_NUMBER=?,MILEAGE=?,CLASS_RATE=? where BUS_ID=?";
		Connection c = null;
		try {
			c = createConnection();
			PreparedStatement statement = c.prepareStatement(query);
			int counter = 1;
			BusModel model = getBusModelByName(bus.getModelName());
			statement.setInt(counter++, model.getId());
			statement.setDate(counter++, bus.getDateOfBuy());
			statement.setString(counter++, bus.getLicensePlate());
			statement.setString(counter++, bus.getSereialNumber());
			statement.setInt(counter++, bus.getSeats());
			statement.setInt(counter++, bus.getMileage());
			statement.setFloat(counter++, bus.getClassRate());
			statement.setInt(counter++, bus.getBusId());
			statement.executeUpdate();
		} catch (SQLException ex) {
			System.err.println("Updating bus failed.\n" + ex.getSQLState());
		} finally {
			endConnection(c);
		}
	}

	public void deleteBus(Bus bus) {
		String query = "delete from BUSES where BUS_ID=?";
		Connection c = null;
		try {
			c = createConnection();
			PreparedStatement statement = c.prepareStatement(query);
			statement.setInt(1, bus.getBusId());
			statement.executeUpdate();
		} catch (SQLException ex) {
			System.err.println("Deleting bus failed.\n" + ex.getSQLState());
		} finally {
			endConnection(c);
		}
	}

	/** Getting bus model by name */
	private BusModel getBusModelByName(String busModelName) {
		String query = "select * from BUS_MODELS where NAME='" + busModelName + "'";
		BusModel busModel = null;
		Connection c = null;
		try {
			c = createConnection();
			Statement statement = c.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				busModel = new BusModel(resultSet.getInt(BusModelConsts.ID),
						resultSet.getString(BusModelConsts.MODEL_NAME));
			}
		} catch (SQLException ex) {
			System.err.println("Selecting  bus model by name failed.\n" + ex.getSQLState());
		} finally {
			endConnection(c);
		}

		return busModel;
	}
	
	
	public void insertServicesIntoBusServiceBook(Bus bus, List<Service> services, Date serviceDate, int mileageThisTime,
			String state, String carServiceAddress) {
		String query = "insert into SERVICES_BOOK_POSITIONS(SERVICES_BOOK_POSITION_ID,BUS_ID,SERVICE_DATE,MILEAGE_THIS_TIME,"
				+ "STATE,CAR_SERVICE_ADDRESS) values(SERVICES_BOOK_POSITION_SEQ.NEXTVAL,?,?,?,?,?)";
		Connection c = null;
		try {
			c = createConnection();
			PreparedStatement pstatement = c.prepareStatement(query);
			int counter = 1;
			pstatement.setInt(counter++, bus.getBusId());
			pstatement.setDate(counter++, serviceDate);
			pstatement.setInt(counter++, mileageThisTime);
			pstatement.setString(counter++, state);
			pstatement.setString(counter++, carServiceAddress);
			pstatement.executeUpdate();
			
			Statement stmt= c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT SERVICES_BOOK_POSITION_SEQ.CURRVAL FROM dual");
			long generatedId = -1;
			if ( rs!=null && rs.next() )
				generatedId =  	rs.getInt(1);
			for(Service service : services)
				insertServicesAccomplishment(generatedId,service);
		}catch (SQLException ex) {
			System.err.println("Inserting service book position  failed.\n" + ex.getSQLState());
		} finally {
			endConnection(c);
		}
	}

	private void insertServicesAccomplishment(long positionId,Service service) {
		String query = "insert into SERVICE_ACCOMPLISHMENTS(SERVICES_BOOK_POSITION_ID,SERVICE_ID) VALUES("
				+ positionId + "," + service.getId() +")";
		Connection c = null;
		try {
			c = createConnection();
			Statement statement = c.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException ex) {
			System.err.println("Inserting  service accomplishment failed.\n" + ex.getSQLState());
		} finally {
			endConnection(c);
		}
	}

	public List<TimeTablePosition> getTimeTablePositionfForDrive(int driveId) {
		String query = "select * from TIME_TABLE_POSITIONS where DRIVE_ID="+ driveId;
		List<TimeTablePosition> timeTable = new ArrayList<>();
		Connection c = null;
		try {
			c = createConnection();
			Statement statement = c.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				TimeTablePosition timetablePosition = new TimeTablePosition(
						resultSet.getInt(TimeTablePositionConsts.ID),
						resultSet.getInt(TimeTablePositionConsts.DRIVE_ID),
						resultSet.getInt(TimeTablePositionConsts.CATEGORY_ID),
						resultSet.getString(TimeTablePositionConsts.WEEK_DAY),
						resultSet.getString(TimeTablePositionConsts.LEAVING_HOUR),
						resultSet.getString(TimeTablePositionConsts.PHONE_NUMBER));
				timeTable.add(timetablePosition);
			}
		} catch (SQLException ex) {
			System.err.println("Selecting time table positions for drive failed.\n" + ex.getSQLState());
		} finally {
			endConnection(c);
		}

		return timeTable;
	}

	public List<Bus> getFreeBuses(Course courseData) {
		List<Drive> allDrives = getAllDrives();
		String query = "select * from COURSES c,TIME_TABLE_POSITIONS ttp "
				+ "where c.TIME_TABLE_POSITION_ID=ttp.TIME_TABLE_POSITION_ID";
		
		Calendar leavingCourseTime = courseData.getDate();
		Calendar arrivingCourseTime = Calendar.getInstance();
		arrivingCourseTime.setTimeInMillis(courseData.getDate().getTimeInMillis());
		arrivingCourseTime.add(Calendar.MINUTE, courseData.getCourseDrive().getTime());
		List<Integer> colidingBusesId = new ArrayList<>();
		Connection c = null;
		try {
			c = createConnection();
			Statement statement = c.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				String leavingTime = resultSet.getString("LEAVING_TIME");
				Date courseDate = resultSet.getDate("COURSE_DATE");
				Drive drive = null;
				int driveId = resultSet.getInt("DRIVE_ID");
				for(Drive d : allDrives) 
				{
					if(d.getId() == driveId)
						drive = d;
				}
				Calendar calLeaving =  Calendar.getInstance();
				calLeaving.setTime(courseDate);
				calLeaving.set(Calendar.HOUR_OF_DAY, Integer.valueOf(leavingTime.substring(0,leavingTime.indexOf(':')-1)));
				calLeaving.set(Calendar.MINUTE, Integer.valueOf(leavingTime.substring(leavingTime.indexOf(':')+1,leavingTime.length())));
				Calendar calArriving = Calendar.getInstance();
				calArriving.setTimeInMillis(calLeaving.getTimeInMillis());
				calArriving.add(Calendar.MINUTE, drive.getTime());
				if((calArriving.compareTo(arrivingCourseTime) < 0 && calArriving.compareTo(leavingCourseTime) > 0) ||
						(calLeaving.compareTo(arrivingCourseTime) < 0 && calLeaving.compareTo(leavingCourseTime) > 0)	) {
					colidingBusesId.add(resultSet.getInt("BUS_ID"));
				}
			}
			
		}catch (SQLException ex) {
			System.err.println("Selecting free buses failed.\n" + ex.getSQLState());
		} finally {
			endConnection(c);
		}
		return getAllBuses().stream().filter(b -> !colidingBusesId.contains(b.getBusId())).collect(Collectors.toList());
	}
	
	/** Getting all free drivers in time current */
	public List<Person> getFreeDrivers(Course courseData) {
		String query = "select * from DRIVERS d,PERSONS p  where d.DRIVER_ID not in "
				+ "(select gp.DRIVER_ID from GRAPHIC_POSITIONS gp where gp.DRIVER_ID is not null and "
				+ "? between gp.TIME_FROM and gp.TIME_TO "
				+ "and ? between gp.TIME_FROM and gp.TIME_TO ) AND d.PERSON_ID=p.PERSON_ID";
		List<Person> result = new ArrayList<>();
		Calendar dateLeaving = courseData.getDate();
		Calendar dateArriving = courseData.getDate();
		dateArriving.add(Calendar.MINUTE, courseData.getCourseDrive().getTime());
		Connection c = null;
		try {
			c = createConnection();
			PreparedStatement statement = c.prepareStatement(query);
			int counter = 1;
			statement.setDate(counter++, new Date(dateLeaving.getTimeInMillis()));
			statement.setDate(counter++, new Date(dateArriving.getTimeInMillis()));
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				Person person = new Person(resultSet.getInt(PersonConsts.ID), resultSet.getString(PersonConsts.NAME),
						resultSet.getString(PersonConsts.SURNAME));
				result.add(person);
			}
		} catch (SQLException ex) {
			System.err.println("Selecting free drivers failed.\n" + ex.getSQLState());
		} finally {
			endConnection(c);
		}

		return result;
	}

	/** Getting all free hostess in time current */
	public List<Person> getFreeHostess(Course courseData) {
		String query = "select * from HOSTESS h,PERSONS p  where h.HOSTESS_ID not in "
				+ "(select gp.HOSTESS_ID from GRAPHIC_POSITIONS gp where gp.HOSTESS_ID is not null and "
				+ "? between gp.TIME_FROM and gp.TIME_TO and "
				+ "? between gp.TIME_FROM and gp.TIME_TO ) AND h.PERSON_ID=p.PERSON_ID";
		List<Person> result = new ArrayList<>();
		
		Calendar dateLeaving = courseData.getDate();
		Calendar dateArriving = courseData.getDate();
		dateArriving.add(Calendar.MINUTE, courseData.getCourseDrive().getTime());
		Connection c = null;
		try {
			c = createConnection();
			PreparedStatement statement = c.prepareStatement(query);
			int counter = 1;
			statement.setDate(counter++, new Date(dateLeaving.getTimeInMillis()));
			statement.setDate(counter++, new Date(dateArriving.getTimeInMillis()));
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				Person person = new Person(resultSet.getInt(PersonConsts.ID), resultSet.getString(PersonConsts.NAME),
						resultSet.getString(PersonConsts.SURNAME));
				result.add(person);
			}
		} catch (SQLException ex) {
			System.err.println("Selecting free hostess failed.\n" + ex.getSQLState());
		} finally {
			endConnection(c);
		}

		return result;
	}

	public void insertCourse(Course courseData, List<Person> staff) {
		String query = "insert into COURSES(COURSE_ID,TIME_TABLE_POSITION_ID,BUS_ID,COURSE_DATE,STATUS,BUSY_SITS) "
				+ "values(COURSE_SEQ.NEXTVAL,?,?,?,'EMPTY',0)";
		Connection c = null;
		try {
			c = createConnection();
			PreparedStatement statement = c.prepareStatement(query);
			int counter = 1;
			statement.setInt(counter++, courseData.getTimeTablePosition().getId());
			statement.setInt(counter++, courseData.getBus().getBusId());
			statement.setDate(counter++, new Date(courseData.getDate().getTimeInMillis()));
			statement.executeUpdate();

			Statement stmt= c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COURSE_SEQ.CURRVAL FROM dual");
			long generatedId = -1;
			if ( rs!=null && rs.next() )
				generatedId =  	rs.getInt(1);
			for(Person person : staff)
				insertStaffForCourse(generatedId,person.getId());
		} catch (SQLException ex) {
			System.err.println("Inserting course failed.\n" + ex.getSQLState());
		} finally {
			endConnection(c);
		}

	}

	private void insertStaffForCourse(long courseId, int staffMemberId) {
		String query = "insert into STAFF(COURSE_ID,PERSON_ID) "
				+ "values("+courseId+","+staffMemberId+")";
		Connection c = null;
		try {
			c = createConnection();
			Statement statement = c.createStatement();
			statement.executeQuery(query);
		}catch (SQLException ex) {
			System.err.println("Inserting staff failed.\n" + ex.getSQLState());
		} finally {
			endConnection(c);
		}
	}
}
