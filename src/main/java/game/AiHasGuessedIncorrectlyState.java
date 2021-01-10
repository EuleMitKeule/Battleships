package game;

import game.core.Vector2Int;

public class AiHasGuessedIncorrectlyState implements IAiState
{
    private Computer _computer;

    

    public AiHasGuessedIncorrectlyState(Computer computer)
    {
        this._computer = computer;
    }

    @Override
    public void onUpdate(Player player, Vector2Int cellPos, boolean isHit, boolean isSunk)
    {
        var lastStartGuessPos = _computer.lastStartGuessPos;

        var direction = Direction.ToVector(_computer.curDirection);
        var nextPos = lastStartGuessPos.add(direction);

        if (_computer.ownBoard.canGuess(nextPos))
        {
            System.out.println("went left");
            _computer.lastGuessPos = nextPos;
            _computer.invokeMove(nextPos);
        }
        else
        {
            System.out.println("random new pos");
            _computer.setState(_computer.aiStartState);
            _computer.state.onUpdate(player, cellPos, false, false);
            return;
        }
    }

    @Override
    public void enterState()
    {
        _computer.curDirection = Direction.values()[(_computer.curDirection.ordinal() + 1) % Direction.values().length];
    }

    @Override
    public void exitState()
    {

    }
}
