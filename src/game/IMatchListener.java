package game;

public interface IMatchListener
{
    default void onPlacingPlayerChanged(Player player, ShipType shipType)
    {

    }

    default void onGuessingPlayerChanged(Player player)
    {

    }

    default void onPlayerAdded(Player player, boolean isLeftPlayer)
    {

    }
}
