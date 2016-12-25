package sample.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import consts.DriveConsts;
import sample.model.Drive;

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
		String query = "SELECT * FROM DRIVES";
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
						resultSet.getString(DriveConsts.CITY_TO));
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
}
