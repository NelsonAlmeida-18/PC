import BoundedBuffer;

public class Consumidor extends Thread {
  private BoundedBuffer bb;

  public Produtor(BoundedBuffer bb) {
    thib.bb = bb;
  }

  public void run() {
    try {
      for (int i = 0; i < 10000; i++) {
        sleep(1000);
        System.out.println("Get: " + i);
        int result = this.bb.get(i);
        System.out.println("Get: " + i + " bem sucedido " + result);
      }
    } catch (Exception e) {
    }
  }
}
