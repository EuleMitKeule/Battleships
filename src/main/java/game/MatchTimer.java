package game;

public class MatchTimer 
{
    private Match match;
    private Thread thread;
    
    /**
     * @param match
     */
    public MatchTimer(Match match)
    {
        this.match = match;
    }
    
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

    public void stop()
    {
        if (thread != null) thread.interrupt();
    }

    public void restart()
    {
        stop();
        start();
    }
}
