package game;

import game.core.UI;
import game.core.Vector2Int;
import game.networking.*;

public class ClientMatch extends Match implements IClientListener
{

    /**
     * Creates a client match object for the online mode
     * @param name The name of the first player
     * @param enemyName The name of the second Player
     * @param isComputer Whether one of the players is a computer player
     */
    public ClientMatch(String name, String enemyName, boolean isComputer)
    {
        super();
        UI.instance.loadGame(name, enemyName);
        addListener(UI.instance);
        leftPlayer = isComputer ? new Computer(name, this) : new Human(name, this);
        rightPlayer = new Enemy(enemyName, this);

        if (isComputer) ((Computer)leftPlayer).start();
    }

    @Override
    public void onGameSetupReceived(String nextPlayerName)
    {
        invokeGameSetup(getPlayer(nextPlayerName));
    }

    @Override
    public void onUpdateReceived(String lastPlayerName, String nextPlayerName, Vector2Int cellPos, boolean isHit, boolean isSunk, boolean isLate)
    {
        invokeUpdate(getPlayer(lastPlayerName), getPlayer(nextPlayerName), cellPos, isHit, isSunk, isLate);
        var isLeftPlayer = lastPlayerName.equals(leftPlayer.name); 
        System.out.println(leftPlayer.name);
        System.out.println(rightPlayer.name);
        System.out.println(lastPlayerName);
        System.out.println(isLeftPlayer);


        if (isHit)
        {
            if (!isLeftPlayer) rightScore += 1;
            else leftScore += 1;
        }
        if (isSunk)
        {
            if(!isLeftPlayer) 
            {
                rightScore += 1;
                leftShipCount -= 1;
            }
            else    
            {
                leftScore += 1;
                rightShipCount -= 1;
            }
        }
        invokeShipCountChanged(leftShipCount, rightShipCount);
        invokeScoreChanged(leftScore, rightScore);
    }

    @Override
    public void onClientBoard(Player player, Board board)
    {
        ClientConnection.instance.sendClientBoard(board);
    }
    
    @Override
    public void onMove(Player player, Vector2Int cellPos)
    {
        ClientConnection.instance.sendMove(cellPos);
    }

    @Override
    public void onGameOverReceived(Result result, boolean isRegularWin)
    {
        invokeGameOver(result);
        UI.instance.loadEnd(result, result == Result.WIN_LEFT ? leftPlayer.name : rightPlayer.name, false, isRegularWin);
        dispose();
    }

    /**
     * @param playerName The name of the player
     * @return The player object with the specified name or null if none is found
     */
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
