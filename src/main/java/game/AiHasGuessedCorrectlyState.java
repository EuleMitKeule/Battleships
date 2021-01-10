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
            System.out.println("Schiff versenkt. Fängt von vorne an");
            _computer.setState(_computer.aiStartState);
            _computer.state.onUpdate(player, cellPos, false, false);
            return;
        }

        if (!isHit)
        {
            System.out.println("Nicht getroffen");
            _computer.setState(_computer.aiHasGuessedIncorrectlyState);
            _computer.state.onUpdate(player, cellPos, isHit, isSunk);
            return;
        }

        var rightPos = _computer.lastGuessPos.add(Vector2Int.right());
        var leftPos = _computer.lastGuessPos.add(Vector2Int.left());
        var upPos = _computer.lastGuessPos.add(Vector2Int.up());
        var downPos = _computer.lastGuessPos.add(Vector2Int.down());
        var nextPos = _computer.lastGuessPos.add(Direction.ToVector(_computer.curDirection));

        if (_computer.ownBoard.canGuess(nextPos))
        {
            System.out.println("Geht nach " + _computer.curDirection);
            _computer.lastGuessPos = nextPos;
            _computer.invokeMove(nextPos);
        }
        else
        {
            System.out.println("Ging nicht");
            _computer.setState(_computer.aiStartState);
            _computer.state.onUpdate(player, cellPos, false, false);
            return;
        }
        // if (_computer.ownBoard.canGuess(rightPos))
        // {
        //     _computer.lastGuessPos = rightPos;
        //     _computer.invokeMove(rightPos);
        // }
        // else if (_computer.ownBoard.canGuess(leftPos))
        // {
        //     _computer.lastGuessPos = leftPos;
        //     _computer.invokeMove(leftPos);
        // }
        // else if (_computer.ownBoard.canGuess(upPos))
        // {
        //     _computer.lastGuessPos = upPos;
        //     _computer.invokeMove(upPos);
        // }
        // else if (_computer.ownBoard.canGuess(downPos))
        // {
        //     _computer.lastGuessPos = downPos;
        //     _computer.invokeMove(downPos);
        // }
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