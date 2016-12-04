import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class MessengerServer 
{
	static String port = "81";
	static ArrayList<User> users;
	
	static ThreadGroup threads;
	
	
    public static void main(String[] args) throws IOException 
    {
    	DatabaseConnector.connectToDatabase();
    	users = DatabaseConnector.getUsers();
 
    	int portNumber = Integer.parseInt(port);
        boolean listening = true;
        
        threads = getThreads();
        
        for(User u:users)
    	{
    		u.loggedIn = false;
    	}
        
        try (ServerSocket serverSocket = new ServerSocket(portNumber))
        { 
            while (listening) 
            {
	            new MessengerServerThread(serverSocket.accept()).start();
	        }
	    } 
        catch (IOException e)
        {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }
    
    
    
    public static User checkUsername(String n)
    {
    	User result = null;
    	
    	for(User u:users)
    	{
    		if(n.equals(u.username))
    		{
    			result = u;
    		}
    	}
    	
    	return result;
    }
    
    public static void addUser(String username,String password)
    {
    	User newu = new User(username,password);
    	users.add(newu);
    	DatabaseConnector.insertUser(username,password);
    }
    
    public static int message(String usernamefrom,String usernameto,String message)
    {
    	User u = checkUsername(usernameto);
    	
    	if(u==null)
    	{
    		return 1;
    	}
    	else if(!u.loggedIn)
    	{
    		return 2;
    	}
    	
    	MessengerServerThread recipient = (MessengerServerThread)getThreadById(u.threadID);
    	recipient.recieveMessage("Message from " + usernamefrom + ": " + message);
    	
    	return 3;
    }
    
    private static Thread getThreadById(long num)
    {
    	Thread t = null;
    	
    	int allActiveThreads = threads.activeCount();
    	Thread[] allThreads = new Thread[allActiveThreads];
    	threads.enumerate(allThreads);  
    	 
    	for (int i = 0; i < allThreads.length; i++) 
    	{
    	       Thread thread = allThreads[i];
    	       long id = thread.getId();
    	       
    	       if(num == id)
    	       {
    	    	   t = thread;
    	    	   break;
    	       }
    	       
    	}
    	
    	return t;
    }
    
    private static ThreadGroup getThreads() 
    {
   	 	Thread currentThread = Thread.currentThread();

        ThreadGroup rootGroup = currentThread.getThreadGroup();
        while (true) 
        {
            ThreadGroup parentGroup = rootGroup.getParent();
            if (parentGroup == null) 
            {
                break;
            }
            rootGroup = parentGroup;
        }
        return rootGroup;
    }
}