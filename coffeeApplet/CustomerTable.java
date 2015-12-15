package coffeeApplet;

import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;

public class CustomerTable extends JPanel implements DocumentListener
{
	private DefaultTableModel customerData;
	private JTable customerTable;
	private TableRowSorter<TableModel> rowSorter;
	private JTextField searchCustomerField;
	private JScrollPane customerTableScrollPane;
	private ListSelectionListener tableSelectionListener;
	ListSelectionModel listSelectionModel;

	/**
		Constructor
	*/

	public CustomerTable(DefaultTableModel customerData, ListSelectionListener tableSelectionListener)
	{
	
		this.customerData = customerData;
		this.tableSelectionListener = tableSelectionListener;
		buildCustomerTable();


	}

	public void setTableSelectionListener(ListSelectionListener tableListener)
	{
		this.tableSelectionListener = tableSelectionListener;
	}

	public void setCustomerData(DefaultTableModel customerData)
	{
		this.customerData = customerData;
		loadCustomerTable();

	}

	private void loadCustomerTable()
	{
		customerTable.setModel(customerData);
		rowSorter = new TableRowSorter<>(customerTable.getModel());
        customerTable.setRowSorter(rowSorter);
        listSelectionModel = customerTable.getSelectionModel();
    	listSelectionModel.addListSelectionListener(tableSelectionListener);
    	customerTable.setSelectionModel(listSelectionModel);	
	}

	/**
		The buildCustomerTable method will construct JPanel of customers and will 
		add it to the panel
	*/

	private void buildCustomerTable()
	{
		setLayout(new BorderLayout());
		customerTable = new JTable(customerData){
        	private static final long serialVersionUID = 1L;
        		public boolean isCellEditable(int row, int column) {                
                	return false;               
        		};
        	};
        customerTable.setGridColor(Color.BLACK);
        
        	

		loadCustomerTable();

		customerTableScrollPane = new JScrollPane(customerTable);
		add(customerTableScrollPane,BorderLayout.CENTER);

		setVisible(true);
		searchCustomerField = new JTextField();
		searchCustomerField.setPreferredSize(new Dimension(200,24));
		searchCustomerField.getDocument().addDocumentListener(this);
		searchCustomerField.setVisible(true);
		searchCustomerField.setToolTipText("Search...");
		add(searchCustomerField,BorderLayout.NORTH);

	}


	/** 
		The getIndexForSelectedRow method will return index of the row tha is selected
		or -1 if nothing is selected
	*/

	public int getIndexForSelectedRow()
	{
		return customerTable.getSelectedRow();
	}

	/**
		The searchCustomerTable method will update the table based on the search term in the 
		searchCustomerField
	*/
	private void searchCustomerTable()
	{

		String customerSearchText = searchCustomerField.getText();
		if(customerSearchText.trim().length() == 0){
			rowSorter.setRowFilter(null);
		}else{
			
			rowSorter.setRowFilter(RowFilter.regexFilter("(?i)"+customerSearchText));
		}

	}

	public void changedUpdate(DocumentEvent e)
	{
		//searchCustomerTable();

	}

	public void removeUpdate(DocumentEvent e)
	{
		searchCustomerTable();
		
	}
	public void insertUpdate(DocumentEvent e)
	{	
		searchCustomerTable();
		
	}

}