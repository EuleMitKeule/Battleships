package game;

import game.core.UI;
import game.core.Vector2Int;
import game.networking.*;

public class ClientMatch extends Match implements IClientListener
{

    public ClientMatch(String name, String enemyName)
    {
        super();
        UI.instance.loadGame(name, enemyName);
        addListener(UI.instance);
        leftPlayer = new Human(name, this);
        rightPlayer = new Enemy(name, this);
    }

    public void onGameSetupReceived(String nextPlayerName)
    {
        invokeGameSetup(getPlayer(nextPlayerName));
    }

    public void onUpdateReceived(String lastPlayerName, String nextPlayerName, Vector2Int cellPos, boolean isHit, boolean isSunk, boolean isLate)
    {
        invokeUpdate(getPlayer(lastPlayerName), getPlayer(nextPlayerName), cellPos, isHit, isSunk, isLate);
        var isLeftPlayer = lastPlayerName == leftPlayer.name;
        
        if (isHit)
        {
            if (!isLeftPlayer) leftScore += 1;
            else rightScore += 1;
        }
        if (isSunk)
        {
            if(!isLeftPlayer) 
            {
                leftScore += 1;
                rightShipCount -= 1;
            }
            else    
            {
                rightScore += 1;
                leftShipCount -= 1;
            }
        }
        invokeShipCountChanged(leftShipCount, rightShipCount);
        invokeScoreChanged(leftScore, rightScore);
    }

    public void onClientBoard(Player player, Board board)
    {
        ClientConnection.instance.sendClientBoard(board);
    }

    public void onMove(Player player, Vector2Int cellPos)
    {
        ClientConnection.instance.sendMove(cellPos);
    }

    public void onGameOverReceived(Result result)
    {
        invokeGameOver(result);
        UI.instance.loadEnd(result, result == Result.WIN_LEFT ? leftPlayer.name : rightPlayer.name, false);
        dispose();
    }

    private Player getPlayer(String playerName)
    {
        if (leftPlayer.name.equals(playerName)) return leftPlayer;
        else if (rightPlayer.name.equals(playerName)) return rightPlayer;
        else return null;
    }

    @Override
    public void dispose()
    {
        super.dispose();

        removeListener(UI.instance);
    }
}
