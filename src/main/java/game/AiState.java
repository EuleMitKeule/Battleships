package game;

public interface AiState
{
    void onGuessingPlayerChanged(Player player, boolean hasHit);

    void enterState();

    void exitState();
}
