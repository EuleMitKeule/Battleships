package game.core;

public interface IRenderable
{
    default void render(BoardRenderer renderer) { }
}
