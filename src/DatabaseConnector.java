import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public abstract class DatabaseConnector 
{
	static Connection dbConnection;
	static String dbUserName = "root";
	static String dbPassword = "";
	
	public static void connectToDatabase()
	{
		try
		{
    		//Class.forName("com.mysql.jdbc.Driver");  
			dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test",dbUserName,dbPassword);  
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static ArrayList<User> getUsers()
    {
    	ArrayList<User> result = new ArrayList<User>();
    	
    	try
    	{
    		Statement stmt=dbConnection.createStatement();  
    		
    		ResultSet rs=stmt.executeQuery("select * from User"); 
    		
    		while(rs.next())
    		{
    			result.add(new User(rs.getString(2),rs.getString(3)));
    		}
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	
    	System.out.println("Users loaded from database");
    	return result;
    }
	
	public static boolean insertUser(String username,String password)
    {    	
    	try
    	{
    		Statement stmt=dbConnection.createStatement();  
    		stmt.execute("INSERT INTO user(UserName,Password) VALUES (\"" + username + "\",\"" + password + "\");"); 
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		System.out.println("Insert failed");
    		return false;
    	}
    	
    	return true;
    }
}
