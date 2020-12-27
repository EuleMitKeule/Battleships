package game;

public interface IMatchListener
{
    default void onPlacingPlayerChanged(Player player, ShipType shipType) { }

    default void onGuessingPlayerChanged(Player player, boolean hasHit) { }

    default void onPlayerAdded(Player player, boolean isLeftPlayer) { }
    
    default void onShipCountChanged(int shipCount, boolean isLeft) { }
}
