import java.util.concurrent.Locks;

class BoundedBuffer {
  int buffer[];
  int iPuts;
  int iGets;
  int bufferSize = 0;
  int numElems = 0;

  Lock l = new ReentrantLock();
  Condition Empty = l.newCondition();
  Contidion Full = l.newCondition();

  public BoundedBuffer(int N) {
    this.buffer = new int[N];
    this.bufferSize = N;
  }

  public void put(int item) {
    l.lock();
    try {
      while (numElems == 0)
        Empty.await();

      buffer.put(item);
      this.numElems += 1;
    }

    finally {
      l.unlock();
    }
  }

  public T get() {
    l.lock();

    try {
      while (numElems == N)
        Full.await();


      
      this.numElems-=1;
      return buffer.get();

    } finally {
      l.unlock();
    }
  }

}
