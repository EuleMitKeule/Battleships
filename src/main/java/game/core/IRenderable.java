package game.core;

import java.awt.Graphics;
public interface IRenderable
{
    default void render(Graphics graphics) { }
}
