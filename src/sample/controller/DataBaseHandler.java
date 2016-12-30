package sample.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Provider;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import consts.BusConsts;
import consts.BusModelConsts;
import consts.DriveConsts;
import consts.IntermediateDriveConsts;
import consts.PersonConsts;
import sample.model.*;

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
	static
	{
		try
		{
			Configuration();
		}
		catch (IOException ex)
		{
			System.err.println("Configuration failed.\n" + ex.getMessage());
		}

		try
		{
			Class.forName(driver);
		}
		catch (ClassNotFoundException ex)
		{
			System.err.println("Registration the JDBC/ODBC driver failed.\n" + ex.getMessage());
		}
	}

	private static void Configuration() throws IOException
	{
		File file = new File("dbconfig.properties");
		if (!file.exists())
		{
			file.createNewFile();
			String defaultData = "#Driver for JDBC\n" + "driver=oracle.jdbc.driver.OracleDriver\n"
					+ "#Data Base adress (default on your localhost on 80 port)\n"
					+ "url=jdbc:oracle:thin:@//localhost:1522/orcl\n" + "#login on your database\n"
					+ "login=xxx\n" + "#your password on database\n" + "password=xxx";
			FileWriter fileWritter = new FileWriter(file.getName(), true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.write(defaultData);
			bufferWritter.close();
		}
		try (FileReader reader = new FileReader("dbconfig.properties"))
		{
			Properties prop = new Properties();
			prop.load(reader);
			driver = prop.getProperty("driver");
			url = prop.getProperty("url");
			login = prop.getProperty("login");
			password = prop.getProperty("password");

		}

	}

	/** Returns connection handler through driver */
	private Connection createConnection() throws SQLException
	{
		return DriverManager.getConnection(url, login, password);
	}

	/** Closing connection with local data base */
	private void endConnection(Connection connection)
	{
		if (connection == null)
		{
			return;
		}
		try
		{
			connection.close();
		}
		catch (SQLException ex)
		{
			System.err.println("Closing connection with local data base failed.\n" + ex.getSQLState());
		}
	}
	
	
	public List<Drive> getAllDrives()
	{
		String query = "select d.DRIVE_ID,d.CITY_FROM,d.CITY_TO, sum(idr.DISTANCE) as DISTANCE_SUM,"
				+ " sum(idr.PRICE) as PRICE_SUM, sum(idr.TIME) as TIME_SUM"
				+ " from DRIVES d,INTERMEDIATE_DRIVES idr"
				+ " where  idr.INTERMEDIATE_DRIVE_ID in (SELECT INTERMEDIATE_DRIVE_ID "
				+ " from DRIVE_CONTENTS where DRIVE_ID=d.DRIVE_ID) group by d.DRIVE_ID,d.CITY_FROM,d.CITY_TO";
		List<Drive> result = new ArrayList<>();
		Connection c = null;
		try
		{
			c = createConnection();
			Statement statement = c.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			
			while (resultSet.next())
			{
				Drive drive = new Drive(resultSet.getInt(DriveConsts.ID),
						resultSet.getString(DriveConsts.CITY_FROM),
						resultSet.getString(DriveConsts.CITY_TO),
						resultSet.getInt("TIME_SUM"),
						resultSet.getInt("DISTANCE_SUM"),
						resultSet.getFloat("PRICE_SUM"),getAllIntermediateDrives(resultSet.getInt(DriveConsts.ID)));
				result.add(drive);
			}
		}
		catch (SQLException ex)
		{
			System.err.println("Selecting drives with intermediate drives data failed.\n" + ex.getSQLState());
		}
		finally
		{
			endConnection(c);
		}

		return result;
	}
	
	public List<Bus> getAllBuses()
	{
		String query = "select * from BUSES join BUS_MODELS on BUSES.BUS_MODEL_ID = BUS_MODELS.BUS_MODEL_ID";
		List<Bus> result = new ArrayList<>();
		Connection c = null;
		try
		{
			c = createConnection();
			Statement statement = c.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			
			while (resultSet.next())
			{
				Bus bus = new Bus(resultSet.getInt(BusConsts.ID),
						resultSet.getInt(BusConsts.MODEL_ID),
						resultSet.getDate(BusConsts.BOUGHT_TIME),
						resultSet.getString(BusConsts.LICENSE_PLATE),
						resultSet.getString(BusConsts.SERIAL_NUMBER),
						resultSet.getInt(BusConsts.SEATS_NUM),
						resultSet.getInt(BusConsts.MILEAGE),
						resultSet.getFloat(BusConsts.CLASS_RATE),
						resultSet.getString(BusConsts.BUS_MODEL_NAME));
				result.add(bus);
			}
		}
		catch (SQLException ex)
		{
			System.err.println("Selecting all buses failed.\n" + ex.getSQLState());
		}
		finally
		{
			endConnection(c);
		}

		return result;
	}
	
	private List<IntermediateDrive> getAllIntermediateDrives(int id)
	{
		String query = "select * from INTERMEDIATE_DRIVES where INTERMEDIATE_DRIVE_ID in "
						+"(select INTERMEDIATE_DRIVE_ID " 
						+"from DRIVE_CONTENTS where DRIVE_ID="+id + ")";	
		List<IntermediateDrive> result = new ArrayList<>();
		Connection c = null;
		try
		{
			c = createConnection();
			Statement statement = c.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			
			while (resultSet.next())
			{
				IntermediateDrive iDrive = new IntermediateDrive(resultSet.getInt(IntermediateDriveConsts.ID),
						resultSet.getString(IntermediateDriveConsts.CITY_FROM),
						resultSet.getString(IntermediateDriveConsts.CITY_TO),
						resultSet.getInt(IntermediateDriveConsts.TIME),
						resultSet.getInt(IntermediateDriveConsts.DISTANCE),
						resultSet.getFloat(IntermediateDriveConsts.PRICE));
				result.add(iDrive);
			}
		}
		catch (SQLException ex)
		{
			System.err.println("Selecting intermediate drives of one drive failed.\n" + ex.getSQLState());
		}
		finally
		{
			endConnection(c);
		}

		return result;
	}

	public List<IntermediateDrive> getIntermediateDrivesData() {
		String query = "select * from INTERMEDIATE_DRIVES";
		List<IntermediateDrive> result = new ArrayList<>();
		Connection c = null;
		try
		{
			c = createConnection();
			Statement statement = c.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next())
			{
				IntermediateDrive iDrive = new IntermediateDrive(resultSet.getInt(IntermediateDriveConsts.ID),
						resultSet.getString(IntermediateDriveConsts.CITY_FROM),
						resultSet.getString(IntermediateDriveConsts.CITY_TO),
						resultSet.getInt(IntermediateDriveConsts.TIME),
						resultSet.getInt(IntermediateDriveConsts.DISTANCE),
						resultSet.getFloat(IntermediateDriveConsts.PRICE));
				result.add(iDrive);
			}
		}
		catch (SQLException ex)
		{
			System.err.println("Selecting intermediate drives data failed.\n" + ex.getSQLState());
		}
		finally
		{
			endConnection(c);
		}

		return result;
	}

	public List<IntermediateDrive> getIntermediateDrivesFromData(String cityFrom) {
		String query = "select * from INTERMEDIATE_DRIVES where CITY_FROM = '" + cityFrom + "'";
		List<IntermediateDrive> result = new ArrayList<>();
		Connection c = null;
		try
		{
			c = createConnection();
			Statement statement = c.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next())
			{
				IntermediateDrive iDrive = new IntermediateDrive(resultSet.getInt(IntermediateDriveConsts.ID),
						resultSet.getString(IntermediateDriveConsts.CITY_FROM),
						resultSet.getString(IntermediateDriveConsts.CITY_TO),
						resultSet.getInt(IntermediateDriveConsts.TIME),
						resultSet.getInt(IntermediateDriveConsts.DISTANCE),
						resultSet.getFloat(IntermediateDriveConsts.PRICE));
				result.add(iDrive);
			}
		}
		catch (SQLException ex)
		{
			System.err.println("Selecting intermediate drvies from data failed.\n" + ex.getSQLState());
		}
		finally
		{
			endConnection(c);
		}

		return result;
	}
	
	public List<Person> getFreeDrivers() {
		String query = "select * from DRIVERS d,PERSONS p  where d.DRIVER_ID not in " 
				+ "(select gp.DRIVER_ID from GRAPHIC_POSITIONS gp where gp.DRIVER_ID is not null and "
				+ "(select SYSDATE from DUAL) between gp.TIME_FROM and gp.TIME_TO) AND d.PERSON_ID=p.PERSON_ID";
		List<Person> result = new ArrayList<>();
		Connection c = null;
		try
		{
			c = createConnection();
			Statement statement = c.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next())
			{
				Person person = new Person(resultSet.getInt(PersonConsts.ID),
						resultSet.getString(PersonConsts.NAME),
						resultSet.getString(PersonConsts.SURNAME));
				result.add(person);
			}
		}
		catch (SQLException ex)
		{
			System.err.println("Selecting intermediate drvies from data failed.\n" + ex.getSQLState());
		}
		finally
		{
			endConnection(c);
		}

		return result;
	}
	
	public List<Person> getFreeHostess() {
		String query = "select * from HOSTESS h,PERSONS p  where h.HOSTESS_ID not in " 
				+ "(select gp.HOSTESS_ID from GRAPHIC_POSITIONS gp where gp.HOSTESS_ID is not null and "
				+ "(select SYSDATE from DUAL) between gp.TIME_FROM and gp.TIME_TO) AND h.PERSON_ID=p.PERSON_ID";
		List<Person> result = new ArrayList<>();
		Connection c = null;
		try
		{
			c = createConnection();
			Statement statement = c.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next())
			{
				Person person = new Person(resultSet.getInt(PersonConsts.ID),
						resultSet.getString(PersonConsts.NAME),
						resultSet.getString(PersonConsts.SURNAME));
				result.add(person);
			}
		}
		catch (SQLException ex)
		{
			System.err.println("Selecting intermediate drvies from data failed.\n" + ex.getSQLState());
		}
		finally
		{
			endConnection(c);
		}

		return result;
	}

	public List<Services> getServices() {
		String query = "select * from SERVICES";
		List<Services> result = new ArrayList<>();
		Connection c = null;
		try
		{
			c = createConnection();
			Statement statement = c.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next())
			{
				Services service = new Services(resultSet.getInt("SERVICE_ID"),
						resultSet.getString("OPERATION"),
						resultSet.getString("MEANINGNESS"));
				result.add(service);
			}
		}
		catch (SQLException ex)
		{
			System.err.println("Selecting services from data failed.\n" + ex.getSQLState());
		}
		finally
		{
			endConnection(c);
		}

		return result;
	}

	public List<BusModel> getAllBusModels()
	{
		String query = "select * from BUS_MODELS";
		List<BusModel> result = new ArrayList<>();
		Connection c = null;
		try
		{
			c = createConnection();
			Statement statement = c.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			while (resultSet.next())
			{
				BusModel busModel = new BusModel(resultSet.getInt(BusModelConsts.ID),
						resultSet.getString(BusModelConsts.MODEL_NAME));
				result.add(busModel);
			}
		}
		catch (SQLException ex)
		{
			System.err.println("Selecting all bus models failed.\n" + ex.getSQLState());
		}
		finally
		{
			endConnection(c);
		}

		return result;
	}
	
	
	/** INSERT */
	public void insertBus(Bus bus) {
		String query = "INSERT INTO " + "BUSES" + "(BUS_ID,BUS_MODEL_ID,BOUGHT_TIME,LICENSE_PLATE,"
				+ "SERIAL_NUMBER,SEATS_NUMBER,MILEAGE,CLASS_RATE) " + "VALUES (BUS_SEQ.NEXTVAL,?,?,?,?,?,?,?)";
		Connection c = null;
		try
		{
			c = createConnection();
			PreparedStatement statement = c.prepareStatement(query);
			int counter = 1;
			statement.setInt(counter++, bus.getBusModelId());
			statement.setDate(counter++, bus.getDateOfBuy());
			statement.setString(counter++, bus.getLicensePlate());
			statement.setString(counter++, bus.getSereialNumber());
			statement.setInt(counter++, bus.getSeats());
			statement.setInt(counter++, bus.getMileage());
			statement.setFloat(counter++, bus.getClassRate());
			statement.executeUpdate();
		}
		catch (SQLException ex)
		{
			System.err.println("Inserting bus failed.\n" + ex.getSQLState());
		}
		finally
		{
			endConnection(c);
		}
	}
	
	public void deleteBus(Bus bus) {
		String query = "delete from BUSES where BUS_ID=?";
		List<BusModel> result = new ArrayList<>();
		Connection c = null;
		try
		{
			c = createConnection();
			PreparedStatement statement = c.prepareStatement(query);
			statement.setInt(1, bus.getBusId());
			statement.executeUpdate();
		}
		catch (SQLException ex)
		{
			System.err.println("Deleting bus failed.\n" + ex.getSQLState());
		}
		finally
		{
			endConnection(c);
		}
	}
}
