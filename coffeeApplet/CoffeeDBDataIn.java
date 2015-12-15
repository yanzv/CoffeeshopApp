package coffeeApplet;

import java.io.Serializable;

class CoffeeDBDataIn implements Serializable
{
	
	private String sqlQuery;
	private DBCommandType commandType;

	public CoffeeDBDataIn(String sqlQuery, DBCommandType commandType)
	{
		this.sqlQuery = sqlQuery;
		this.commandType = commandType;
	}

	public String getSqlQuery()
	{
		return sqlQuery;
	}

	public DBCommandType getCommandType()
	{
		return this.commandType;
	}

}