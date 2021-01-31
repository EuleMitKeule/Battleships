package game;

import game.core.*;

public class LocalMatch extends Match
{

    public LocalMatch()
    {
        super();

        leftPlayer = new Human("You", this);

        invokePlayerAdded(leftPlayer, true);

        rightPlayer = new Computer("Enemy", this);

        ((Computer)rightPlayer).start();

        invokePlayerAdded(rightPlayer, false);

        if (UI.instance != null)
        {
            UI.instance.loadGame(leftPlayer.name, rightPlayer.name);
        }
        
    }

    public LocalMatch(IMatchListener listener, boolean debug)
    {
        super();
        
        addListener(listener);

        leftPlayer = new Human("You", this);

        invokePlayerAdded(leftPlayer, true);

        rightPlayer = debug ? new Enemy("Enemy", this) : new Computer("Enemy", this);

        if (!debug) ((Computer)rightPlayer).start();

        invokePlayerAdded(rightPlayer, false);

        addListener(UI.instance);

        if (UI.instance != null)
        {
            UI.instance.loadGame(leftPlayer.name, rightPlayer.name);
        }
        
    }

    @Override
    protected void invokeGameOver(Result result)
    {
        super.invokeGameOver(result);
        if (UI.instance != null)
        {
        UI.instance.loadEnd(result, result == Result.WIN_LEFT ? leftPlayer.name : rightPlayer.name, true, true);
        }
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
