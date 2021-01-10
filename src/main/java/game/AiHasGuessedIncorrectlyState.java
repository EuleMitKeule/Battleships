package game;

public class AiHasGuessedIncorrectlyState implements AiState
{
    private Computer _computer;
    
    public AiHasGuessedIncorrectlyState(Computer computer)
    {
        this._computer = computer;
    }

    public void onGuessingPlayerChanged(Player player, boolean hasHit)
    {
        
    }
    
    public void enterState()
    {
        
    }

    public void exitState()
    {

    }
}
