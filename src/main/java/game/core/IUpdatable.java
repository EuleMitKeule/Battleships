package game.core;

public interface IUpdatable
{
    default void update(long elapsedMillis) { }
}
