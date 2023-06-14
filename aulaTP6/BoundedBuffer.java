
class BoundedBuffer {
  private int N;
  private int counter = 0;

  public BoundedBuffer(int N) {
    this.N = N;
  }

  public synchronized int get() throws InterruptedException {

  }

  public synchronized void await() throws InterruptedException {
    counter += 1; 
  if (c < N) {
      while (c < N) {
        wait();
      }
    } else {

      notifyAll();
    }

    while (c < N) {
      wait();
    }
    notifyAll();

  }

}
