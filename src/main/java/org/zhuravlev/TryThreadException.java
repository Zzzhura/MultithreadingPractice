package org.zhuravlev;

public class TryThreadException {
  public static void TryWaitThreadException(Object obj) {
    try {
      obj.wait();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
