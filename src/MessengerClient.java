import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.Timer;
 
public class MessengerClient 
{
	static String port = "81";
	static String hostName = "localhost";
	
	static String fromServer;
    static String fromUser = "nothing";
    
    public static void main(String[] args) throws IOException 
    {

             
        int portNumber = Integer.parseInt(port);
 
        System.out.println("Begin");
        
        try 
        (
        	Socket kkSocket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
        )   
        {
        	System.out.print("Connected to " + kkSocket.getInetAddress().toString() + "\n");
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
 
            ActionListener taskPerformer = new ActionListener() 
        	{
        	      public void actionPerformed(ActionEvent evt) 
        	      {
        	    	  try
        	    	  {  
        	    		  //System.out.println(fromUser);
	        	    	  out.println(fromUser);
	        	    	  fromServer = in.readLine();
	        	    	  if(!fromServer.equals("Timer works"))
	        	    	  {
		        	    	  System.out.println("R:" + fromServer);
	        	    	  }

        	    	  }
        	    	  catch(Exception e)
        	    	  {
        	    		  //e.printStackTrace();
        	    		  fromServer = "Bye.";
        	    		  ((Timer)evt.getSource()).stop();
        	    	  }
        	      }
        	};
            
        	new Timer(100, taskPerformer).start();
        	fromServer = "Begin server communication";
            
            while (true) 
            {
            	//fromServer = in.readLine();
            	
            	String[] arguments = fromServer.split("[$]+");
            	
                fromUser = stdIn.readLine();
                
                if (fromUser != null) 
                {
                	  if(fromUser.equals("q"))
    	    		  {
    	    			  System.out.println("Connection Terminated.");
    	    			  System.exit(1);
    	    		  }
                	  
                    System.out.println("S: " + fromUser);
                    out.println(fromUser);
                    fromUser = "nothing";
                }
            }
        } 
        catch (UnknownHostException e) 
        {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } 
        catch (IOException e) 
        {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
    }
}