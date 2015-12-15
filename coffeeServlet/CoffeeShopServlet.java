package coffeeServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.*;
import java.io.*;
import java.io.ObjectInputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class NewHelloWorld
 */
//@WebServlet("/NewHelloWorld")
public class CoffeeShopServlet  extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CoffeeServletDBManager coffeeDBManager;	
	private final String DB_URL = "jdbc:derby:/usr/share/tomcat8/webapps/coffeeshop/db/CoffeeDB";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CoffeeShopServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void init()
    {
    	try{
    		coffeeDBManager = new CoffeeServletDBManager(DB_URL);
    	}catch(SQLException ex){
			System.out.println("Init--->"+ex.getMessage());
		}
    }

    public void destroy()
    {
    	try{
    		coffeeDBManager.closeConnection();
    	}catch(SQLException ex){
			System.err.println("--->"+ex.getMessage());
		}

    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		PrintWriter out = response.getWriter();
		
		out.println("<html><body><h1>Coffee Two</h1>");
		try{
			if(coffeeDBManager == null){
				out.println("coffeeDBManager is null");
			}else{
				DefaultTableModel coffeeNames = coffeeDBManager.getDataFromDB("Select * from Customers");
			//out.println("<html>");
			//out.println("<link rel='stylesheet' type='text/css' href='" + request.getContextPath() +  "/coffeedb.css' />");
			//out.println("<body>");
			//out.println("<h1> <center> Coffee Shop </center> </h1>");
				out.println("<div class=\"rTable\">");
				out.println("<div class=\"rTableRow\">");
				out.println("<div class=\"rTableHead\"><strong>Name</strong></div>");
				out.println("<div class=\"rTableHead\"><span style=\"font-weight: bold;\">Balance</span></div>");
				out.println("</div>");
			
			for(int i = 0; i < coffeeNames.getRowCount();i++){
				out.println("<div class=\"rTableRow\">");
				out.println("<div class=\"rTableCell\">"+coffeeNames.getValueAt(i,1)+"</div>");
				out.println("</div>");
			}
			out.println("</div>");
			//coffeeDBManager.insertDataIntoDB("INSERT INTO Customers (Name, Balance) VALUES ('Darina',0.00)");
			}
		}catch(SQLException ex){
			System.err.println("--->"+ex.getMessage());
			out.println("Connection Error --->");
			out.println(ex.getMessage());
			out.println(ex.getStackTrace());
			out.println(ex.getSQLState());
		}catch(Exception e){
			out.println("Error --->");
			out.println(e.getMessage());
			out.println(e.getStackTrace());
		}
	}	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		CoffeeDBDataIn coffeeDBDataIn = null;

		ObjectInputStream in = new ObjectInputStream(request.getInputStream());
		try{
			coffeeDBDataIn = (CoffeeDBDataIn) in.readObject();
		}catch(ClassNotFoundException ex){
			ex.printStackTrace();
		}
		in.close();

		CoffeeDBDataOut coffeeDBDataOut = new CoffeeDBDataOut(null,null);
		coffeeDBDataOut.setSuccess(false);

		try{
			if(coffeeDBDataIn.getCommandType() == DBCommandType.QUERY){
				DefaultTableModel dt = coffeeDBManager.getDataFromDB(coffeeDBDataIn.getSqlQuery());
				coffeeDBDataOut = new CoffeeDBDataOut(dt,null);
				coffeeDBDataOut.setSuccess(true);
			}else if(coffeeDBDataIn.getCommandType() == DBCommandType.INSERT){
				coffeeDBManager.insertDataIntoDB(coffeeDBDataIn.getSqlQuery());
				coffeeDBDataOut = new CoffeeDBDataOut(null,null);
				coffeeDBDataOut.setSuccess(true);
			}
			
		}catch(SQLException ex){
			coffeeDBDataOut = new CoffeeDBDataOut(null,ex);
			coffeeDBDataOut.setSuccess(false);
		}catch(Exception e){
			coffeeDBDataOut = new CoffeeDBDataOut(null,e);
			coffeeDBDataOut.setSuccess(false);
		}

		ObjectOutputStream outStream = new ObjectOutputStream(response.getOutputStream());

		outStream.writeObject(coffeeDBDataOut);
		outStream.close();
	}

}
