package coffeeApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.sql.*;

public class AddNewCustomerFrame extends JFrame implements ActionListener
{
	JLabel nameLabel;
	JButton submitButton;
	JButton cancelButton;
	JTextField nameTextField;

	public AddNewCustomerFrame()
	{
		setSize(new Dimension(300,150));
		JPanel addCustomerPanel = new JPanel();
		addCustomerPanel.setVisible(true);
		nameLabel = new JLabel("Enter your name: ");
		nameTextField = new JTextField();
		nameTextField.setPreferredSize(new Dimension(150,30));
		submitButton = new JButton("Submit");
		submitButton.addActionListener(this);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		addCustomerPanel.add(nameLabel);
		addCustomerPanel.add(nameTextField);
		addCustomerPanel.add(submitButton);
		addCustomerPanel.add(cancelButton);
		add(addCustomerPanel);
		setVisible(true);
	}
	public void actionPerformed(ActionEvent e)
	{
		String submitButtonText = ((JButton)e.getSource()).getText();
		if(submitButtonText.equals("Cancel")){
			this.dispose();
		}else{
			try{
				CoffeeShopModel coffeeShopModel = new CoffeeShopModel();
				String name = nameTextField.getText();
				coffeeShopModel.addCustomer(name,"","","","");
				System.out.println("Added succeesfully!!!!!");

			}catch(SQLException ex){
				System.out.println("Added not succeesfully->" + ex.getMessage());
			}
			this.dispose();

		}
	}
}