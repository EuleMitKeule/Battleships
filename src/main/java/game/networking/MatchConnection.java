package game.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MatchConnection
{
    private NetPlayer leftPlayer;
    private NetPlayer rightPlayer;
    private BufferedReader leftIn;
    private PrintStream leftOut;
    private BufferedReader rightIn;
    private PrintStream rightOut;

    public MatchConnection(NetPlayer leftPlayer, NetPlayer rightPlayer, ServerSocket serverSocket) 
    {
        this.leftPlayer = leftPlayer;
        this.rightPlayer = rightPlayer;
        
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
        }
        catch (IOException e) { }
    }

    private void readMessage(NetPlayer netPlayer, BufferedReader in)
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
                    case "b": onClientBoard(netPlayer, inputSplit[1]);
                    case "m": onMove(netPlayer, inputSplit[1], inputSplit[2]);
                    default: continue;
                }
            }
        } 
        catch (IOException e) { }
    }

    private void onClientBoard(NetPlayer netPlayer, String boardEnc)
    {

    }

    private void onMove(NetPlayer netPlayer, String xEnc, String yEnc)
    {

    }

    public void dispose()
    {
        try
        {
            leftIn.close();
            leftOut.close();
            rightIn.close();
            rightOut.close();
            leftPlayer.socket.close();
            rightPlayer.socket.close();
        }
        catch (IOException e) { }
    }
}