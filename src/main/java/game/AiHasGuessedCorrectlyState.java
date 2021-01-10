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
    public void onUpdate(Player lastPlayer, Player nextPlayer, Vector2Int cellPos, boolean isHit, boolean isSunk)
    {
        if (isSunk)
        {
            System.out.println("Schiff versenkt.");
            _computer.setState(_computer.aiStartState);
            _computer.state.onUpdate(lastPlayer, nextPlayer, cellPos, false, false);
            return;
        }

        if (!isHit)
        {
            System.out.println("Nicht getroffen.");
            _computer.setState(_computer.aiHasGuessedIncorrectlyState);
            _computer.state.onUpdate(lastPlayer, nextPlayer, cellPos, isHit, isSunk);
            return;
        }

        var nextPos = _computer.lastGuessPos.add(Direction.ToVector(_computer.curDirection));

        if (_computer.enemyBoard.canGuess(nextPos))
        {
            System.out.println("Guess in Richtung " + _computer.curDirection);
            _computer.lastGuessPos = nextPos;
            _computer.invokeMove(nextPos);
        }
        else
        {
            System.out.println("Kein guess mehr möglich, starte von vorne");
            _computer.setState(_computer.aiStartState);
            _computer.state.onUpdate(lastPlayer, nextPlayer, cellPos, false, false);
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