package coffeeApplet;

import javax.swing.table.*;
import java.sql.SQLException;
import java.io.*;
import java.net.*;
import java.time.*;
import java.time.format.*;

public class CoffeeAppletDBManager
{
	

	private CoffeeDBDataOut dataInOutServlet(CoffeeDBDataIn coffeeDBDataIn)
	{
		CoffeeDBDataOut coffeeDBDataOut = null;
		try
		{
			URL url = new URL("http://54.174.169.58:8080/coffeeshop/CoffeeShopServer");
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setRequestMethod("POST");

			ObjectOutputStream outStream = new ObjectOutputStream(urlConnection.getOutputStream());

			outStream.writeObject(coffeeDBDataIn);
			outStream.close();


			ObjectInputStream inStream = new ObjectInputStream(urlConnection.getInputStream());
			coffeeDBDataOut = (CoffeeDBDataOut) inStream.readObject(); 
			inStream.close();

		}catch(IOException e){
			System.out.println("IOException");
			e.printStackTrace();
		}catch(ClassNotFoundException ex){
			System.out.println("ClassNotFoundException");
			ex.printStackTrace();
		}
		if(coffeeDBDataOut != null && coffeeDBDataOut.getSuccess()){
			System.out.println("Success");
		}else{
			System.out.println("Not Success!!!!!");
			System.out.println(coffeeDBDataOut == null?"NULL":coffeeDBDataOut.getException().getMessage());
		}
		return coffeeDBDataOut;
	}


	/**
		The getCustomersData method will return DefaultTableModel object containing 
		customers table
	*/

	public DefaultTableModel getCustomersData()
	{
		DefaultTableModel customersData = null;
		CoffeeDBDataIn coffeeDBDataIn = new CoffeeDBDataIn("Select customerID, Name,Balance from Customers",DBCommandType.QUERY);
		CoffeeDBDataOut coffeeDBDataOut = dataInOutServlet(coffeeDBDataIn);

		return coffeeDBDataOut.getTableModelData();
	}




	/**
		The coffeesData method will return DfaultTableModel object containing 
		coffees table
	*/

	public DefaultTableModel getCoffeesData() throws SQLException
	{
		DefaultTableModel coffeesData = null;
		CoffeeDBDataIn coffeeDBDataIn = new CoffeeDBDataIn("Select * from Coffees",DBCommandType.QUERY);
		CoffeeDBDataOut coffeeDBDataOut = dataInOutServlet(coffeeDBDataIn);
		//Need to handle Errors!!!!
		return coffeeDBDataOut.getTableModelData();
	}

	/**
		The ordersData method will return DefaultTableModel object containing 
		orders table
	*/

	public DefaultTableModel getOrdersData() throws SQLException
	{
		DefaultTableModel ordersData = null;
		CoffeeDBDataIn coffeeDBDataIn = new CoffeeDBDataIn("Select * from Orders",DBCommandType.QUERY);
		CoffeeDBDataOut coffeeDBDataOut = dataInOutServlet(coffeeDBDataIn);
		//need to handle Errors!!!!
		return coffeeDBDataOut.getTableModelData();
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
	
	 	String sqlString = "INSERT INTO Orders(customerID,coffeeID,quantity,cost,orderDate)" + 
	 					"VALUES("+customerID+","+coffeeID+","+quantity+","+cost+", '"+ todaysDate.format(formatter)+"')";

	 	CoffeeDBDataIn coffeeDBDataIn = new CoffeeDBDataIn(sqlString,DBCommandType.INSERT);
	 	CoffeeDBDataOut coffeeDBDataOut = dataInOutServlet(coffeeDBDataIn);

	 }

	 public void addCustomer(String name, String address, String city, String state, String zip) throws SQLException
	 {
	 	String sqlString = "INSERT INTO Customers(Name, Balance) VALUES ('"+name+"',0.0)";
	 	System.out.println("SQL:"+sqlString);
	 	CoffeeDBDataIn coffeeDBDataIn = new CoffeeDBDataIn(sqlString,DBCommandType.INSERT);
	 	CoffeeDBDataOut coffeeDBDataOut = dataInOutServlet(coffeeDBDataIn);
		
	 }

	 public void addCoffee(String prodNum, String coffeeDescription, double price) throws SQLException
	 {
	 	
	 	String sqlString = "INSERT INTO Coffees(ProdNum, Description, Price) VALUES('"+prodNum+"', '"+coffeeDescription+"',"+price+")";
	 	CoffeeDBDataIn coffeeDBDataIn = new CoffeeDBDataIn(sqlString,DBCommandType.INSERT);
	 	CoffeeDBDataOut coffeeDBDataOut = dataInOutServlet(coffeeDBDataIn);
	 	
	 }
}