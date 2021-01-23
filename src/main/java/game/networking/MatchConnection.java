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

    public MatchConnection(NetPlayer leftPlayer, NetPlayer rightPlayer, ServerSocket serverSocket) 
    {
        System.out.println("New Match started!");
        System.out.println("Waiting for client boards...");

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

    private void readMessage(NetPlayer netPlayer, BufferedReader in)
    {
        try 
        {
            while (true)
            {
                var inputLine = in.readLine();
    
                if (inputLine == null) continue;

                System.out.println("Received message from player " + netPlayer.name + ":");
                System.out.println(inputLine);

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
        catch (IOException e) { e.printStackTrace(); }
    }

    public void sendGameSetup(Player nextPlayer)
    {
        leftOut.println("s;" + nextPlayer.name);
        rightOut.println("s;" + nextPlayer.name);
    }

    public void sendUpdate(Player lastPlayer, Player nextPlayer, Vector2Int cellPos, boolean isHit, boolean isSunk, boolean isLate)
    {
        var lastPlayerName = lastPlayer.name;
        var nextPlayerName = nextPlayer.name;

        var x = (cellPos == null) ? "" : cellPos.x;
        var y = (cellPos == null) ? "" : cellPos.y;

        leftOut.println("u;" + x + ";" + y + ";" + isHit + ";" + isSunk + ";" + isLate + ";" + lastPlayerName + ";" + nextPlayerName);
        rightOut.println("u;" + x + ";" + y + ";" + isHit + ";" + isSunk + ";" + isLate + ";" + lastPlayerName + ";" + nextPlayerName);
    }

    public void sendGameOver(Result result)
    {

    }

    private void onClientBoard(NetPlayer netPlayer, String[] boardEnc)
    {
        var isLeftPlayer = netPlayer == leftPlayer;
        var board = new Board(GameConstants.boardSize, isLeftPlayer ? GameConstants.leftOffset : GameConstants.rightOffset, GameConstants.tileSize);

        for (int x = 0; x < GameConstants.boardSize.x; x++)
        {
            for (int y = 0; y < GameConstants.boardSize.y; y++)
            {
                var shipType = ShipType.valueOf(boardEnc[y * GameConstants.boardSize.y + x]);

                board.setShip(new Vector2Int(x, y), shipType);
            }
        }

        invokeClientBoard(netPlayer.name, board);
    }

    private void onMove(NetPlayer netPlayer, String xEnc, String yEnc)
    {
        var x = Integer.parseInt(xEnc);
        var y = Integer.parseInt(yEnc);

        invokeMove(netPlayer.name, new Vector2Int(x, y));
    }

    private void invokeClientBoard(String playerName, Board board)
    {
        for (int i = 0; i < listeners.size(); i++)
        {
            var listener = listeners.get(i);
            if (listener == null) continue;
            listener.onClientBoardReceived(playerName, board);
        }
    }

    private void invokeMove(String playerName, Vector2Int cellPos)
    {
        for (int i = 0; i < listeners.size(); i++)
        {
            var listener = listeners.get(i);
            if (listener == null) continue;
            listener.onMoveReceived(playerName, cellPos);
        }
    }

    public void addListener(IMatchConnectionListener listener)
    {
        listeners.add(listener);
    }

    public void removeListener(IMatchConnectionListener listener)
    {
        listeners.remove(listener);
    }

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