package game;

import game.networking.MatchConnection;
import game.networking.*;
import game.core.*;

public class ServerMatch extends Match implements IMatchConnectionListener
{
    MatchConnection matchConnection;

    public ServerMatch(String leftPlayerName, String rightPlayerName, MatchConnection matchConnection)
    {
        super();
        
        leftPlayer = new Enemy(leftPlayerName, this);
        rightPlayer = new Enemy(rightPlayerName, this);

        this.matchConnection = matchConnection;
        matchConnection.addListener(this);
    }

    
    /** 
     * @requires playerName != null
     * @param playerName
     * @param board
     */
    public void onClientBoardReceived(String playerName, Board board)
    {
        super.onClientBoard(getPlayer(playerName), board);
    }

    
    /** 
     * @requires playerName != null
     * @requires cellPos != null
     * @param playerName
     * @param cellPos
     */
    public void onMoveReceived(String playerName, Vector2Int cellPos)
    {
        super.onMove(getPlayer(playerName), cellPos);
    }

    
    /** 
     * @param lastPlayer
     * @param nextPlayer
     * @param cellPos
     * @param isHit
     * @param isSunk
     * @param isLate
     */
    protected void invokeUpdate(Player lastPlayer, Player nextPlayer, Vector2Int cellPos, boolean isHit, boolean isSunk, boolean isLate)
    {
        super.invokeUpdate(lastPlayer, nextPlayer, cellPos, isHit, isSunk, isLate);
        matchConnection.sendUpdate(lastPlayer, nextPlayer, cellPos, isHit, isSunk, isLate);
    }

    
    /** 
     * @param result
     */
    protected void invokeGameOver(Result result)
    {
        super.invokeGameOver(result);
        if (result == Result.WIN_LEFT)
        {
            matchConnection.sendGameOver(leftPlayer.name, true);
        }
        else if (result == Result.WIN_RIGHT) 
        {
            matchConnection.sendGameOver(rightPlayer.name, true);
        }
        else matchConnection.sendGameOver("", true);
    }

    
    /** 
     * @param player
     */
    protected void invokeGameSetup(Player player)
    {
        super.invokeGameSetup(player);

        matchConnection.sendGameSetup(player);
    }

    
    /** 
     * @param name
     * @return Player
     */
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
