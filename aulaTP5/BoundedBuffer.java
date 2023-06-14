package buffer;

import java.util.concurrent.Semaphore;

class BoundedBuffer {

  private int[] buffer;
  private int numGets = 0;
  private int numPuts = 0;

  Semaphore mutualExclusion = new Semaphore(1);
  Semaphore items = new Semaphore(0);
  Semaphore slots;

  public BoundedBuffer(int N) {
    a = new int[N];
    Semaphore slots = new Semaphore(N);
  }

  public int get() throws InterruptedException {

    mutualExclusion.acquire();

    int v = a[numGets];
    numGets = (numGets + 1) % a.length;
    mutualExclusion.release();
    slots.release();
    return v;
  }

  p

    slots.acquire();mutualExclusion.acquire();a[numPuts]=v;

}

}
