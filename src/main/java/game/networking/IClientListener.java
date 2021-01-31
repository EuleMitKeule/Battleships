package game.networking;

import game.Result;
import game.core.Vector2Int;

public interface IClientListener
{    
    /**
     * Invoked when the server started the match
     * @param nextPlayerName The name of the beginning player
     */
    default void onGameSetupReceived(String nextPlayerName) { }

    /**
     * Invoked when the server sent an update message
     * @param lastPlayerName The player that made the move
     * @param nextPlayerName The player that will guess next
     * @param cellPos The position the move was made at
     * @param isHit Whether the move hit a ship
     * @param isSunk Whether the move sunk a ship
     * @param isLate Whether the move was made due to the round timer running out
     */
    default void onUpdateReceived(String lastPlayerName, String nextPlayerName, Vector2Int cellPos, boolean isHit, boolean isSunk, boolean isLate) { }

    /**
     * Invoked when the server sent a game over message
     * @param result The result type of the match
     * @param isRegularWin Whether the game was won by score or due to the match timer running out
     */
    default void onGameOverReceived(Result result, boolean isRegularWin) { }
}