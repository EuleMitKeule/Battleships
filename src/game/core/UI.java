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

        game.add(leftPlayerName);
        game.add(rightPlayerName);

        Match.addListener(this);
    }


    @Override
    public void onPlayerAdded(Player player, boolean isLeftPlayer)
    {
        var label = isLeftPlayer ? leftPlayerName : rightPlayerName;

        label.setText(player.getName());
    }
}
