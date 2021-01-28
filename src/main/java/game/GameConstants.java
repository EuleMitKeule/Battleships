package game;

import game.core.*;

/**
 * This class defines all game constants existing wirthin this game. 
 * Those constants are static so they can be acessed everywhere.
 */
public class GameConstants 
{
    public static Vector2Int boardSize = new Vector2Int(15, 10);

    public static Vector2Int leftOffset = new Vector2Int(32, 64);

    public static Vector2Int rightOffset = new Vector2Int(832, 64);

    public static int tileSize = 64;

    public static float scale = .766666666f;
}
