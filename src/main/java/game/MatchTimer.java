package game;

public class MatchTimer 
{
    private Match match;
    private Thread thread;
    
    /**
     * Assigns the match to this.match
     * @invariant match != null
     * @param match The current match
     */
    public MatchTimer(Match match)
    {
        this.match = match;
    }
    
    /**
     * Starts the match timer
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
                    Thread.sleep(300000);
                    match.onMatchTimerStopped();
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
     * Stops the match timer
     */
    public void stop()
    {
        if (thread != null) thread.interrupt();
    }

    /**
     * Restarts the match timer
     */
    public void restart()
    {
        stop();
        start();
    }
}
