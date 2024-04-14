package org.zhuravlev;

public class AbstractProgram implements Runnable {
  public State programStatus = State.UNKNOWN;
  public Sync sync;
  private DeamonTh deamonTh;

  public AbstractProgram(Sync sync) {
    this.sync = sync;
    this.deamonTh = new DeamonTh(this, sync);
    Thread deamonTh = new Thread(this.deamonTh);
    deamonTh.setDaemon(true);
    deamonTh.start();
  }

  public void setState(State state) {
    programStatus = state;
  }

  @Override
  public void run() {
    synchronized (this.sync) {
      do {
        while (sync.sync != 0) {
          TryThreadException.TryWaitThreadException(sync);
        }
        System.out.println(ColorsForOutput.ANSI_YELLOW +  "ABSTRACT PROGRAM STATUS IS: " +  ColorsForOutput.ANSI_GREEN + programStatus);
        sync.sync = 1;
        sync.notifyAll();
      } while (programStatus != State.FATAL_ERROR);
    }
  }
}
