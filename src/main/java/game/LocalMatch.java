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

        addListener(UI.instance);
    }

    @Override
    protected void invokeGameOver(Result result)
    {
        super.invokeGameOver(result);
        UI.instance.loadEnd(result, result == Result.WIN_LEFT ? leftPlayer.name : rightPlayer.name);
    }

    @Override
    public void onMove(Player player, Vector2Int cellPos)
    {
        super.onMove(player, cellPos);
    }

    @Override
    public void dispose()
    {
        super.dispose();

        removeListener(UI.instance);
    }
}
