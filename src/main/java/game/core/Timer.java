package game.core;

public class Timer
{
    private long lastTime = System.currentTimeMillis();
    private long thisTime;
    private double deltaTime = 0.0;

    /**
     * Updates the timer to the new current time
     * and calculates the elapsed time since last update call
     */
    public void update() 
    {
        thisTime = System.currentTimeMillis();
        deltaTime += (thisTime - lastTime) / 1000f;
        lastTime = thisTime;
    }

    public void reset() {
        deltaTime = 0.0;
    }

    public long getLastTime() {
        return lastTime;
    }

    public long getThisTime() {
        return thisTime;
    }

    public double getDeltaTime() {
        return deltaTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public void setThisTime(long thisTime) {
        this.thisTime = thisTime;
    }

    public void setDeltaTime(double deltaTime) {
        this.deltaTime = deltaTime;
    }
}