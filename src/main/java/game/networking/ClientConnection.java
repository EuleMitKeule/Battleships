package game.networking;

import game.*;
import java.net.*;
import java.io.*;

public class ClientConnection 
{
    private Socket _socket;
    private PrintWriter _out;
    private BufferedReader _in;

    public ClientConnection() 
    {
        try 
        {
            System.out.println("Client started!");
            _socket = new Socket("localhost", GameConstants.port);
            _out = new PrintWriter(_socket.getOutputStream(), true);
            _in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));    

            var inThread = new Thread(() -> 
            {
                readMessage();
            });

            inThread.start();
        } 
        catch (Exception e) 
        {
        
        }
    }

    public void stop()
    {
        try
        {
            _socket.close();
            _in.close();
            _out.close();
        } 
        catch (Exception e)
        {
        }
    }

    private void readMessage()
    {
        try 
        {
            var inputLine = _in.readLine();
    
            if (inputLine != null)
            {
                System.out.println("Server says " + inputLine);
            }
        } 
        catch (Exception e) 
        {
        }
    }
}
