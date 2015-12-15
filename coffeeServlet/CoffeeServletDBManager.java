package coffeeServlet;
	/*

	ResultSet.TYPE_FORWARD_ONLY This is the default scrolling type.  It specifies that the resulst set's 
	cursor should move forward.

	ResultSet.TYPE_SCROLL_INSENSITIVE Allowing the cursor to move forward and backward through the result set
	In addition, this result set is insentive to changes made to the database.

	ResultSet.TYPE_SCROLL_SENSITIVE Allowing the cursor to move forward and backward through the result set
	In addition, this result set is sensitive to changes made to the database. If another program or process 
	makes changes to the database, those changes will appear in this result set as soon as they are made.

	ResultSet.CONCUR_READ_ONLY  This is the default concurrency level.  It specified that the result set
	contains a read-only version of data from the database.

	ResultSet.CONCUR_UPDATEABLE This specifies that the result set should be updateable.  Changes can be made
	to the result set, and then hose changes can be saved to the database.
*/


import java.sql.*;
import java.time.*;
import javax.swing.table.*;
import java.util.*;
import java.time.format.FormatStyle;
import java.time.format.DateTimeFormatter;	

public class CoffeeServletDBManager
{
	
	private final String DB_URL = "jdbc:derby:/usr/share/tomcat8/webapps/coffeeshop/db/CoffeeDB";
	
	private Connection databaseConnection;


	/**
		Constructor
	*/

	public CoffeeServletDBManager() throws SQLException
	{
		//create connection to the database
		databaseConnection = DriverManager.getConnection(DB_URL);
	}

	public CoffeeServletDBManager(String dbURL) throws SQLException
	{
		DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
		databaseConnection = DriverManager.getConnection(dbURL);
	}

	public boolean closeConnection() throws SQLException
	{
		try{
			databaseConnection.close();

		}catch(SQLException ex){
			throw ex;
		}
		return true;
	}

	
	/**
		The BuildTableModel method returns a DefaultTableModel object to populate a JTable
		@param rs specified ResultSet
	*/
	public DefaultTableModel buildTableModel(ResultSet rs) 
				throws SQLException 
	{

	    ResultSetMetaData metaData = rs.getMetaData();

	    // names of columns
	    Vector<String> columnNames = new Vector<String>();
	    int columnCount = metaData.getColumnCount();
	    for (int column = 1; column <= columnCount; column++) {
	        columnNames.add(metaData.getColumnName(column));
	    }

	    // data of the table
	    Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	    while (rs.next()) {
	        Vector<Object> vector = new Vector<Object>();
	        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
	            vector.add(rs.getObject(columnIndex));
	        }
	        data.add(vector);
    	}
    	return new DefaultTableModel(data, columnNames);

	}
	/**
		The getCustomersData method will return DfaultTableModel object containing 
		customers table
	*/

	public DefaultTableModel getDataFromDB(String sqlStatement) throws SQLException
	{
		DefaultTableModel customersData = null;
		Statement stmt = databaseConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet resultSet = stmt.executeQuery(sqlStatement);
		customersData = buildTableModel(resultSet);
		stmt.close();
		return customersData;
	}

	public void insertDataIntoDB(String sqlStatement) throws SQLException
	{
		Statement stmt = databaseConnection.createStatement();
		stmt.executeUpdate(sqlStatement);
		stmt.close();
	}
}