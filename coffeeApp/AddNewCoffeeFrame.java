package coffeeApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.sql.*;

public class AddNewCoffeeFrame extends JFrame implements ActionListener
{
	JLabel coffeeDescLabel;
	JLabel productNumLabel;
	JLabel coffeePriceLabel;
	JButton submitButton;
	JButton cancelButton;
	JTextField coffeeDescField;
	JTextField coffeeProductNumField;
	JTextField coffeePriceField;

	CoffeeShopModel coffeeShopModel;

	public AddNewCoffeeFrame()
	{
		setSize(new Dimension(200,300));
		JPanel addCoffeePanel = new JPanel();
		addCoffeePanel.setVisible(true);
		productNumLabel = new JLabel("Enter Product Number: ");
		coffeeProductNumField = new JTextField();
		coffeeProductNumField.setPreferredSize(new Dimension(150,30));
		coffeeDescLabel = new JLabel("Enter Coffee Description: ");
		coffeeDescField = new JTextField();
		coffeeDescField.setPreferredSize(new Dimension(150,30));
		coffeeDescField.setPreferredSize(new Dimension(150,30));
		coffeePriceLabel = new JLabel("Enter Coffee Price: ");
		coffeePriceField = new JTextField();
		coffeePriceField.setPreferredSize(new Dimension(150,30));
		submitButton = new JButton("Submit");
		submitButton.addActionListener(this);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		addCoffeePanel.add(coffeeDescLabel);
		addCoffeePanel.add(coffeeDescField);
		addCoffeePanel.add(productNumLabel);
		addCoffeePanel.add(coffeeProductNumField);
		addCoffeePanel.add(coffeePriceLabel);
		addCoffeePanel.add(coffeePriceField);
		addCoffeePanel.add(submitButton);
		addCoffeePanel.add(cancelButton);
		add(addCoffeePanel);
		setVisible(true);
	}

	public void setCoffeeShopModel(CoffeeShopModel coffeeShopModel)
	{
		this.coffeeShopModel = coffeeShopModel;
	}

	public void actionPerformed(ActionEvent e)
	{
		String submitButtonText = ((JButton)e.getSource()).getText();
		if(submitButtonText.equals("Cancel")){
			this.dispose();
		}else{
			try{
				String description = coffeeDescField.getText();
				String productNum = coffeeProductNumField.getText();
				double coffeePrice = Double.parseDouble(coffeePriceField.getText());
				coffeeShopModel.addCoffee(productNum,description,coffeePrice);
				System.out.println("Coffee Added succeesfully");

			}catch(SQLException ex){
				System.out.println("Coffee Added not succeesfully->" + ex.getMessage());
			}
			this.dispose();

		}
	}
}