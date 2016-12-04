import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class MessengerServerThread extends Thread 
{
    private Socket socket = null;
    public MessengerProtocol mp;
    ArrayList<String> buffer;
    PrintStream out;
    
    public MessengerServerThread(Socket socket) 
    {
        super("MessengerServerThread");
        this.socket = socket;
    }
    
    public void run()
    {
    	//listener = new MessageListenerThread();	
    	buffer = new ArrayList<String>();
    	
        try 
        {
        	out = new PrintStream(socket.getOutputStream(), false);
        	 
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String inputLine, outputLine;
            mp = new MessengerProtocol(this.getId());
            outputLine = mp.processInput(null);
            out.println(outputLine);

            while ((inputLine = in.readLine()) != null)
            {
                outputLine = mp.processInput(inputLine);
             
                out.print(outputLine);
                             
                out.println();
                
                if (outputLine.equals("Bye"))
                {
                    System.out.println("Thread " + this.getId() + " quit by the client.");
                    mp.logOut();
                    break;
                }         
            }
            
            socket.close();
        } 
        catch(SocketException s)
        {
        	System.out.println("Socket thread " + this.getId() + " forcibly terminated by the client.");
        	mp.logOut();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
       
    }
    
    public void recieveMessage(String s)
    {
    	System.out.println("Message recieved");
    	out.println(s);
    }
}