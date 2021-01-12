package game.networking;

import game.*;
import java.net.*;
import java.io.*;

public class ServerConnection
{

    private ServerSocket _socket;
    private Socket _leftClientSocket;
    private Socket _rightClientSocket;
    private PrintWriter _outLeft;
    private BufferedReader _inLeft;
    private PrintWriter _outRight;
    private BufferedReader _inRight;

    public final int port = GameConstants.port;

    public ServerConnection()
    {
        try
        {
            _socket = new ServerSocket(GameConstants.port);
            System.out.println("Server started");

            
            _leftClientSocket = _socket.accept();
            _outLeft = new PrintWriter(_leftClientSocket.getOutputStream(), true);
            _inLeft = new BufferedReader(new InputStreamReader(_leftClientSocket.getInputStream()));
            System.out.println("left client joined");

           

            // _rightClientSocket = _socket.accept();
            // _outRight = new PrintWriter(_rightClientSocket.getOutputStream(), true);
            // _inRight = new BufferedReader(new
            // InputStreamReader(_rightClientSocket.getInputStream()));

            onAllPlayersConnected();
        } 
        catch (Exception e)
        {
            System.out.println("Connection failed");
            e.printStackTrace();
        }
    }

    public void stop()
    {
        try
        {
            _inLeft.close();
            _outLeft.close();
            _inRight.close();
            _outRight.close();
            _leftClientSocket.close();
            _rightClientSocket.close();
            _socket.close();
        } 
        catch (Exception e)
        {
        }
    }

    private void onAllPlayersConnected()
    {
        sendString("hello world");
    }

    public void sendString(String message)
    {
        try 
        {
            var outgoing = new PrintWriter(_leftClientSocket.getOutputStream());
            outgoing.println(message);
            outgoing.flush();
            _leftClientSocket.close();
        } 
        catch (Exception e) 
        {
            //TODO: handle exception
        }
    }
}
