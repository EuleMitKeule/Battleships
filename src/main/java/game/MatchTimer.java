package game;

public class MatchTimer 
{
    private Match match;
    private Thread thread;

    public MatchTimer(Match match)
    {
        this.match = match;
    }

    public void start()
    {
        System.out.println("5 Minute Match Timer has started!");
        thread = new Thread(new Runnable()
        {
            @Override
            public void run() 
            {
                try 
                {
                    Thread.sleep(300000);
                    System.out.println("5 Minutes are over!");
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
        thread.interrupt();
    }

    public void restart()
    {
        stop();
        start();
    }
}
