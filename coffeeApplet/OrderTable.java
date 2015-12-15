package coffeeApplet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.text.DecimalFormat;

public class OrderTable extends JPanel implements ActionListener
{
	private String customerName;
	private String coffeeName;

	private JLabel chooseCustomerTextLabel;
	private JLabel customerNameLabel;
	private JLabel chooseCoffeeTextLabel;
	private JLabel coffeeNameLabel;

	private JLabel chooseQuantityCoffeesTextLabel;
	private JLabel coffeeQuantityLabel;

	private JButton addQuantityButton;
	private JButton subtractQuantyButton;

	private JLabel orderSubTotalTextLabel;
	private JLabel orderSubTotalNumLabel;

	private CoffeeQuantityListener coffeeQuantityListener;


	public OrderTable(CoffeeQuantityListener coffeeQuantityListener)
	{
		customerName = "";
		coffeeName = "";
		this.coffeeQuantityListener = coffeeQuantityListener;
		buildOrderTable();

	}

	public void setOrderSubTotalNumLabel(double subTotal)
	{
		DecimalFormat df = new DecimalFormat("0.00");

		orderSubTotalNumLabel.setText("$"+df.format(subTotal));
	}

	public void setCoffeeName(String coffeeName)
	{
		this.coffeeName = coffeeName;
		coffeeNameLabel.setText(coffeeName);
	}

	public void setCustomerName(String customerName)
	{
		this.customerName = customerName;
		customerNameLabel.setText(customerName);
	}

	private void buildOrderTable()
	{
		setLayout(new GridLayout(4,2));

		chooseCustomerTextLabel = new JLabel("Choose a Customer:");
		add(chooseCustomerTextLabel);

		customerNameLabel = new JLabel(customerName,SwingConstants.CENTER);
		add(customerNameLabel);

		
		//JPanel coffeesPanel = new JPanel(new GridLayout(2,1));
		//coffeeNameLabel = new JLabel(coffeeName);
		
		chooseCoffeeTextLabel = new JLabel("Choose Coffee:");
		add(chooseCoffeeTextLabel);

		coffeeNameLabel = new JLabel("",SwingConstants.CENTER);
		add(coffeeNameLabel);
	
		chooseQuantityCoffeesTextLabel = new JLabel("Choose number of cups: ");
		add(chooseQuantityCoffeesTextLabel);

		JPanel quantityPanel = new JPanel(new GridLayout(1,3));
		coffeeQuantityLabel = new JLabel("0",SwingConstants.CENTER);
		


		addQuantityButton = new JButton("+");
		addQuantityButton.addActionListener(this);
		subtractQuantyButton = new JButton("-");
		subtractQuantyButton.addActionListener(this);
		quantityPanel.add(addQuantityButton);
		quantityPanel.add(coffeeQuantityLabel);
		quantityPanel.add(subtractQuantyButton);	
		add(quantityPanel);

		orderSubTotalTextLabel = new JLabel("Subtotal:");
		add(orderSubTotalTextLabel);

		orderSubTotalNumLabel = new JLabel("$0.00",SwingConstants.CENTER);
		add(orderSubTotalNumLabel);

	}

	public void setCoffeeQuantityLabel(int numOfCoffees)
	{
		coffeeQuantityLabel.setText(""+numOfCoffees);
	}

	public void actionPerformed(ActionEvent e)
	{
		String buttonText = ((JButton)e.getSource()).getText();
		if(buttonText.equals("+")){
			coffeeQuantityListener.addCoffeeQuantity();
		}else if(buttonText.equals("-")){
			coffeeQuantityListener.subtractCoffeeQuantity();
		}
	}

}