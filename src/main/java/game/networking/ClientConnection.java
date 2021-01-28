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

    private boolean isComputer;

    public ClientConnection(boolean isComputer) 
    {
        instance = this;
        this.isComputer = isComputer;

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
        
        System.out.println("Starting client on port: " + port);

        try 
        {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("Sending client handshake with name: " + playerName);

            out.println("h;" + playerName);
        } 
        catch (IOException e) { e.printStackTrace(); }

        var readMessageThread = new Thread(() -> 
        {
            readMessage();
        });

        readMessageThread.start();
    }

    private void readMessage()
    {
        try 
        {
            while (true)
            {
                var inputLine = in.readLine();
    
                if (inputLine == null) continue;

                System.out.println("Received message:");
                System.out.println(inputLine);

                var inputSplit = inputLine.split(";");

                switch(inputSplit[0])
                {
                    case "ne": 
                    {
                        onNameExists();
                        break;
                    }
                    case "h":
                    {
                        onServerHandshake();
                        break;
                    }
                    case "n":
                    {
                        onEnemyName(inputSplit[1]);
                        break;
                    }
                    case "s": 
                    {
                        onGameSetup(inputSplit[1]);
                        break;
                    }
                    case "u": 
                    {
                        onUpdate(inputSplit[1], inputSplit[2], inputSplit[3], inputSplit[4], inputSplit[5], inputSplit[6], inputSplit[7]);
                        break;
                    }
                    case "g": 
                    {
                        onGameOver(inputSplit[1], inputSplit[2]);
                        dispose();
                        return;
                    }
                }
            }
        } 
        catch (IOException e) { e.printStackTrace(); }
    }

    public void sendClientBoard(Board board)
    {
        var boardEnc = "";

        for (int y = 0; y < GameConstants.boardSize.y; y++)
        {
            for (int x = 0; x < GameConstants.boardSize.x; x++)
            {
                var shipType = board.getShip(new Vector2Int(x, y));
                boardEnc += shipType + ";";
            }
        }
        out.println("b;" + boardEnc);
    }

    public void sendMove(Vector2Int cellPos)
    {
        var x = (cellPos == null) ? "" : cellPos.x;
        var y = (cellPos == null) ? "" : cellPos.y;

        out.println("m;" + x + ";" + y);
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
        System.out.println("Received enemy name: " + enemyName);

        System.out.println("Starting client match!");

        match = new ClientMatch(playerName, enemyName, isComputer);
        addListener(match);
    }

    private void onGameSetup(String nextPlayerName)
    {
        System.out.println("Received game setup, player " + nextPlayerName + " will begin!");

        invokeGameSetup(nextPlayerName);
    }

    private void onUpdate(String xEnc, String yEnc, String isHitEnc, String isSunkEnc, String isLateEnc, String lastPlayerName, String nextPlayerName)
    {
        var cellPos = new Vector2Int(-1, -1);
        try
        {
            cellPos.x = Integer.parseInt(xEnc);
            cellPos.y = Integer.parseInt(yEnc);
        }
        catch (NumberFormatException e) { cellPos = null; }

        var isHit = Boolean.parseBoolean(isHitEnc);
        var isSunk = Boolean.parseBoolean(isSunkEnc);
        var isLate = Boolean.parseBoolean(isLateEnc);

        invokeUpdate(lastPlayerName, nextPlayerName, cellPos, isHit, isSunk, isLate);
    }

    private void onGameOver(String winnerName, String isRegularWinEnc)
    {
        var result = Result.TIE;
        if (match.leftPlayer.name.equals(winnerName)) result = Result.WIN_LEFT;
        else if (match.rightPlayer.name.equals(winnerName)) result = Result.WIN_RIGHT;
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
        catch (IOException e) { e.printStackTrace(); }
    }
}
