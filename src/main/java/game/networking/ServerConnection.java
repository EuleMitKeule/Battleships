package game.networking;

import game.core.*;
import java.net.*;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import java.io.*;

public class ServerConnection
{

    private ServerSocket serverSocket;

    private LinkedList<NetPlayer> waitingPlayers = new LinkedList<NetPlayer>();

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
            serverSocket = new ServerSocket(port);
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

            System.out.println("Waiting for client handshake");

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
            var outputStream = new PrintStream(socket.getOutputStream(), true);

            var inputLine = inputStream.readLine();
            var inputSplit = inputLine.split(";");

            while (!inputSplit[0].equals("h"))
            {
                inputLine = inputStream.readLine();
                inputSplit = inputLine.split(";");
            }

            var playerName = inputSplit[1];
            var isAlreadyPresent = false;

            System.out.println("Client handshake received! Player name :" + playerName);

            for (int i = 0; i < waitingPlayers.size(); i++)
            {
                var netPlayer = waitingPlayers.get(i);
                if (netPlayer == null) continue;
                if (netPlayer.name == playerName) isAlreadyPresent = true;           
            }

            if (isAlreadyPresent)
            {
                System.out.println("The name " + playerName + " is already present in the lobby!");
                outputStream.println("ne");
                return;
            }

            if (waitingPlayers.size() > 0)
            {
                var leftPlayer = waitingPlayers.pop();
                var rightPlayer = new NetPlayer(socket, playerName);
                
                new MatchConnection(leftPlayer, rightPlayer, serverSocket);

                System.out.println("Player " + leftPlayer.name + " and player " + rightPlayer.name + "got matched");
            }
            else 
            {
                waitingPlayers.add(new NetPlayer(socket, playerName));
                System.out.println("Player " + playerName + " added to match queue");
            }
        }
        catch (IOException e) { e.printStackTrace(); }
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
