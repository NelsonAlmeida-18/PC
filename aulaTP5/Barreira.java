import java.util.concurrent.Semaphore;

class Barreira {

  private Semaphore s = new Semaphore(0);
  private Semaphore mut = new Semaphore(1);

  private int counter = 0;
  private final int N;

  public Barreira(int N) {
    this.N = N;
  }

  public void await() throws InterruptedException {
    mut.acquire();
    counter += 1;
    boolean last = counter == N;
    mut.release();

    if (last) {
      for (int i = 0; i < N - 1; i++) {
        s.release();
      }
    } else {
      s.acquire();
    }

  };

}

class Main {
  public static void main(String[] args) {
    Barreira b = new Barreira(3);
    for (int i = 0; i < 3; i++) {
      int j = i;
      new Thread(() -> {
        try {
          Thread.sleep(j * 1000);
          System.out.println("Antes");
          b.await();
          System.out.println("Depois");
        } catch (InterruptedException e) {
        }
        ;
      }).start();
    }
  }
}
