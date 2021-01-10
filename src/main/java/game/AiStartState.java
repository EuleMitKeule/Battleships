package game;

public class AiStartState implements AiState
{
    private Computer _computer;
    
    public AiStartState(Computer computer)
    {
        this._computer = computer;
    }
    
    public void onGuess()
    {
        var guessPos = _computer.getRandomGuessPos();
        _computer.lastGuessPos = guessPos;
        _computer.invokeFieldGuessed(guessPos);
    }
    
    public void enterState()
    {
        
    }

    public void exitState()
    {

    }
}
