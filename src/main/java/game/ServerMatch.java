package game;

import game.core.*;

public class ServerMatch extends Match
{
    public ServerMatch(Game game, Vector2Int leftOffset, Vector2Int rightOffset, int tileSize, Vector2Int boardSize)
    {
        super(game, leftOffset, rightOffset, tileSize, boardSize);
    }
}
