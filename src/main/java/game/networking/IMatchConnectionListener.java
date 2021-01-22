package game.networking;

import game.core.Vector2Int;
import game.Board;

public interface IMatchConnectionListener
{
    default void onClientBoardReceived(String playerName, Board board) { }
    
    default void onMoveReceived(String playerName, Vector2Int cellPos) { }
}
