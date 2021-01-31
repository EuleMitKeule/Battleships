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

    /**
     * @param isComputer Whether the client joined the server as a computer player
     */
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

    /**
     * Listens for any messages sent by the server
     */
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

    /**
     * Sends the client board message
     * @param board The board object to encode and send
     */
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

    /**
     * Sends the move message
     * @param cellPos The position the move was made at
     */
    public void sendMove(Vector2Int cellPos)
    {
        var x = (cellPos == null) ? "" : cellPos.x;
        var y = (cellPos == null) ? "" : cellPos.y;

        out.println("m;" + x + ";" + y);
    }

    /**
     * Invoked when a name exists message was received
     */
    private void onNameExists()
    {
        new ClientConnection(this.isComputer);
    }

    /**
     * Invoked when a server handshake message was received
     */
    private void onServerHandshake()
    {
        System.out.println("You successfully connected to the Server");
    }
    
    /**
     * Invoked when an enemy name message was received
     * @param enemyName The enemies name
     */
    private void onEnemyName(String enemyName)
    {
        System.out.println("Received enemy name: " + enemyName);

        System.out.println("Starting client match!");

        match = new ClientMatch(playerName, enemyName, isComputer);
        addListener(match);
    }

    /**
     * Invoked when a game setup message was received
     * @param nexPlayerName The name of the beginning player
     */
    private void onGameSetup(String nextPlayerName)
    {
        System.out.println("Received game setup, player " + nextPlayerName + " will begin!");

        invokeGameSetup(nextPlayerName);
    }

    /**
     * Invoked when an update message was received
     * @param xEnc The encoded x position of the move
     * @param yEnc The encoded y position of the move
     * @param isHitEnc Whether the move hit a ship encoded as string
     * @param isSunkEnc Whether the move sunk a ship encoded as string
     * @param isLateEnc Whether the move was due to the round timer running out encoded as string
     * @param lastPlayerName The name of the player that made the move
     * @param nextPlayername The name of the player who will guess next
     */
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

    /**
     * Invoked when a game over message was received
     * @param winnerName The name of the player that won the match
     * @param isRegularWinEnc Whether the game was won by score or due to a player disconnect encoded as string
     */
    private void onGameOver(String winnerName, String isRegularWinEnc)
    {
        var result = Result.TIE;
        var isRegularWin = Boolean.parseBoolean(isRegularWinEnc);
        if (match.leftPlayer.name.equals(winnerName)) result = Result.WIN_LEFT;
        else if (match.rightPlayer.name.equals(winnerName)) result = Result.WIN_RIGHT;
        invokeGameOver(result, isRegularWin);
    }

    /**
     * Invokes the GameSetup event
     * @param nexPlayerName The name of the beginning player
     */
    private void invokeGameSetup(String nextPlayerName)
    {
        for (int i = 0; i < listeners.size(); i++)
        {
            var listener = listeners.get(i);
            if (listener == null) continue;
            listener.onGameSetupReceived(nextPlayerName);
        }
    }

    /**
     * Invoked when an update message was received
     * @param lastPlayerName The name of the player that made the move
     * @param nextPlayername The name of the player who will guess next
     * @param cellPos The position the move was made at
     * @param isHit Whether the move hit a ship
     * @param isSunk Whether the move sunk a ship
     * @param isLate Whether the move was due to the round timer running out
     */
    private void invokeUpdate(String lastPlayerName, String nextPlayerName, Vector2Int cellPos, boolean isHit, boolean isSunk, boolean isLate)
    {
        for (int i = 0; i < listeners.size(); i++)
        {
            var listener = listeners.get(i);
            if (listener == null) continue;
            listener.onUpdateReceived(lastPlayerName, nextPlayerName, cellPos, isHit, isSunk, isLate);
        }
    }

    /**
     * Invokes a GameOver event
     * @param result The result type of the match
     * @param isRegularWin Whether the game was won by score or due to a player disconnect
     */
    private void invokeGameOver(Result result, boolean isRegularWin)
    {
        for (int i = 0; i < listeners.size(); i++)
        {
            var listener = listeners.get(i);
            if (listener == null) continue;
            listener.onGameOverReceived(result, isRegularWin);
        }
    }

    /**
     * Adds an IClientListener to the list of listeners
     * @param listener The listener to add
     */    
    public void addListener(IClientListener listener)
    {
        listeners.add(listener);
    }

    /**
     * Removes an IClientListener from the list of listeners
     * @param listener The listener to remove
     */
    public void removeListener(IClientListener listener)
    {
        listeners.remove(listener);
    }

    /**
     * @param value The string to parse
     * @return Whether the string can be parsed to integer
     */
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

    /**
     * Event subscription cleanup and socket disposing
     */
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
