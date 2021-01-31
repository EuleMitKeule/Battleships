package game;

import game.core.Vector2Int;

public interface IAiState
{
    /**
     * Invoked when the AI should make a move
     * @param lastPlayer The player that guessed before
     * @param nextPlayer The player that will be guessing next
     * @param position The position that was guessed at
     * @param isHit Whether the guess hit a ship
     * @param isSunk Whether the guess sunk a ship
     */
    void onUpdate(Player lastPlayer, Player nextPlayer, Vector2Int position, boolean isHit, boolean isSunk);

    /**
     * Invoked when this state is entered
     */
    void enterState();

    /**
     * Invoked when this state is exited
     */
    void exitState();
}
