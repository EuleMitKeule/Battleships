package game.networking;

import game.*;
import game.core.Game;

import java.net.*;

import javax.swing.JOptionPane;

import java.io.*;

public class ClientConnection 
{
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private String host;
    private int port;

    private String playerName = "";

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

        while (playerName.equals(""))
        {
            playerName = JOptionPane.showInputDialog(Game.frame, "Please enter your name:");
        }
        
        try 
        {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("h;" + playerName);
        } 
        catch (IOException e) { return; }

        var readMessageThread = new Thread(() -> 
        {
            readMessage();
        });

        readMessageThread.start();
    }

    public void stop()
    {
        try
        {
            socket.close();
            in.close();
            out.close();
        } 
        catch (IOException e) { }
    }

    private void readMessage()
    {
        try 
        {
            while (true)
            {
                var inputLine = in.readLine();
    
                if (inputLine == null) continue;

                var inputSplit = inputLine.split(";");

                switch(inputSplit[0])
                {
                    case "ne" -> onNameExists();
                    case "h" -> onServerHandshake();
                    case "n" -> onEnemyName(inputSplit[1]);
                    case "s" -> onGameSetup(inputSplit[1]);
                    case "u" -> onUpdate(inputSplit[1], inputSplit[2], inputSplit[3], inputSplit[4], inputSplit[5], inputSplit[6], inputSplit[7]);
                    case "g" -> onGameOver(inputSplit[1]);
                }
            }
        } 
        catch (IOException e) { }
    }

    private void onNameExists()
    {
        while (playerName.equals(""))
        {
            playerName = JOptionPane.showInputDialog(Game.frame, "Please enter your name:");
        }
        out.println("h;" + playerName);
    }

    private void onServerHandshake()
    {
        System.out.println("You successfully connected to the Server");
    }

    private void onEnemyName(String enemyName)
    {

    }

    private void onGameSetup(String nextPlayer)
    {

    }

    private void onUpdate(String posX, String posY, String isHit, String isSunk, String isLate, String lastPlayer, String nextPlayer)
    {

    }

    private void onGameOver(String result)
    {

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
