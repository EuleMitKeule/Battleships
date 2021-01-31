package game;

import game.core.*;

public enum Direction 
{
    Right,
    Left,
    Up,
    Down;  

    /**
     * @param direction The direction type
     * @return The direction type converted to a vector
     */
    public static Vector2Int ToVector(Direction direction)
    {
        switch (direction)
        {
            case Left: return Vector2Int.left();
            case Right: return Vector2Int.right();
            case Up : return Vector2Int.up();
            case Down: return Vector2Int.down();
            default: return null;
        }
    }
}
