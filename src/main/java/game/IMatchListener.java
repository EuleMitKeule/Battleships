package game;

import game.core.Vector2Int;
public interface IMatchListener
{
    /**
     * Invoked when the GameSetup is made
     * @param player The player whoose game is set up
     */
    default void onGameSetup(Player player) { }

    /** 
     * Invoked when a player made a move
     * @param lastPlayer The player that guessed before
     * @param nextPlayer The player that will be guessing next
     * @param position The position that was guessed at
     * @param isHit Whether the guess hit a ship
     * @param isSunk Whether the guess sunk a ship
     * @param isLate Whether the move was made due to the round timer running out
     */
    default void onUpdate(Player lastPlayer, Player nextPlayer, Vector2Int position, boolean isHit, boolean isSunk, boolean isLate) { }

    /**
     * Invoked when a player is added to the game
     * @param player The added player
     * @param isLeftPlayer Whether the player is the left player
     */
    default void onPlayerAdded(Player player, boolean isLeftPlayer) { }

    /**
     *  Invoked when the name already exists in the game
     */
    default void onNameExists() { }

    /**
     * Invoked when the game is over and a player won
     * @param result The Result of the game
     */
    default void onGameOver(Result result) { }

    /**
     * Invoked when the score of the player is changed
     * @param leftScore The score of the left player
     * @param rightScore The score of the right player
     */
    default void onScoreChanged(int leftScore, int rightScore) { }

    /**
     * Invoked when the ShipCount of a player changed
     * @param leftShipCount The number of ships the left player has left
     * @param rightShipCount The number of ships the right player has left
     */
    default void onShipCountChanged(int leftShipCount, int rightShipCount) { }

}
