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
        System.out.println("5 Second Round Timer has started!");
        thread = new Thread(new Runnable()
        {
            @Override
            public void run() 
            {
                try 
                {
                    Thread.sleep(5000);
                    System.out.println("5 Seconds are over!");
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
        thread.interrupt();
    }

    public void restart()
    {
        stop();
        start();
    }
}