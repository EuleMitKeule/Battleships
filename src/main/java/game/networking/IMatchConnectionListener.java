package game.networking;

import game.core.Vector2Int;
import game.Board;

public interface IMatchConnectionListener
{
    /**
     * Invoked when the client sent a client board message
     * @param playerName The name of the player that owns the board
     * @param board The board object that was sent
     */
    default void onClientBoardReceived(String playerName, Board board) { }
    
    /**
     * Invoked when the client sent a move message
     * @param playerName The name of the player that made the move
     * @param cellPos The position the move was made at
     */
    default void onMoveReceived(String playerName, Vector2Int cellPos) { }
}
