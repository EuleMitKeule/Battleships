package game.core;

import java.awt.Graphics;
public interface IRenderable
{
    /**
     * Invoked every render frame
     * @param graphics The graphics context
     */
    default void render(Graphics graphics) { }
}
