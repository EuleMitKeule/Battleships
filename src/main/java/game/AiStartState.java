package game;

import game.core.Vector2Int;

public class AiStartState implements IAiState
{
    private Computer _computer;

    /**
     * @param computer The computer object the state belongs to
     */
    public AiStartState(Computer computer)
    {
        this._computer = computer;
    }

    @Override
    public void enterState()
    {
        _computer.curDirection = Direction.Right;
    }

    @Override
    public void exitState()
    {

    }

    @Override
    public void onUpdate(Player lastPlayer, Player nextPlayer, Vector2Int position, boolean isHit, boolean isSunk)
    {
        var guessPos = _computer.getRandomGuessPos();

        _computer.lastGuessPos = guessPos;
        _computer.lastStartGuessPos = guessPos;
        _computer.invokeMove(guessPos);
    }
}
