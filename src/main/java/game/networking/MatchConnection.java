package game.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.util.ArrayList;
import game.*;
import game.core.*;
import java.util.Arrays;

public class MatchConnection
{
    private NetPlayer leftPlayer;
    private NetPlayer rightPlayer;
    private BufferedReader leftIn;
    private PrintStream leftOut;
    private BufferedReader rightIn;
    private PrintStream rightOut;

    private ArrayList<IMatchConnectionListener> listeners = new ArrayList<IMatchConnectionListener>();

    private ServerMatch match;

    /**
     * @param leftPlayer The net player object of the left player
     * @param rightPlayer The net player object of the right player
     * @param serverSocket The server socket object
     */
    public MatchConnection(NetPlayer leftPlayer, NetPlayer rightPlayer, ServerSocket serverSocket) 
    {
        UI.instance.log("New Match started!");
        UI.instance.log("Waiting for client boards...");

        this.leftPlayer = leftPlayer;
        this.rightPlayer = rightPlayer;

        match = new ServerMatch(leftPlayer.name, rightPlayer.name, this);

        try
        {
            leftIn = new BufferedReader(new InputStreamReader(leftPlayer.socket.getInputStream()));
            leftOut = new PrintStream(leftPlayer.socket.getOutputStream(), true);
            
            rightIn = new BufferedReader(new InputStreamReader(rightPlayer.socket.getInputStream()));
            rightOut = new PrintStream(rightPlayer.socket.getOutputStream(), true);

            var readLeftThread = new Thread(() -> readMessage(leftPlayer, leftIn));
            var readRightThread = new Thread(() -> readMessage(rightPlayer, rightIn));

            readLeftThread.start();
            readRightThread.start();

            leftOut.println("n;" + rightPlayer.name);
            rightOut.println("n;" + leftPlayer.name);
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    /**
     * Listens for any messages sent by either client
     * @param netPlayer The net player object that send the message
     * @param in The input stream that read the message
     */
    private void readMessage(NetPlayer netPlayer, BufferedReader in)
    {
        try 
        {
            while (true)
            {
                var inputLine = in.readLine();
    
                if (inputLine == null) continue;

                UI.instance.log("Received message from player " + netPlayer.name + ":");
                UI.instance.log(inputLine);

                var inputSplit = inputLine.split(";");

                switch(inputSplit[0])
                {
                    case "b":
                    {
                        onClientBoard(netPlayer, Arrays.copyOfRange(inputSplit, 1, inputSplit.length));
                        break;
                    } 
                    case "m":
                    {
                        onMove(netPlayer, inputSplit[1], inputSplit[2]);
                        break;
                    } 
                    default: continue;
                }
            }
        } 
        catch (IOException e) 
        { 
            var isLeftPlayer = netPlayer == leftPlayer;
            sendGameOver(isLeftPlayer ? rightPlayer.name : leftPlayer.name, false, isLeftPlayer ? rightOut : leftOut);
            dispose();
        }
        
    }

    /**
     * Sends a game setup message to both clients
     * @param nextPlayer The player object of the beginning player
     */
    public void sendGameSetup(Player nextPlayer)
    {
        leftOut.println("s;" + nextPlayer.name);
        rightOut.println("s;" + nextPlayer.name);
    }

    /**
     * Sends an update message to both clients
     * @param lastPlayer The player that made the move
     * @param nextPlayer The player that will guess next
     * @param cellPos The position the move was made at
     * @param isHit Whether the move hit a ship
     * @param isSunk Whether the move sunk a ship
     * @param isLate Whether the move was made due to the round timer running out
     */
    public void sendUpdate(Player lastPlayer, Player nextPlayer, Vector2Int cellPos, boolean isHit, boolean isSunk, boolean isLate)
    {
        var lastPlayerName = " ";
        var nextPlayerName = " ";
        if (lastPlayer != null) lastPlayerName = lastPlayer.name;
        if (nextPlayer != null) nextPlayerName = nextPlayer.name;

        var x = (cellPos == null) ? "" : cellPos.x;
        var y = (cellPos == null) ? "" : cellPos.y;

        leftOut.println("u;" + x + ";" + y + ";" + isHit + ";" + isSunk + ";" + isLate + ";" + lastPlayerName + ";" + nextPlayerName);
        rightOut.println("u;" + x + ";" + y + ";" + isHit + ";" + isSunk + ";" + isLate + ";" + lastPlayerName + ";" + nextPlayerName);
    }

    /**
     * Sends a game over message to both clients
     * @param winnerName The name of the winning player
     * @param isRegularWin Whether the match was won by score or due to the match timer running out
     */
    public void sendGameOver(String winnerName, boolean isRegularWin)
    {
        leftOut.println("g;" + winnerName + ";" + isRegularWin);
        rightOut.println("g;" + winnerName + ";" + isRegularWin);
        dispose();
    }

    /**
     * Sends a game over message to a single client
     * @param winnerName The name of the winning player
     * @param isRegularWin Whether the match was won by score or due to the match timer running out
     * @param out The print stream object to write the message to
     */
    public void sendGameOver(String winnerName, boolean isRegularWin, PrintStream out)
    {
        out.println("g;" + winnerName + ";" + isRegularWin);
    }

    /**
     * Invoked when a client board message was received
     * @param netPlayer The net player object that sent the message
     * @param boardEnc The board object encoded as string
     */
    private void onClientBoard(NetPlayer netPlayer, String[] boardEnc)
    {
        var isLeftPlayer = netPlayer == leftPlayer;
        var board = new Board(GameConstants.boardSize, isLeftPlayer ? GameConstants.leftOffset : GameConstants.rightOffset, GameConstants.tileSize);

        for (int y = 0; y < GameConstants.boardSize.y; y++)
        {
            for (int x = 0; x < GameConstants.boardSize.x; x++)
            {
                var shipType = ShipType.valueOf(boardEnc[y * GameConstants.boardSize.x + x]);

                board.setShip(new Vector2Int(x, y), shipType);
            }
        }

        invokeClientBoard(netPlayer.name, board);
    }

    /**
     * Invoked when a move message was received
     * @param netPlayer The net player object that sent the message
     * @param xEnc The x position the move was made at encoded as string
     * @param yEnc The y position the move was made at encoded as string
     */
    private void onMove(NetPlayer netPlayer, String xEnc, String yEnc)
    {
        var x = Integer.parseInt(xEnc);
        var y = Integer.parseInt(yEnc);

        invokeMove(netPlayer.name, new Vector2Int(x, y));
    }

    /**
     * Invokes the ClientBoard event
     * @param playerName The name of the player that owns the board
     * @param board The board that the player assigned
     */
    private void invokeClientBoard(String playerName, Board board)
    {
        for (int i = 0; i < listeners.size(); i++)
        {
            var listener = listeners.get(i);
            if (listener == null) continue;
            listener.onClientBoardReceived(playerName, board);
        }
    }

    /**
     * Invokes the Move event
     * @param playerName The name of the player that made the move
     * @param cellPos The position the move was made at
     */
    private void invokeMove(String playerName, Vector2Int cellPos)
    {
        for (int i = 0; i < listeners.size(); i++)
        {
            var listener = listeners.get(i);
            if (listener == null) continue;
            listener.onMoveReceived(playerName, cellPos);
        }
    }

    /**
     * Adds an IMatchConnectionListener to the list of listeners
     * @param listener The listener to add
     */
    public void addListener(IMatchConnectionListener listener)
    {
        listeners.add(listener);
    }

    /**
     * Removes an IMatchConnectionListener from the list of listeners
     * @param listener The listener to remove
     */
    public void removeListener(IMatchConnectionListener listener)
    {
        listeners.remove(listener);
    }

    /**
     * Event subscription cleanup and socket disposing
     */
    public void dispose()
    {
        match.dispose();

        try
        {
            leftIn.close();
            leftOut.close();
            rightIn.close();
            rightOut.close();
            leftPlayer.socket.close();
            rightPlayer.socket.close();
        }
        catch (IOException e) { e.printStackTrace(); }
    }
}