package game;

public class RoundTimer 
{
    private Match match;
    private Thread thread;

    public RoundTimer(Match match)
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
                    Thread.sleep(30000);
                    match.onRoundTimerStopped();
                } 
                catch (Exception e) { }
            }
        });

        thread.start();
    }

    public void stop()
    {
        thread.interrupt();
    }

    public void restart()
    {
        stop();
        start();
    }
}