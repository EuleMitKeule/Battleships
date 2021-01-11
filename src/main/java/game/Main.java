package game;

import game.core.Game;
import game.networking.*;

public class Main
{
  public static void main(String args[])  
  {
    new Game("Battleships", 60, 60);

    if (args.length > 0 && args[0].equals("server"))
    {
      new ServerConnection();
      System.out.println(args[0]);
    } 
    else 
    {
      new ClientConnection();
    }
  }
}
