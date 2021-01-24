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
                    Thread.sleep(15000);
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