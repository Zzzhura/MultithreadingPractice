package org.zhuravlev;

public class Supervisor implements Runnable {
    public Sync sync;
    public AbstractProgram abstractProgram;

    public Supervisor(AbstractProgram abstractProgram, Sync sync)
    {
        this.abstractProgram = abstractProgram;
        this.sync = sync;
    }
    public void runProgram(AbstractProgram abstractProgram)
    {
        State prevState = abstractProgram.programStatus;
        abstractProgram.programStatus = State.RUNNING;
        System.out.println(ColorsForOutput.ANSI_BLUE + "SUPERVISOR HAS CHANGED PREVIOUS CONDITION " + ColorsForOutput.ANSI_RED +  prevState + ColorsForOutput.ANSI_GREEN + "  TO -> " + ColorsForOutput.ANSI_YELLOW  + abstractProgram.programStatus + ColorsForOutput.ANSI_RESET);
    }

    public void stopProgram(AbstractProgram abstractProgram)
    {
        State prevState = abstractProgram.programStatus;
        abstractProgram.programStatus = State.STOPPING;
        System.out.println(ColorsForOutput.ANSI_BLUE + "SUPERVISOR HAS CHANGED PREVIOUS CONDITION " + ColorsForOutput.ANSI_RED +  prevState + ColorsForOutput.ANSI_GREEN + "  TO -> " + ColorsForOutput.ANSI_YELLOW + abstractProgram.programStatus + ColorsForOutput.ANSI_RESET);
    }

    private void changeStatus(AbstractProgram abstractProgram)
    {
        if(abstractProgram.programStatus == State.STOPPING)
        {
            stopProgram(abstractProgram);
        }
        else if(abstractProgram.programStatus == State.RUNNING)
        {
            runProgram(abstractProgram);
        }
    }

    @Override
    public void run()
    {
        synchronized (this.sync){
            do
            {
                while(sync.sync != 2)
                {
                    TryThreadException.TryWaitThreadException(sync);
                }
                changeStatus(abstractProgram);
                sync.sync = 0;
                sync.notifyAll();
            }
            while(abstractProgram.programStatus != State.FATAL_ERROR);
        }
    }
}
