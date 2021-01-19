package game;

import game.core.Vector2Int;
public interface IMatchListener
{
    default void onGameSetup(Player player) { }

    default void onUpdate(Player lastPlayer, Player nextPlayer, Vector2Int position, boolean isHit, boolean isSunk) { }

    default void onPlayerAdded(Player player, boolean isLeftPlayer) { }

    default void onGameOver(Result result) { }
}
