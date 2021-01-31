package game;

import game.networking.MatchConnection;
import game.networking.*;
import game.core.*;

public class ServerMatch extends Match implements IMatchConnectionListener
{
    MatchConnection matchConnection;

    /**
     * @param leftPlayerName The name of the left player
     * @param rightPlayerName The name of the right player
     * @param matchConnection The matchConnection object
     */
    public ServerMatch(String leftPlayerName, String rightPlayerName, MatchConnection matchConnection)
    {
        super();
        
        leftPlayer = new Enemy(leftPlayerName, this);
        rightPlayer = new Enemy(rightPlayerName, this);

        this.matchConnection = matchConnection;
        matchConnection.addListener(this);
    }

    @Override
    public void onClientBoardReceived(String playerName, Board board)
    {
        super.onClientBoard(getPlayer(playerName), board);
    }

    @Override
    public void onMoveReceived(String playerName, Vector2Int cellPos)
    {
        super.onMove(getPlayer(playerName), cellPos);
    }
    
    /** 
     * Invokes the Update event
     * @param lastPlayer The player that guessed before
     * @param nextPlayer The player that will be guessing next
     * @param position The position that was guessed at
     * @param isHit Whether the guess hit a ship
     * @param isSunk Whether the guess sunk a ship
     * @param isLate Whether the move was made due to the round timer running out
     */
    protected void invokeUpdate(Player lastPlayer, Player nextPlayer, Vector2Int cellPos, boolean isHit, boolean isSunk, boolean isLate)
    {
        super.invokeUpdate(lastPlayer, nextPlayer, cellPos, isHit, isSunk, isLate);
        matchConnection.sendUpdate(lastPlayer, nextPlayer, cellPos, isHit, isSunk, isLate);
    }
 
    /** 
     * Invokes the GameOver event
     * @param result The result of the game
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
     * Invokes the GameSetup event
     * @param player The player whoose game gets set up
     */
    protected void invokeGameSetup(Player player)
    {
        super.invokeGameSetup(player);

        matchConnection.sendGameSetup(player);
    }

    /** 
     * Returns the player object by the name
     * @param name The name of the player
     * @return Player
     */
    private Player getPlayer(String name)
    {
        if (leftPlayer.name.equals(name)) return leftPlayer;
        else if (rightPlayer.name.equals(name)) return rightPlayer;
        else return null;
    }

    /**
     * @param playerName The name of the player
     * @return The player object with the specified name or null if none is found
     */
    @Override
    public void dispose()
    {
        super.dispose();

        matchConnection.removeListener(this);
    }
}
