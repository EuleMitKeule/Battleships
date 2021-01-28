package game;

public class RoundTimer 
{
    private Match match;
    private Thread thread;

    /**
     * Assigns the match to this.match
     * @invariant match != null
     * @param match The current match
     */
    public RoundTimer(Match match)
    {
        this.match = match;
    }

    /**
     * Starts the round timer
     */
    public void start()
    {
        thread = new Thread(new Runnable()
        {
            @Override
            public void run() 
            {
                try 
                {
                    Thread.sleep(30000);
                    match.onRoundTimerStopped();
                } 
                catch (InterruptedException e)
                {

                }
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    /**
     * Stops the timer.
     */
    public void stop()
    {
        if (thread != null) thread.interrupt();
    }

    /**
     * Restarts the round timer
     */
    public void restart()
    {
        stop();
        start();
    }
}