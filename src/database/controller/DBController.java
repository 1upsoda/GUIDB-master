package database.controller;

import java.sql.*;

import javax.swing.JOptionPane;

import database.model.QueryInfo;

public class DBController
{
	/**
	 * the string that connects the database info to this program
	 */
	private String connectionString;
	/**
	 * an object that connects the database itself to the program
	 */
	private Connection databaseConnection;
	/**
	 * allows the transfer of info between the database and the GUI
	 *
	 */
	private DBAppController baseController;
	private String currentQuery;
	private long queryTime;
	

	/**
	 * sets up the controller and all of the methods,
	 * 
	 * @param baseController
	 */
	public DBController(DBAppController baseController)
	{
		this.baseController = baseController;
//		this.connectionString = "jdbc:mysql://10.228.5.160/book_reading?user=t.parsons&password=pars901";
		this.connectionString = "jdbc:mysql://localhost/information_schema?user=root";
		queryTime = 0;
		checkDriver();
		setupConnection();
	}
	
	
	public void connectionStringBuilder(String pathToDBServer, String databaseName, String userName, String password)
	{
		connectionString = "jdbc:mysql://";
		connectionString += pathToDBServer;
		connectionString += "/" + databaseName;
		connectionString += "?user=" + userName;
		connectionString += "&password" + password;
	}

