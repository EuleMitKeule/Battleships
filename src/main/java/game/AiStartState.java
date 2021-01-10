package game;

import game.core.Vector2Int;

public class AiStartState implements IAiState
{
    private Computer _computer;

    public AiStartState(Computer computer)
    {
        this._computer = computer;
    }

    public void enterState()
    {
        _computer.curDirection = Direction.Right;
    }

    public void exitState()
    {

    }

    @Override
    public void onUpdate(Player player, Vector2Int position, boolean isHit, boolean isSunk)
    {
        var guessPos = _computer.getRandomGuessPos();
        _computer.lastGuessPos = guessPos;
        _computer.lastStartGuessPos = guessPos;
        _computer.invokeMove(guessPos);
    }
}
