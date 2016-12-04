import java.net.*;
import java.io.*;
 
public class MessengerProtocol {
	
	private static final int INIT = -1;
    private static final int WAITINGFORLOGIN = 0;
    private static final int NEWUSERYN = 1;
    private static final int MAKENEWACCOUNTPASSWORD = 2;
    private static final int ENTERPASSWORDFOREXISTINGACCOUNT = 3;
    private static final int LOGGEDINDEFAULT = 4;
  
    private int state = INIT;
    private boolean initialized = false;
    private User currentUser;
    private String namebuffer;
    private long threadID;
 
    MessengerProtocol(long id)
    {
    	this.threadID = id;
    }
    
    public String processInput(String input)
    {
        String output = null;

        //System.out.println(state);
        
        if (!initialized) 
        {
        	initialized = true;
        	output = "Log in or register.";
        	input = " ";
        	state = WAITINGFORLOGIN;
        }
        else if(input.equals("nothing"))
        {
        	output = "Timer works";
        }
        else if(state == WAITINGFORLOGIN)
        {
        	output = checkUsername(input);
        }
        else if(state == NEWUSERYN)
        {
        	if(input.equals("y"))
        	{
        		state = MAKENEWACCOUNTPASSWORD;
        		output = "Please enter a password for the new account " + namebuffer;
        	}
        	else if(input.equals("n"))
        	{
        		output = "Log in or register.";
        		state = WAITINGFORLOGIN;
        	}
        }
        else if(state == MAKENEWACCOUNTPASSWORD)
        {
        	output = addUser(namebuffer,input);
        }
        else if(state == ENTERPASSWORDFOREXISTINGACCOUNT)
        {
        	output = tryLogin(currentUser,input);
        }
        else if(state == LOGGEDINDEFAULT)
        {
        	output = processCommand(input);
        }
        else
        {
        	output = "Unknown command.";
        }
        
        
      
       	if(input.equals("q"))
       	{
       		output = "Bye";
        }
       	
        return output;
    }
    
    public String checkUsername(String input)
    {
    	String output = " ";
    	
    	User u = MessengerServer.checkUsername(input);
    	
    	if(u==null)
    	{
    		namebuffer = input;
    		output = "Username not recognized. Do you want to register as a new user y/n?";
    		state = NEWUSERYN;
    	}
    	else
    	{
    		currentUser = u;
    		output = "Please enter your password.";
    		state = ENTERPASSWORDFOREXISTINGACCOUNT;
    	}
    	
    	return output;
    }
    
    public String addUser(String username,String password)
    {
    	MessengerServer.addUser(username,password);
    	state = WAITINGFORLOGIN;
    	return "New user " + username + " created. Please log in or register.";
    }
    
    public String tryLogin(User u,String input)
    {
    	String output;
    	
    	if(input.equals(u.password))
    	{
    		u.loggedIn = true;
    		u.threadID = this.threadID;
    		state = LOGGEDINDEFAULT;
    		output = "Logged in to " + u.username;
    	}
    	else
    	{
    		currentUser = null;
    		state = WAITINGFORLOGIN;
    		output = "Password incorrect. Please log in or register.";
    	}
    	
    	return output;
    }
    
    public String processCommand(String input)
    {
    	String result = "";
       	String[] arguments = input.split("[ ]+");
       	
       	if(arguments[0].equals("message")&&arguments.length>2)
       	{
       		int n = MessengerServer.message(currentUser.username,arguments[1],arguments[2]);
       		
       		if(n==0)
       		{
       			result = "Unknown error.";
       		}
       		else if(n==1)
       		{
       			result = "This user does not exist.";
       		}
       		else if(n==2)
       		{
       			result = "This user is not logged in.";
       		}
       		else if(n==3)
       		{
       			result = "Message sent.";
       		}
       	}
       	else
       	{
       		result = "Unknown Command";
       	}
    	
    	return result;
    }
    
    public void logOut()
    {
    	currentUser.loggedIn = false;
    	currentUser = null;
    	state = INIT;
    	initialized = false;
    }
    
}