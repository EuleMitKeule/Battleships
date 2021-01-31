package game.core;

public interface IUpdatable
{
    /**
     * Invoked every update frame
     * @param elapsedMillis The time elapsed since last update tick
     */
    default void update(long elapsedMillis) { }
}
