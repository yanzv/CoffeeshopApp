package coffeeApplet;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.JApplet;

public class CoffeeShopApplet extends JApplet

{

	private CustomerTable customerTable;
	private CoffeesTable coffeesTable;
	private OrderTable ordersTable;
	CoffeeShopAppletModel coffeeShopModel;
	/**
		Constructor
	*/
	public void init()
	{
		try{
			
			coffeeShopModel = new CoffeeShopAppletModel();

			System.out.println("Connected to the database");
			setLayout(new BorderLayout());
			buildPanels();
			

		}catch(SQLException ex){
			System.out.println("Error Connecting to the database \n" + ex.getMessage());
		}
	}

	/**
		The buildPanels method will setup all the JPanels 
		in the main content pane
	*/ 

	private void buildPanels()
	{
		try{
			JPanel titlePanel  = new JPanel(new BorderLayout());
			JLabel titleLabel = new JLabel("Yan's Coffee Shop",SwingConstants.CENTER);
			titleLabel.setFont(new Font(titleLabel.getName(),Font.PLAIN,25));
			titlePanel.add(titleLabel,BorderLayout.CENTER);
			titlePanel.setPreferredSize(new Dimension(WIDTH,170));
			JLabel picLabel = new JLabel();
			JLabel picLabel1 = new JLabel();
			// ImageIcon coffeeIcon = null;
			// URL url = null;
			// try{
			// 	url = (new java.io.File("images/coffee.gif")).toURI().toURL();
			// 	coffeeIcon = new ImageIcon(url);
			// }catch (MalformedURLException e){
			// 	System.out.println("Error");
			// }

			// picLabel.setIcon(coffeeIcon);
			// picLabel1.setIcon(coffeeIcon);
			// titlePanel.add(picLabel,BorderLayout.WEST);
			// titlePanel.add(picLabel1,BorderLayout.EAST);
			add(titlePanel,BorderLayout.NORTH);

			JPanel customerTablePanel = new JPanel(new BorderLayout());
			customerTable = new CustomerTable(coffeeShopModel.getCustomersData(), new CustomerSelectionListener());
			JButton addCustomerButton = new JButton("Add Customer");
			addCustomerButton.addActionListener(new AddButtonActionListener());
			customerTablePanel.add(addCustomerButton,BorderLayout.SOUTH);
			customerTablePanel.add(customerTable,BorderLayout.CENTER);
			
			add(customerTablePanel,BorderLayout.WEST);

			JPanel coffeesTablePanel = new JPanel(new BorderLayout());
			coffeesTablePanel.setVisible(true);
			coffeesTable = new CoffeesTable(coffeeShopModel.getCoffeesData(),new CoffeesButtonActionListener());
			coffeesTablePanel.add(coffeesTable,BorderLayout.CENTER);
			JButton addCoffeeButton = new JButton("Add Coffee");
			addCoffeeButton.addActionListener(new AddButtonActionListener());
			coffeesTablePanel.add(addCoffeeButton,BorderLayout.SOUTH);
			add(coffeesTablePanel,BorderLayout.CENTER);

			JPanel ordersCoffeesPanel = new JPanel(new BorderLayout());
			ordersCoffeesPanel.setVisible(true);
			ordersTable = new OrderTable(new OrderQuantityListener());
			ordersCoffeesPanel.add(ordersTable,BorderLayout.CENTER);
			JButton submitOrderButton = new JButton("Submit Order");
			submitOrderButton.addActionListener(new AddButtonActionListener());
			ordersCoffeesPanel.add(submitOrderButton,BorderLayout.SOUTH);
			add(ordersCoffeesPanel,BorderLayout.EAST);

			titlePanel.setVisible(true);
			customerTablePanel.setVisible(true);
			ordersCoffeesPanel.setVisible(true);
			setVisible(true);
			validate();

		}catch(SQLException ex){
			System.out.println("Error Loading UI Data"+ex.getMessage());
		}
	}

	public void resetOrderTable()
	{
		ordersTable.setCoffeeName("");
		ordersTable.setCustomerName("");
		ordersTable.setOrderSubTotalNumLabel(0.0);
		ordersTable.setCoffeeQuantityLabel(0);
	}


