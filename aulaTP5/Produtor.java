import BoundedBuffer;

public class Produtor extends Thread {
  private BoundedBuffer bb;

  public Produtor(BoundedBuffer bb) {
    thib.bb = bb;
  }

  public void run() {
    try {
      for (int i = 0; i < 10000; i++) {
        sleep(1000);
        System.out.println("Put: " + i);
        this.bb.put(i);
        System.out.println("Put: " + i + " bem sucedido");
      }
    } catch (Exception e) {
    }
  }
}
