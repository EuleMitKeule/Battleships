package game.networking;

import java.net.Socket;

public class NetPlayer 
{
    public Socket socket;
    public String name;

    public NetPlayer(Socket socket, String name)
    {
        this.socket = socket;
        this.name = name;
    }
}
