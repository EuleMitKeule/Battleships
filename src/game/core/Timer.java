package game.core;

public class Timer
{
    private long lastTime = System.currentTimeMillis();
    private long thisTime;
    private double deltaTime = 0.0;

    public Timer() {

    }

    public void update() {
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