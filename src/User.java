
public class User implements java.io.Serializable
{
	
	String username;
	String password;
	boolean loggedIn = false;
	long threadID;
	
	User(String n,String p)
	{
		username = n;
		password = p;
	}
}
