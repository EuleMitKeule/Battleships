package game.core;

import game.IMatchListener;
import game.Match;
import game.Player;
import game.ShipType;

import javax.swing.*;
import java.awt.*;

public class UI implements IMatchListener
{
    private Game game;
    
    private JLabel shipCount;
    private JLabel leftPlayerName;
    private JLabel rightPlayerName;

    public UI (Game game)
    {
        this.game = game;

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

        Match.addListener(this);
    }


    @Override
    public void onPlayerAdded(Player player, boolean isLeftPlayer)
    {
        var label = isLeftPlayer ? leftPlayerName : rightPlayerName;

        label.setText(player.getName());
    }
    
    @Override
    public void onShipCountChanged(int shipCount, boolean isLeft)
    {
    	if(!isLeft) this.shipCount.setText("" + shipCount);
    }
}
