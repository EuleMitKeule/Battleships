package game;

import game.core.Vector2Int;

public interface IPlayerListener
{
    /** 
     * Invoked when the player has assigned his board
     * @param player The player that owns the board
     * @param board The board object that was assigned
     */
    default void onClientBoard(Player player, Board board) { }
    /**
     * Invoked when the player made a move
     * @param player player who made the move
     * @param position position of the move made
     */
    default void onMove(Player player, Vector2Int position) { }
}
