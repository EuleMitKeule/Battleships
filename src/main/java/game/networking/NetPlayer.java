package game.networking;

import java.net.Socket;

public class NetPlayer 
{
    public Socket socket;
    public String name;

    /**
     * @param socket The clients socket object
     * @param name The name of the client
     */
    public NetPlayer(Socket socket, String name)
    {
        this.socket = socket;
        this.name = name;
    }
}
