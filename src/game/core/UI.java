package game.core;

import game.IMatchListener;
import game.Match;
import game.Player;

import javax.swing.*;
import java.awt.*;

public class UI implements IMatchListener
{    
    private JLabel shipCount;
    private JLabel leftPlayerName;
    private JLabel rightPlayerName;

    /**
     * @param game The game window context
     * @param match The match context
     */
    public UI (Game game, Match match)
    {
        var labelFont = new JLabel().getFont();

        leftPlayerName = new JLabel("");
        leftPlayerName.setBounds(64, 16, 128, 32);
        leftPlayerName.setFont(new Font(labelFont.getName(), Font.BOLD, 32));

        rightPlayerName = new JLabel("");
        rightPlayerName.setBounds(768, 16, 128, 32);
        rightPlayerName.setFont(new Font(labelFont.getName(), Font.BOLD, 32));
        
        shipCount = new JLabel("");
        shipCount.setBounds(1376, 16, 128, 32);
        shipCount.setFont(new Font(labelFont.getName(), Font.BOLD, 32));

        game.add(leftPlayerName);
        game.add(rightPlayerName);
        game.add(shipCount);

        match.addListener(this);
    }

    /**
     * Gets invoked when a new player is added to the match context
     * @param player The player that was added
     * @param isLeftPlayer Whether the player is on the left side or the right side
     */
    @Override
    public void onPlayerAdded(Player player, boolean isLeftPlayer)
    {
        var label = isLeftPlayer ? leftPlayerName : rightPlayerName;

        label.setText(player.getName());
    }
    
    /**
     * Gets invoked when either ship count has changed
     * @param shipCount The new ship count
     * @param isLeft Whether the ship count is on the left or the right side
     */
    @Override
    public void onShipCountChanged(int shipCount, boolean isLeft)
    {
    	if(!isLeft) this.shipCount.setText("" + shipCount);
    }
}
