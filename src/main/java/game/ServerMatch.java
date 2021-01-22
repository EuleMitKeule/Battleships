package game;

import game.networking.MatchConnection;
import game.networking.*;
import game.core.*;

public class ServerMatch extends Match implements IMatchConnectionListener
{
    MatchConnection matchConnection;

    public ServerMatch(MatchConnection matchConnection)
    {
        super();
        
        this.matchConnection = matchConnection;
        matchConnection.addListener(this);
    }

    public void onClientBoardReceived(String playerName, Board board)
    {
        super.onClientBoard(getPlayer(playerName), board);
    }

    public void onMoveReceived(String playerName, Vector2Int cellPos)
    {
        super.onMove(getPlayer(playerName), cellPos);
    }

    protected void invokeUpdate(Player lastPlayer, Player nextPlayer, Vector2Int cellPos, boolean isHit, boolean isSunk, boolean isLate)
    {
        super.invokeUpdate(lastPlayer, nextPlayer, cellPos, isHit, isSunk, isLate);
        matchConnection.sendUpdate(lastPlayer, nextPlayer, cellPos, isHit, isSunk, isLate);
    }

    protected void invokeGameOver(Result result)
    {
        super.invokeGameOver(result);
        matchConnection.sendGameOver(result);
    }

    protected void invokeGameSetup(Player player)
    {
        super.invokeGameSetup(player);
        matchConnection.sendGameSetup(player);
    }

    private Player getPlayer(String name)
    {
        if (leftPlayer.name.equals(name)) return leftPlayer;
        else if (rightPlayer.name.equals(name)) return rightPlayer;
        else return null;
    }

    @Override
    public void dispose()
    {
        super.dispose();

        matchConnection.removeListener(this);
    }
}
