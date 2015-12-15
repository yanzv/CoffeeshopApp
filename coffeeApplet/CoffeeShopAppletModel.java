package coffeeApplet;

import java.sql.*;
import javax.swing.table.*;
import java.math.BigDecimal;

public class CoffeeShopAppletModel
{
	private CoffeeAppletDBManager coffeeDBManager;
	private DefaultTableModel customersData;
	private DefaultTableModel coffeesData;
	private int coffeeOrdered;
	private int customerOrdered;
	private Order newOrder;


	/**
		Constructor
	*/

	public CoffeeShopAppletModel() throws SQLException
	{

		
		coffeeDBManager = new CoffeeAppletDBManager();
		newOrder = new Order();
		
	}

	/**
		The getCustomersData() method will return DefaultTableModel object containing 
		customers table
	*/

	public DefaultTableModel getCustomersData() throws SQLException
	{
		customersData = coffeeDBManager.getCustomersData();
		return customersData;
	}

	/**
		The getCoffeesData() method will return DefaultTableModel object containing 
		customers table
	*/

	public DefaultTableModel getCoffeesData() throws SQLException
	{	
		coffeesData = coffeeDBManager.getCoffeesData();
		return coffeesData;
	}

	public void addCustomer(String name, String address, String city, String state, String zip) throws SQLException
	{
		coffeeDBManager.addCustomer(name,address,city,state,zip);

	}

	public void addCoffee(String prodNum, String coffeeDescription, double price) throws SQLException
	{
		coffeeDBManager.addCoffee(prodNum,coffeeDescription,price);
	}

	public String getCustomerNameForTableRow(int row)
	{
		return (String)customersData.getValueAt(row,1);
	}

	public void setCustomerToOrder(int row)
	{
		newOrder.customerToOrderRow = row;
	}

	public void setCoffeeToOrder(int row)
	{
		newOrder.coffeeToOrderRow = row;
	}

	public void setCoffeeToOrder(String coffeeName)
	{
		for(int i = 0;i < customersData.getRowCount();i++){
			if(((String)coffeesData.getValueAt(i,2)).equals(coffeeName)){
				newOrder.coffeeToOrderRow = i;

				break;
			}
		}
	}
	public void setCoffeeToOrderQuantity(int quantity)
	{
		newOrder.quantity = quantity;
	}

	public String getCustomerNameToOrder()
	{
		return newOrder.customerToOrderRow == -1?"":(String)customersData.getValueAt(newOrder.customerToOrderRow,1);
	}

	public int getCustomerIDToOrder()
	{
		return newOrder.customerToOrderRow == -1?-1:(int)customersData.getValueAt(newOrder.customerToOrderRow,0);
	}

	public String getCoffeeNameToOrder()
	{
		return newOrder.coffeeToOrderRow == -1?"":(String)coffeesData.getValueAt(newOrder.coffeeToOrderRow,2);
	}

	public int getCoffeeIDToOrder()
	{
		return newOrder.coffeeToOrderRow == -1?-1:(int)coffeesData.getValueAt(newOrder.coffeeToOrderRow,0);
	}

	public double getCoffeePriceToOrder()
	{
		return newOrder.coffeeToOrderRow == -1?0.0:((BigDecimal)coffeesData.getValueAt(newOrder.coffeeToOrderRow,3)).doubleValue();
	}

	public void submitOrder() throws SQLException
	{
		coffeeDBManager.submitOrder(getCustomerIDToOrder(),getCoffeeIDToOrder(),newOrder.quantity,getCoffeePriceToOrder());
		newOrder = new Order();	
	}

	public void addCoffeeQuantityToOrder(int quantity)
	{
		newOrder.quantity +=quantity;
	}

	public void subtractCoffeeQuantityFromOrder(int quantity)
	{
		if(newOrder.quantity-quantity > -1){
			newOrder.quantity -=quantity;
		}
	}

	public int getCoffeeQuantityInOrder()
	{
		return newOrder.quantity;
	}

	public double getOrderSubtotal()
	{
		return getCoffeePriceToOrder() * newOrder.quantity;
	}

	class Order
	{
		private int customerToOrderRow = -1;
		private int coffeeToOrderRow = -1 ;
		private int quantity = 0;
	}
}