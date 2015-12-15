package coffeeApp;

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

public class CoffeeDBManager
{
	
	private final String DB_URL = "jdbc:derby:/Users/Yan/Programming/CoffeeApp/CoffeeDB";
	
	private Connection databaseConnection;


	/**
		Constructor
	*/

	public CoffeeDBManager() throws SQLException
	{
		//create connection to the database
		DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());`
		databaseConnection = DriverManager.getConnection(DB_URL);
	}

	public CoffeeDBManager(String dbURL) throws SQLException
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
		The getCoffeNames method returns an array of Strings 
		containing all the coffee names.
	*/

	public String[] getCoffeeNames() throws SQLException
	{
		Statement stmt =  databaseConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);


		ResultSet resultSet = stmt.executeQuery("SELECT Description from Coffees");
		resultSet.last();
		int numRows = resultSet.getRow();
		resultSet.first();

		String coffeeNames[]  = new String[numRows];

		for(int index = 0;index < numRows;index++){
			coffeeNames[index] = resultSet.getString("Description");
			resultSet.next();
		}

		stmt.close();
		return coffeeNames;
	}

	/**
		The getProdNum method returns a specific coffee's product number.
		@param coffeeName The specified coffee. 
	*/

	public String getProdNum(String coffeeName) throws SQLException
	{
		String prodNum = "";
		Statement stmt = databaseConnection.createStatement();
		ResultSet resultSet = stmt.executeQuery("SELECT prodNum FROM Coffees WHERE Description = '"+coffeeName+"'");

		if(resultSet.next()){
			prodNum = resultSet.getString("ProdNum");
		}
		stmt.close();
		return prodNum;
	}

	/**
		The getCoffeeName method returns a specific coffee's Description.
		@param coffeeID for specified coffee. 
	*/

	public String getCoffeeDescription(int coffeeID) throws SQLException
	{
		String prodNum = "";
		Statement stmt = databaseConnection.createStatement();
		ResultSet resultSet = stmt.executeQuery("SELECT Description FROM Coffees WHERE coffeeID = "+coffeeID);

		if(resultSet.next()){
			prodNum = resultSet.getString("Description");
		}
		stmt.close();
		return prodNum;
	}

	
	/**
		The getCoffeePrice method returns the price of a coffee
		@param coffeeID The specifed Coffee ID
	*/

	public double getCoffeePrice(int coffeeID) throws SQLException
	{
		double coffeePrice = 0.0;
		Statement stmt = databaseConnection.createStatement();

		ResultSet resultSet = stmt.executeQuery("SELECT Price from coffees where coffeeID="+coffeeID);
		if(resultSet.next()){
			coffeePrice = resultSet.getDouble("Price");
		}
		stmt.close();
		return coffeePrice;
	}

	/**
		The getCustomerNames method return an array of Strings
		containing all the customer names.
	*/

	public String[] getCustomerNames() throws SQLException
	{
		String[] customerNames = null;
		Statement stmt = databaseConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet resultSet = stmt.executeQuery("Select Name from Customers");
		if(resultSet.next()){
			resultSet.last();
			int numRows = resultSet.getRow();
			resultSet.first();
			customerNames = new String[numRows];
			for(int index = 0;index < numRows;index++){
				customerNames[index] = resultSet.getString("Name");
				resultSet.next();
			}
		}
		stmt.close();
		return customerNames;
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

	public DefaultTableModel getCustomersData() throws SQLException
	{
		DefaultTableModel customersData = null;
		Statement stmt = databaseConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet resultSet = stmt.executeQuery("Select * from Customers");
		customersData = buildTableModel(resultSet);
		stmt.close();
		return customersData;
	}




	/**
		The coffeesData method will return DfaultTableModel object containing 
		coffees table
	*/

	public DefaultTableModel getCoffeesData() throws SQLException
	{
		DefaultTableModel coffeesData = null;
		Statement stmt = databaseConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet resultSet = stmt.executeQuery("Select * from Coffees");
		coffeesData = buildTableModel(resultSet);
		stmt.close();
		return coffeesData;
	}

	/**
		The ordersData method will return DefaultTableModel object containing 
		orders table
	*/

	public DefaultTableModel getOrdersData() throws SQLException
	{
		DefaultTableModel ordersData = null;
		Statement stmt = databaseConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet resultSet = stmt.executeQuery("Select * from Orders");
		ordersData = buildTableModel(resultSet);
		stmt.close();
		return ordersData;
	}

	


	/**
		The sumbitOrder method submits an order to the Orders table in the Coffees database.
		@param customerID The customer ID.
		@param coffeeID The Coffee ID.
		@param quantity The Quantity Ordered.
		@param price The product Price.
	*/
	public void submitOrder(int customerID, int coffeeID, int quantity, double price) throws SQLException
	{
		double cost = quantity * price;
		ZonedDateTime todaysDate =  ZonedDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Statement stmt = databaseConnection.createStatement();
		stmt.executeUpdate("INSERT INTO Orders(customerID,coffeeID,quantity,cost,orderDate)" + 
						"VALUES("+customerID+","+coffeeID+","+quantity+","+cost+", '"+ todaysDate.format(formatter)+"')");

	}

	public void addCustomer(String name, String address, String city, String state, String zip) throws SQLException
	{
		Statement stmt = databaseConnection.createStatement();

		stmt.executeUpdate("INSERT INTO Customers(Name, Address, City, State, Zip, Balance) VALUES ('"+name+"', '"+address+"', '"+city+"', '"+state+"', '"+zip+"',0.0)");
		stmt.close();	
		
	}

	public void addCoffee(String prodNum, String coffeeDescription, double price) throws SQLException
	{
		Statement stmt = databaseConnection.createStatement();
		stmt.executeUpdate("INSERT INTO Coffees(ProdNum, Description, Price) VALUES('"+prodNum+"', '"+coffeeDescription+"',"+price+")");
		stmt.close();
	}

}