package game.networking;

import game.*;
import game.core.Game;

import java.net.*;

import javax.swing.JOptionPane;

import java.io.*;

public class ClientConnection 
{
    private Socket _socket;
    private PrintWriter _out;
    private BufferedReader _in;

    private String host;
    private int port;

    public ClientConnection() 
    {
        do
        {
            host = JOptionPane.showInputDialog(Game.frame, "Please enter the server IP adress:");
        }
        while (host == "");

        do
        {
            var portIn = JOptionPane.showInputDialog(Game.frame, "Please enter the server port:");
            
            if (tryParseInt(portIn)) port = Integer.parseInt(portIn);
            else port = -1;
        }
        while (port < 0 || port > 65535);

        try 
        {
            _socket = new Socket("localhost", GameConstants.port);
            _out = new PrintWriter(_socket.getOutputStream(), true);
            _in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));    
        } 
        catch (IOException e) { return; }

        var inThread = new Thread(() -> 
        {
            readMessage();
        });

        inThread.start();
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
    
    private boolean tryParseInt(String value)
    {
        try 
        {  
            Integer.parseInt(value);  
            return true;  
        } 
        catch (NumberFormatException e) 
        {  
            return false;  
        }  
    }
}
