package org.zhuravlev;


public class DeamonTh implements Runnable{
    private AbstractProgram program;
    public Sync sync;

    public DeamonTh(AbstractProgram abstractProgram, Sync sync)
    {
        this.program = abstractProgram;
        this.sync = abstractProgram.sync;
    }

    

    public static boolean isValueInArray(int[] array_of_data, int value)
    {
        boolean flag = false;
        for(int i = 0; i < array_of_data.length; ++i)
        {
            if (array_of_data[i] == value)
            {
                flag = true;
            }
        }
        return flag;
    }

    public State getNewState()
    {
        State state = this.program.programStatus;
        int[] stoppingValues = new int[]{0, 2, 3, 4};
        int[] runningValues = new int[]{5, 6, 7, 8};
        int[] fatalValues = new int[]{1, 9};
        int randomNumber = getRandomNumber();
        if (isValueInArray(stoppingValues, randomNumber))
        {
            return State.STOPPING;
        }
        else if (isValueInArray(runningValues, randomNumber))
        {
            return State.RUNNING;
        }
        else if (isValueInArray(fatalValues, randomNumber))
        {
            return State.FATAL_ERROR;
        }
        return state;
    }

    public int getRandomNumber()
    {
        int randomNumber = (int) (Math.random() * 10);
        return randomNumber;
    }

    @Override
    public void run()
    {
        synchronized (this.sync){
            do
            {
                while(sync.sync != 1)
                {
                    TryThreadException.TryWaitThreadException(sync);
                }
                program.setState(getNewState());
                System.out.println(ColorsForOutput.ANSI_RED +  "DEAMON HAS CHANGED STAUTS TO -> " + ColorsForOutput.ANSI_RESET + ColorsForOutput.ANSI_GREEN + program.programStatus + ColorsForOutput.ANSI_RESET);
                sync.sync = 2;
                sync.notifyAll();
            }
            while(program.programStatus != State.FATAL_ERROR);
        }
    }
}