	class OrderQuantityListener implements CoffeeQuantityListener
	{
		public void addCoffeeQuantity()
		{
			coffeeShopModel.addCoffeeQuantityToOrder(1);
			ordersTable.setCoffeeQuantityLabel(coffeeShopModel.getCoffeeQuantityInOrder());
			ordersTable.setOrderSubTotalNumLabel(coffeeShopModel.getOrderSubtotal());
		}

		public void subtractCoffeeQuantity()
		{
			coffeeShopModel.subtractCoffeeQuantityFromOrder(1);
			ordersTable.setCoffeeQuantityLabel(coffeeShopModel.getCoffeeQuantityInOrder());
			ordersTable.setOrderSubTotalNumLabel(coffeeShopModel.getOrderSubtotal());

		}
	}

	class AddButtonActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String submitButtonText = ((JButton)e.getSource()).getText();
			if(submitButtonText.equals("Add Customer")){
				AddNewCustomerFrame addNewCustomer = new AddNewCustomerFrame();
				addNewCustomer.setTitle("Add Customer");
				addNewCustomer.addWindowListener(new AddButtonWindowListener());	
			}else if(submitButtonText.equals("Add Coffee")){
				AddNewCoffeeFrame addNewCoffeeFrame = new AddNewCoffeeFrame();
				addNewCoffeeFrame.setTitle("Add Coffee");
				addNewCoffeeFrame.setCoffeeShopModel(coffeeShopModel);
				addNewCoffeeFrame.addWindowListener(new AddButtonWindowListener());
			}else if(submitButtonText.equals("Submit Order")){
				try{
					coffeeShopModel.submitOrder();
					customerTable.setCustomerData(coffeeShopModel.getCustomersData());
					resetOrderTable();
				}catch(SQLException ex){
					System.out.println("Can't submit order "+ex.getMessage());
				}
				
			}
		}
	}

	class CoffeesButtonActionListener implements ActionListener
	{
		private String coffeeNameFromButtonText(String buttonText)
		{
			int startPos = buttonText.indexOf("r>")+2;
			int endPos = buttonText.indexOf("<b");
			return buttonText.substring(startPos,endPos);
		}

		public void actionPerformed(ActionEvent e)
		{
			String coffeeName = coffeeNameFromButtonText(((JButton)e.getSource()).getText());
			coffeeShopModel.setCoffeeToOrder(coffeeName);
			ordersTable.setCoffeeName(coffeeShopModel.getCoffeeNameToOrder());	
			ordersTable.setOrderSubTotalNumLabel(coffeeShopModel.getOrderSubtotal());

		}
	}

	class CustomerSelectionListener implements ListSelectionListener
	{
		public void valueChanged (ListSelectionEvent event)
		{

			int selectedRow = customerTable.getIndexForSelectedRow();
			coffeeShopModel.setCustomerToOrder(selectedRow);
			String customerName = coffeeShopModel.getCustomerNameToOrder();
			ordersTable.setCustomerName(customerName);
		}	
	}

	class AddButtonWindowListener implements WindowListener
	{
		public void windowActivated(WindowEvent e) {
                System.out.println("windowActivated");
            }

            public void windowClosed(WindowEvent e) {
            	String closedWindowTitle = ((JFrame)e.getComponent()).getTitle();
                System.out.println("windowClosed " + ((JFrame)e.getComponent()).getTitle());
                try{
                	if(closedWindowTitle.equals("Add Customer")){
                		customerTable.setCustomerData(coffeeShopModel.getCustomersData());
                	}else if(closedWindowTitle.equals("Add Coffee")){
                		coffeesTable.setCoffeesData(coffeeShopModel.getCoffeesData());
                	}
            	}catch(SQLException ex){
            		System.out.println("Error Adding" + ex.getMessage());
            	}
            }

            public void windowClosing(WindowEvent e) {
                System.out.println("windowClosing" );
            }
		
            public void windowDeactivated(WindowEvent e) {
                System.out.println("windowDeactivated");
            }

            public void windowDeiconified(WindowEvent e) {
                System.out.println("windowDeiconified");
            }

            public void windowIconified(WindowEvent e) {
                System.out.println("windowIconified");
            }

            public void windowOpened(WindowEvent e) {
                System.out.println("windowOpened");
            }
	}

}