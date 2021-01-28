package game;

import game.core.Game;

public class Main
{
  /**
   * Starts the main application 
   * @ensures new Game()
   * @param args the arguments given for this main method
   */
  public static void main(String args[])  
  {
      new Game("Battleships", 60, 60);
      var test = "test";
      System.out.println(test.split(";")[0]);
  }
}
