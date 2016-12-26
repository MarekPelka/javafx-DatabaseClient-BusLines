package sample.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import consts.DriveConsts;
import consts.IntermediateDriveConsts;
import javafx.collections.ObservableList;
import sample.model.Drive;
import sample.model.IntermediateDrive;

public class DataBaseHandler {
	/** Packet adsress for JDBC driver */
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
					+ "login=grzegorz\n" + "#your password on database\n" + "password=##TAJNE##";
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
		String query = "select d.DRIVE_ID,d.CITY_FROM,d.CITY_TO, sum(idr.DISTANCE) AS DISTANCE_SUM,"
				+ " sum(idr.PRICE) AS PRICE_SUM, sum(idr.TIME) AS TIME_SUM"
				+ " from DRIVES d,INTERMEDIATE_DRIVES idr"
				+ " where  idr.INTERMEDIATE_DRIVE_ID IN (SELECT INTERMEDIATE_DRIVE_ID "
				+ " FROM DRIVE_CONTENTS WHERE DRIVE_ID=d.DRIVE_ID) group by d.DRIVE_ID,d.CITY_FROM,d.CITY_TO";
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
			System.err.println("Selecting drives failed.\n" + ex.getSQLState());
		}
		finally
		{
			endConnection(c);
		}

		return result;
	}
	
	private List<IntermediateDrive> getAllIntermediateDrives(int id)
	{
		String query = "select * from INTERMEDIATE_DRIVES WHERE INTERMEDIATE_DRIVE_ID IN "+"(select INTERMEDIATE_DRIVE_ID " 
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
			System.err.println("Selecting drives failed.\n" + ex.getSQLState());
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
			System.err.println("Selecting drives failed.\n" + ex.getSQLState());
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
			System.err.println("Selecting drives failed.\n" + ex.getSQLState());
		}
		finally
		{
			endConnection(c);
		}

		return result;
	}
}