	/**
	 * makes sure the driver doesn't crash anything
	 */
	private void checkDriver()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch (Exception currentException)
		{
			displayErrors(currentException);
			System.exit(1);
		}

	}

	public void submitUpdateQuery(String query)
	{
		this.currentQuery = query;
		long startTime = System.currentTimeMillis();
		long endTime = 0;
		try
		{
			Statement updateStatement = databaseConnection.createStatement();
			updateStatement.executeUpdate(query);
			endTime = System.currentTimeMillis();
		}
		catch(SQLException currentError)
		{
			endTime = System.currentTimeMillis();
			displayErrors(currentError);
		}
		baseController.getQueryList().add(new QueryInfo(query, endTime - startTime));
	}
	/**
	 * makes sure nothing crashes it when you want to close the database
	 */
	public void closeConnection()
	{
		try
		{
			databaseConnection.close();
		}
		catch (SQLException currentException)
		{
			displayErrors(currentException);
		}
	}

	/**
	 * makes sure it doesn't crash when setting up the connection string
	 */
	private void setupConnection()
	{
		try
		{
			databaseConnection = DriverManager.getConnection(connectionString);
		}
		catch (SQLException currentException)
		{
			displayErrors(currentException);
		}
	}
	/**
	 * makes sure query isn't going to mess stuff up
	 * @return true if it will delete stuff
	 */
	private boolean checkQueryForDataViolation()
	{
		if(currentQuery.toUpperCase().contains(" DROP ") 
				|| currentQuery.toUpperCase().contains(" TRUNCATE ") 
				|| currentQuery.toUpperCase().contains(" SET ") 
				|| currentQuery.toUpperCase().contains(" ALTER "))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * allows any query to be used
	 * @param query
	 * @return
	 */
	public String [][] selectQueryResults(String query)
	{
		String[][] results;
		long startTime, endTime;
		startTime = System.currentTimeMillis();
		this.currentQuery = query;
		try
		{
			if(checkQueryForDataViolation())
			{
				throw new SQLException("There was an attempt at a data violation",
						" you dont get to mess the data up here :D", Integer.MIN_VALUE);
			}
			// sets up the first statement//
			Statement firstStatement = databaseConnection.createStatement();
			// actually gets the statement stuff back//
			ResultSet answers = firstStatement.executeQuery(query);
			int columnCount = answers.getMetaData().getColumnCount();
			
			// puts the cursor at the end to get the length of the array//
			answers.last();
			int numberOfRows = answers.getRow();
			// puts the cursor back at the beginning to go through the loop
			answers.beforeFirst();
			// pretty much converts back into a 1d array for right now, but with
			// the capability of being a 2d//
			results = new String[numberOfRows][columnCount];

			// while there is stuff still in the answers list keep doing stuff//
			while (answers.next())
			{
				// get string removes the string and puts it elsewhere, so
				// getstring(1) will get the
				// first thing, which makes the second string the first one...
				for(int col = 0; col<columnCount; col++)
				{
					results[answers.getRow() - 1][col] = answers.getString(col+1);
				}
			}
			
			answers.close();
			firstStatement.close();
			endTime = System.currentTimeMillis();
		}
		catch (SQLException currentException)
		{
			endTime = System.currentTimeMillis();
			results = new String[][] { 	{ "Query unsuccessful" }, 
										{"Maybe use a better query string?"}, 
										{currentException.getMessage()} };
			displayErrors(currentException);

		}
		queryTime = endTime-startTime;
		baseController.getQueryList().add(new QueryInfo(currentQuery, queryTime));
		return results;
	}

	/**
	 * gets the titles of the columns from the databases
	 * 
	 * @return
	 */
	public String [][] realResults()
	{
		String[][] results;
		currentQuery = "SELECT * FROM `games`";
		long startTime, endTime;
		startTime = System.currentTimeMillis();
		try
		{
			// sets up the first statement//
			Statement firstStatement = databaseConnection.createStatement();
			// actually gets the statement stuff back//
			ResultSet answers = firstStatement.executeQuery(currentQuery);
			int columnCount = answers.getMetaData().getColumnCount();
			
			// puts the cursor at the end to get the length of the array//
			answers.last();
			int numberOfRows = answers.getRow();
			// puts the cursor back at the beginning to go through the loop
			answers.beforeFirst();
			// pretty much converts back into a 1d array for right now, but with
			// the capability of being a 2d//
			results = new String[numberOfRows][columnCount];

			// while there is stuff still in the answers list keep doing stuff//
			while (answers.next())
			{
				// get string removes the string and puts it elsewhere, so
				// getstring(1) will get the
				// first thing, which makes the second string the first one...
				for(int col = 0; col<columnCount; col++)
				{
					results[answers.getRow() - 1][col] = answers.getString(col+1);
				}
			}

			answers.close();
			firstStatement.close();
			endTime = System.currentTimeMillis();
		}
		catch (SQLException currentException)
		{
			endTime = System.currentTimeMillis();
			results = new String[][] { { "empty" } };
			displayErrors(currentException);

		}
		queryTime = endTime-startTime;
		baseController.getQueryList().add(new QueryInfo(currentQuery, queryTime));
		return results;
	}
	/**
	 * a main way to just get the info from the innodb sys columns mostly to show how the panel will work
	 * @return
	 */
	public String[] getMetaDataTitles()
	{
		String[] columns;
		currentQuery = "SELECT * FROM `games`";

		long startTime, endTime;
		startTime = System.currentTimeMillis();
		try
		{
			Statement firstStatement = databaseConnection.createStatement();
			ResultSet answers = firstStatement.executeQuery(currentQuery);

			ResultSetMetaData answerData = answers.getMetaData();
			columns = new String[answerData.getColumnCount()];

			// sets the name of each of the objects in the one column to the
			// name of the next table//
			for (int column = 0; column < answerData.getColumnCount(); column++)
			{
				columns[column] = answerData.getColumnName(column + 1);
			}

			answers.close();
			firstStatement.close();
			endTime = System.currentTimeMillis();
		}
		catch (SQLException currentException)
		{
			endTime = System.currentTimeMillis();
			columns = new String[] { "empty" };
			displayErrors(currentException);

		}
		queryTime = endTime-startTime;
		baseController.getQueryList().add(new QueryInfo(currentQuery, queryTime));
		return columns;
	}
	public String[] getDatabaseColumnNames(String tables)
	{
		String[] columns;
		currentQuery = "SELECT * FROM `" + tables + "`";

		long startTime, endTime;
		startTime = System.currentTimeMillis();
		try
		{
			Statement firstStatement = databaseConnection.createStatement();
			ResultSet answers = firstStatement.executeQuery(currentQuery);

			ResultSetMetaData answerData = answers.getMetaData();
			columns = new String[answerData.getColumnCount()];

			// sets the name of each of the objects in the one column to the
			// name of the next table//
			for (int column = 0; column < answerData.getColumnCount(); column++)
			{
				columns[column] = answerData.getColumnName(column + 1);
			}

			answers.close();
			firstStatement.close();
			endTime = System.currentTimeMillis();
		}
		catch (SQLException currentException)
		{
			endTime = System.currentTimeMillis();
			columns = new String[] { "empty" };
			displayErrors(currentException);

		}
		queryTime = endTime-startTime;
		baseController.getQueryList().add(new QueryInfo(currentQuery, queryTime));
		return columns;
	}
	/**
	 * allows you to drop stuff, but not drop the entire database itself, so you cant f everything up
	 * @param query
	 */
	public void dropStatement(String query)
	{
		currentQuery = query;
		String results;
		long startTime, endTime;
		startTime = System.currentTimeMillis();
		try
		{
			if(checkQueryForDataViolation())
			{
				throw new SQLException("You can't do the drops to DB", "d", Integer.MIN_VALUE);
				
			}
			if(currentQuery.toUpperCase().contains(" INDEX "))
			{
				results = "The index was ";
			}
			else
			{
				results = "The table was ";
			}
			
			Statement dropStatement = databaseConnection.createStatement();
			int affected = dropStatement.executeUpdate(currentQuery);
			
			dropStatement.close();
			
			if(affected == 0)
			{
				results += "dropped";
			}
			JOptionPane.showMessageDialog(baseController.getBaseFrame(), results);
			endTime = System.currentTimeMillis();
		}
		catch(SQLException dropError)
		{
			endTime = System.currentTimeMillis();
			displayErrors(dropError);
		}
		queryTime = endTime-startTime;
		baseController.getQueryList().add(new QueryInfo(currentQuery, queryTime));
	}

	/**
	 * shows the info from the database in a better format (translates into a 2D
	 * array) so it can be displayed on the panel
	 * 
	 * @return
	 */
	public String[][] testResults()
	{
		String[][] results;
		currentQuery = "SHOW TABLES";
		long startTime, endTime;
		startTime = System.currentTimeMillis();
		try
		{
			// sets up the first statement//
			Statement firstStatement = databaseConnection.createStatement();
			// actually gets the statement stuff back//
			ResultSet answers = firstStatement.executeQuery(currentQuery);

			// puts the cursor at the end to get the length of the array//
			answers.last();
			int numberOfRows = answers.getRow();
			// puts the cursor back at the beginning to go through the loop
			answers.beforeFirst();
			// pretty much converts back into a 1d array for right now, but with
			// the capability of being a 2d//
			results = new String[numberOfRows][1];

			// while there is stuff still in the answers list keep doing stuff//
			while (answers.next())
			{
				// get string removes the string and puts it elsewhere, so
				// getstring(1) will get the
				// first thing, which makes the second string the first one...
				results[answers.getRow() - 1][0] = answers.getString(1);
			}

			answers.close();
			firstStatement.close();
			endTime = System.currentTimeMillis();
		}
		catch (SQLException currentException)
		{
			endTime = System.currentTimeMillis();
			results = new String[][] { { "empty" } };
			displayErrors(currentException);

		}
		queryTime = endTime-startTime;
		baseController.getQueryList().add(new QueryInfo(currentQuery, queryTime));
		return results;
	}

	/**
	 * shows the info from a database
	 * 
	 * @return all of the tables that you are accessing
	 */
	public String displayTables()
	{
		String tableNames = "";
		currentQuery = "SHOW TABLES";
		long startTime, endTime;
		startTime = System.currentTimeMillis();
		try
		{
			// sets up the first statement//
			Statement firstStatement = databaseConnection.createStatement();
			// actually gets the statement stuff back//
			ResultSet answers = firstStatement.executeQuery(currentQuery);
			// while there is stuff still in the answers list keep doing stuff//
			while (answers.next())
			{
				// get string removes the string and puts it elsewhere, so
				// getstring(1) will get the first thing, which makes the second
				// string the first one...//
				tableNames += answers.getString(1) + "\n";
			}
			answers.close();
			firstStatement.close();
			endTime = System.currentTimeMillis();

		}
		catch (SQLException currentError)
		{
			endTime = System.currentTimeMillis();
			displayErrors(currentError);
		}
		queryTime = endTime-startTime;
		baseController.getQueryList().add(new QueryInfo(currentQuery, queryTime));
		return tableNames;
	}

	/**
	 * the method to call when an exception pops up, can be used for all
	 * exceptions
	 * 
	 * @param currentException
	 */
	public void displayErrors(Exception currentException)
	{
		JOptionPane.showMessageDialog(baseController.getBaseFrame(), "Exception: " + currentException.getMessage());
		if (currentException instanceof SQLException)
		{
			JOptionPane.showMessageDialog(baseController.getBaseFrame(), "SQL State: " + ((SQLException) currentException).getSQLState());
			JOptionPane.showMessageDialog(baseController.getBaseFrame(), "SQL Error Code: " + ((SQLException) currentException).getErrorCode());
		}

	}

	/**
	 * a way to add info to the database, currently only adds the specific
	 * things i say...
	 * **** WILL ADD PARAMETERS SO THAT YOU CAN ADD WHAT YOU WANT LATER****
	 * @return
	 */

	public int insertSample()
	{
		int rowsAffected = -1;
		String query = "INSERT INTO `game_info_database`.`user_info` " + "(`id`, `username`, `password`, `email`, `alignment`) " + "VALUES (NULL, 'Loller', 'Lolz', 'lol@lol.com', 3);";
		long startTime, endTime;
		startTime = System.currentTimeMillis();
		try
		{
			Statement insertStatement = databaseConnection.createStatement();
			rowsAffected = insertStatement.executeUpdate(query);
			insertStatement.close();
			endTime = System.currentTimeMillis();
		}
		catch (SQLException currentError)
		{
			endTime = System.currentTimeMillis();
			displayErrors(currentError);
		}
		queryTime = endTime-startTime;
		baseController.getQueryList().add(new QueryInfo(currentQuery, queryTime));
		return rowsAffected;
	}

	public String getQuery()
	{
		return currentQuery;
	}

	public void setQuery(String query)
	{
		this.currentQuery = query;
	}


	

}
