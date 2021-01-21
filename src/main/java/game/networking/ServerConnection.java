package game.networking;

import game.*;
import game.core.*;
import java.net.*;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import java.io.*;

public class ServerConnection
{

    private ServerSocket serverSocket;

    private LinkedList<NetPlayer> connectedPlayers = new LinkedList<NetPlayer>();

    private int port;

    public ServerConnection()
    {
        do
        {
            var portIn = JOptionPane.showInputDialog(Game.frame, "Please enter the server port:");
            
            if (tryParseInt(portIn)) port = Integer.parseInt(portIn);
            else port = -1;
        }
        while (port < 0 || port > 65535);
        
        System.out.println("Starting server on port " + port);

        try
        {
            serverSocket = new ServerSocket(GameConstants.port);
        }
        catch (IOException e) { return; }
        
        System.out.println("Waiting for player connections");
        
        var listenForConnectionsThread = new Thread(() -> listenForConnections());
        listenForConnectionsThread.start();
    }

    private void listenForConnections()
    {
        try
        {
            var newPlayerSocket = serverSocket.accept();
    
            System.out.println("New player connected! " + newPlayerSocket.getInetAddress());

            System.out.println("Wait for client handshake");

            var listenForHandshakeThread = new Thread(() -> listenForHandshake(newPlayerSocket));
            listenForHandshakeThread.start();
        }
        catch (IOException e) { }
    }

    private void listenForHandshake(Socket socket)
    {
        try 
        {
            var inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            var inputLine = inputStream.readLine();
            var inputSplit = inputLine.split(" ");

            while (Integer.parseInt(inputSplit[0]) != Protocol.HANDSHAKE.ordinal())
            {
                inputLine = inputStream.readLine();
                inputSplit = inputLine.split(" ");
            }

            System.out.println("Client handshake received!");

            var playerName = inputSplit[1];

            if (connectedPlayers.size() > 0)
            {
                var leftPlayer = connectedPlayers.pop();
                var rightPlayer = new NetPlayer(socket, playerName);

                //new MatchConnection(serverSocket, leftPlayer, rightPlayer);
            }
            else connectedPlayers.add (new NetPlayer(socket, playerName));
        }
        catch (IOException e) { }
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
