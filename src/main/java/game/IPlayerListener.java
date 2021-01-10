package game;

import game.core.Vector2Int;

public interface IPlayerListener
{
    default void onClientBoard(Player player, Board board) { }

    default void onMove(Player player, Vector2Int position) { }
}
