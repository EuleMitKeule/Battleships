package game;

import game.core.*;

public class LocalMatch extends Match
{
    public LocalMatch(Game game, Vector2Int leftOffset, Vector2Int rightOffset, int tileSize, Vector2Int boardSize)
    {
        super(game, leftOffset, rightOffset, tileSize, boardSize);

        leftPlayer = new Human("eule", this);

        invokePlayerAdded(leftPlayer, true);

        rightPlayer = new Computer("com", this);

        ((Computer)rightPlayer).Start();

        invokePlayerAdded(rightPlayer, false);
    }

    @Override
    public void onMove(Player player, Vector2Int cellPos)
    {
        super.onMove(player, cellPos);
    }
}
