package coffeeApp;

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.awt.geom.Rectangle2D;

public class CoffeeShop extends JFrame
{
	static SplashScreen mySplash;                   // instantiated by JVM we use it to get    graphics
	static Graphics2D splashGraphics;               // graphics context for overlay of the splash image
	static Rectangle2D.Double splashTextArea;       // area where we draw the text
	static Rectangle2D.Double splashProgressArea;   // area where we draw the progress bar
	static Font font;                               // used to draw our text
	static Rectangle2D.Double titleTextArea;

	private CustomerTable customerTable;
	private CoffeesTable coffeesTable;
	private OrderTable ordersTable;
	
	CoffeeShopModel coffeeShopModel;
	/**
		Constructor
	*/

	private final int WIDTH = 800;
	private final int HEIGHT = 1200;

	public CoffeeShop()
	{
		try{
			
			coffeeShopModel = new CoffeeShopModel();

			System.out.println("Connected to the database");
			setTitle("Yan's Coffee Shop");
			setLayout(new BorderLayout());
			setSize(WIDTH, HEIGHT);
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
			ImageIcon coffeeIcon = null;
			URL url = null;
			try{
				url = (new java.io.File("images/coffee.gif")).toURI().toURL();
				coffeeIcon = new ImageIcon(url);
			}catch (MalformedURLException e){
				System.out.println("Error");
			}

			picLabel.setIcon(coffeeIcon);
			picLabel1.setIcon(coffeeIcon);
			titlePanel.add(picLabel,BorderLayout.WEST);
			titlePanel.add(picLabel1,BorderLayout.EAST);
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
			pack();
			setLocationRelativeTo(null);
			setVisible(true);

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
                		pack();
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

	public static void main(String[] args) 
	{		
		splashInit();           // initialize splash overlay drawing parameters
		appInit();
		new CoffeeShop();
	
		
	}

	/**
     * Prepare the global variables for the other splash functions
     */
    private static void splashInit()
    {
        mySplash = SplashScreen.getSplashScreen();
        if (mySplash != null)
        {   // if there are any problems displaying the splash this will be null
            Dimension ssDim = mySplash.getSize();
            int height = ssDim.height;
            int width = ssDim.width;
            // stake out some area for our status information
            splashTextArea = new Rectangle2D.Double(15., height*0.85, width * .50, 32.);
            splashProgressArea = new Rectangle2D.Double(width * .55, height*.89, width*.44, 12);
            titleTextArea = new Rectangle2D.Double(10, height*.1,width*.5,height*.5);
            // create the Graphics environment for drawing status info
            splashGraphics = mySplash.createGraphics();
            font = new Font("Dialog", Font.PLAIN, 14);
            splashGraphics.setFont(font);
            splashGraphics.setBackground(Color.BLACK);
            
            // initialize the status info
            //splashText("Starting");
            titleText("Yan's Coffee Shop");
            splashProgress(0);
        }
    }

    /**
    	Display a (very) basic progress bar
     	@param pct how much of the progress bar to display 0-100
     */
    public static void splashProgress(int pct)
    {
        if (mySplash != null && mySplash.isVisible())
        {

            // Note: 3 colors are used here to demonstrate steps
            // erase the old one
            splashGraphics.setPaint(Color.LIGHT_GRAY);
            splashGraphics.fill(splashProgressArea);

            // draw an outline
            splashGraphics.setPaint(Color.BLUE);
            splashGraphics.draw(splashProgressArea);

            // Calculate the width corresponding to the correct percentage
            int x = (int) splashProgressArea.getMinX();
            int y = (int) splashProgressArea.getMinY();
            int wid = (int) splashProgressArea.getWidth();
            int hgt = (int) splashProgressArea.getHeight();

            int doneWidth = Math.round(pct*wid/100.f);
            doneWidth = Math.max(0, Math.min(doneWidth, wid-1));  // limit 0-width

            // fill the done part one pixel smaller than the outline
            splashGraphics.setPaint(Color.GREEN);
            splashGraphics.fillRect(x, y+1, doneWidth, hgt-1);

            // make sure it's displayed
            mySplash.update();
        }
    }


    public static void titleText(String title)
    {
    	if(mySplash != null && mySplash.isVisible())
    	{
    		splashGraphics.setPaint(Color.BLACK);
    		font = new Font("Dialog", Font.PLAIN, 26);
            splashGraphics.setFont(font);
    		splashGraphics.drawString(title, (int)(titleTextArea.getX() + 14),(int)(titleTextArea.getY() + 40));
    		font = new Font("Dialog", Font.PLAIN, 16);
            splashGraphics.setFont(font);
    		splashGraphics.drawString("Version 0.9b", (int)(titleTextArea.getX() + 16),(int)(titleTextArea.getY() + 60));
    		mySplash.update();
    		font = new Font("Dialog", Font.PLAIN, 14);
            splashGraphics.setFont(font);
    	}
    }

    /**
    	Display text in status area of Splash.  Note: no validation it will fit.
     	@param str - text to be displayed
     */
    public static void splashText(String str)
    {
        if (mySplash != null && mySplash.isVisible())
        {   // important to check here so no other methods need to know if there
            // really is a Splash being displayed

            // erase the last status text
            splashGraphics.setPaint(Color.LIGHT_GRAY);
            splashGraphics.fill(splashTextArea);

            // draw the text
            splashGraphics.setPaint(Color.BLACK);
            splashGraphics.drawString(str, (int)(splashTextArea.getX() + 10),(int)(splashTextArea.getY() + 15));

            // make sure it's displayed
            mySplash.update();
        }
    }

	private static void appInit()
    {
        for(int i=1;i<=10;i++)
        {
            int pctDone = i * 10;
            splashText("Loading Database....");
            splashProgress(pctDone);
            try
            {
                Thread.sleep(200);
            }
            catch (InterruptedException ex)
            {
                // ignore it
            }
        }
    }

}