package game;

import game.core.Vector2Int;

public class AiHasGuessedCorrectlyState implements IAiState
{
    private Computer _computer;

    public AiHasGuessedCorrectlyState(Computer computer)
    {
        this._computer = computer;
    }

    @Override
    public void onUpdate(Player player, Vector2Int cellPos, boolean isHit, boolean isSunk)
    {
        if (isSunk)
        {
            _computer.setState(_computer.aiStartState);
            _computer.state.onUpdate(player, cellPos, false, false);
            return;
        }

        if (!isHit)
        {
            _computer.setState(_computer.aiHasGuessedIncorrectlyState);
            _computer.state.onUpdate(player, cellPos, isHit, isSunk);
            return;
        }

        var rightPos = _computer.lastGuessPos.add(Vector2Int.right());
        var leftPos = _computer.lastGuessPos.add(Vector2Int.left());
        var upPos = _computer.lastGuessPos.add(Vector2Int.up());
        var downPos = _computer.lastGuessPos.add(Vector2Int.down());

        if (_computer.match.inBounds(rightPos) && _computer.match.canGuess(_computer, rightPos))
        {
            _computer.lastGuessPos = rightPos;
            _computer.invokeMove(rightPos);
        }
        else if (_computer.match.inBounds(leftPos) && _computer.match.canGuess(_computer, leftPos))
        {
            _computer.lastGuessPos = leftPos;
            _computer.invokeMove(leftPos);
        }
        else if (_computer.match.inBounds(upPos) && _computer.match.canGuess(_computer, upPos))
        {
            _computer.lastGuessPos = upPos;
            _computer.invokeMove(upPos);
        }
        else if (_computer.match.inBounds(downPos) && _computer.match.canGuess(_computer, downPos))
        {
            _computer.lastGuessPos = downPos;
            _computer.invokeMove(downPos);
        }
        else
        {
            _computer.setState(_computer.aiStartState);
            _computer.state.onUpdate(player, cellPos, false, false);
            return;
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