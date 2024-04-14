package org.zhuravlev;

public class Main {
    public static void main(String[] args){
        Sync sync = new Sync();
        AbstractProgram abstractProgram = new AbstractProgram(sync);
        Supervisor supervisor = new Supervisor(abstractProgram, sync);
        Thread thread = new Thread(supervisor);
        Thread Program = new Thread(abstractProgram);
        Program.start();
        thread.start();
    }
}
