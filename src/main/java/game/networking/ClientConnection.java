package game.networking;

import game.*;
import game.core.Game;
import game.core.Vector2Int;

import java.net.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import java.io.*;

public class ClientConnection 
{
    public static ClientConnection instance;

    private ArrayList<IClientListener> listeners = new ArrayList<IClientListener>();

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private String host;
    private int port;

    private String playerName = "";

    private ClientMatch match;

    public ClientConnection() 
    {
        instance = this;

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
                    case "ne": onNameExists();
                    case "h": onServerHandshake();
                    case "n": onEnemyName(inputSplit[1]);
                    case "s": onGameSetup(inputSplit[1]);
                    case "u": onUpdate(inputSplit[1], inputSplit[2], inputSplit[3], inputSplit[4], inputSplit[5], inputSplit[6], inputSplit[7]);
                    case "g": 
                    {
                        onGameOver(inputSplit[1]);
                        dispose();
                        return;
                    }
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
        match = new ClientMatch(playerName, enemyName);
        addListener(match);
    }

    private void onGameSetup(String nextPlayerName)
    {
        invokeGameSetup(nextPlayerName);
    }

    private void onUpdate(String xEnc, String yEnc, String isHitEnc, String isSunkEnc, String isLateEnc, String lastPlayerName, String nextPlayerName)
    {
        var x = Integer.parseInt(xEnc);
        var y = Integer.parseInt(yEnc);
        var cellPos = new Vector2Int(x, y);

        var isHit = Boolean.parseBoolean(isHitEnc);
        var isSunk = Boolean.parseBoolean(isSunkEnc);
        var isLate = Boolean.parseBoolean(isLateEnc);

        invokeUpdate(lastPlayerName, nextPlayerName, cellPos, isHit, isSunk, isLate);
    }

    private void onGameOver(String resultEnc)
    {
        var result = Result.valueOf(resultEnc);
        invokeGameOver(result);
    }

    private void invokeGameSetup(String nextPlayerName)
    {
        for (int i = 0; i < listeners.size(); i++)
        {
            var listener = listeners.get(i);
            if (listener == null) continue;
            listener.onGameSetupReceived(nextPlayerName);
        }
    }

    private void invokeUpdate(String lastPlayerName, String nextPlayerName, Vector2Int cellPos, boolean isHit, boolean isSunk, boolean isLate)
    {
        for (int i = 0; i < listeners.size(); i++)
        {
            var listener = listeners.get(i);
            if (listener == null) continue;
            listener.onUpdateReceived(lastPlayerName, nextPlayerName, cellPos, isHit, isSunk, isLate);
        }
    }

    private void invokeGameOver(Result result)
    {
        for (int i = 0; i < listeners.size(); i++)
        {
            var listener = listeners.get(i);
            if (listener == null) continue;
            listener.onGameOverReceived(result);
        }
    }

    public void addListener(IClientListener listener)
    {
        listeners.add(listener);
    }

    public void removeListener(IClientListener listener)
    {
        listeners.remove(listener);
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

    private void dispose()
    {
        if (match != null) removeListener(match);

        try
        {
            in.close();
            out.close();
            socket.close();
        }
        catch (IOException e) { }
    }
}
