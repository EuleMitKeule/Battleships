package game.networking;

import game.Player;
import game.Result;
import game.core.Vector2Int;

public interface IClientListener
{    
    default void onGameSetupReceived(String nextPlayerName) { }

    default void onUpdateReceived(String lastPlayerName, String nextPlayerName, Vector2Int cellPos, boolean isHit, boolean isSunk, boolean isLate) { }

    default void onGameOverReceived(Result result) { }
}