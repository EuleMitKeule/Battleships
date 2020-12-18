package game;

public interface IMatchListener
{
    void onPlacingPlayerChanged(Player player);

    void onGuessingPlayerChanged(Player player);
}
