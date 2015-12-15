import java.io.Serializable;
import javax.swing.table.*;


public class CoffeeDBDataOut implements Serializable
{
	private DefaultTableModel tableModelData;
	private Exception exception;
	private boolean success;

	public CoffeeDBDataOut(DefaultTableModel tableModelData, Exception exception)
	{
		this.tableModelData = tableModelData;
		this.exception = exception;
	}

	public DefaultTableModel getTableModelData()
	{
		return tableModelData;
	}

	public Exception getException()
	{
		return exception;
	}

	public void setSuccess(boolean success)
	{
		this.success = success;
	}

	public boolean getSuccess()
	{
		return success;
	}

}