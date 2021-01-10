package game;

import game.core.Vector2Int;

public class AiHasGuessedCorrectlyState implements AiState
{
    private Computer _computer;

    public AiHasGuessedCorrectlyState(Computer computer)
    {
        this._computer = computer;
    }

    @Override
    public void onGuessingPlayerChanged(Player player, boolean hasHit)
    {
        var rightPos = _computer.lastGuessPos.add(Vector2Int.right());
        var leftPos = _computer.lastGuessPos.add(Vector2Int.left());
        var upPos = _computer.lastGuessPos.add(Vector2Int.left());

        if (_computer.match.inBounds(rightPos) && _computer.match.canGuess(_computer, rightPos))
        {
            _computer.lastGuessPos = rightPos;
            _computer.invokeFieldGuessed(rightPos);
        }
        else if (_computer.match.inBounds(leftPos) && _computer.match.canGuess(_computer, leftPos))
        {
            _computer.lastGuessPos = leftPos;
            _computer.invokeFieldGuessed(leftPos);
        }
        else
        {
            var guessPos = _computer.getRandomGuessPos();
            _computer.lastGuessPos = guessPos;
            _computer.invokeFieldGuessed(guessPos);
        }
    }

    @Override
    public void enterState()
    {

    }

    @Override
    public void exitState()
    {

    }
}