package coffeeApplet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class CoffeesTable extends JPanel
{
	private final int COFFEE_NAMES_ROW = 2;
	private final int COFFEE_PRICE_ROW = 3;
	private final int COFFEES_GRID_COLUMNS = 3;
	private ActionListener actionListener;

	DefaultTableModel coffeesData;
	
	private JButton[] coffeesButtons;


	public CoffeesTable(DefaultTableModel coffeesData, ActionListener actionListener)
	{
		this.coffeesData = coffeesData;
		this.actionListener = actionListener;
		buildCoffeesTable();

	}

	public void setCoffeesData(DefaultTableModel coffeesData)
	{
		removeAllCoffeeButtons();
		this.coffeesData = coffeesData;
		loadCoffeesTable();
	}

	private void removeAllCoffeeButtons()
	{
		removeAll();
		revalidate();
		repaint();
	}
	private void loadCoffeesTable()
	{
		int numOfCoffees = coffeesData.getRowCount();
		coffeesButtons = new JButton[numOfCoffees];
		for(int i = 0;i < numOfCoffees;i++){
			coffeesButtons[i] = new JButton("<html><center>"+(String)coffeesData.getValueAt(i,COFFEE_NAMES_ROW)+"<br>$"+coffeesData.getValueAt(i,COFFEE_PRICE_ROW)+"</center></html>");
			coffeesButtons[i].addActionListener(actionListener);
			add(coffeesButtons[i]);
		}

	}
	private void buildCoffeesTable()
	{
		setLayout(new GridLayout(0, COFFEES_GRID_COLUMNS));
		loadCoffeesTable();
	}

}