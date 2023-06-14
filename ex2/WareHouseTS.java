package WareHouseTS;

import java.util.HashMap;
import java.util.concurrent.locks.*;

class Item {
  int qtt;
  Condition c;

  public Item(int N, Lock lock) {
    this.qtt = N;
    this.c = lock.newCondition();
  }

  public synchronized void getItem() {
    this.qtt--;
  }

  public synchronized void addItem(int N) {
    this.qtt += N;
  }
}

class WareHouse {
  HashMap<String, Item> db = new HashMap<String, Item>();
  Lock l = new ReentrantLock();

  public void supply(String itemName, int qtt) {
    l.lock();
    try {
      Item i = this.get(itemName);
      i.addItem(qtt);
      i.c.signalAll();
    } finally {
      l.unlock();
    }
  }

  public void consume(String[] items) throws InterruptedException {
    l.lock();
    try {
      int size = items.length;

      // lazy solution
      // caso o produto exista vai ficar com ele por muito que outro que ele quer não
      // exista
      for (String item : items) {
        Item it = this.get(item);
        while (it.qtt == 0) {
          it.c.await();
        }
        it.getItem();
      }

      // best solution
      // 1. verificar que todos os elementos que ele quer estão disponíveis,
      // 2. caso exista, então serve-o de todos os itens,
      // 3. caso contrário, vai esperar até que esteja disponível
    } finally {
      l.unlock();
    }
  }

  private Item get(String itemName) {
    // se o item estiver no hashmap, então, vou devolver
    if (!db.containsKey(itemName)) {
      // caso não esteja tem de ser criado
      Item item = new Item(0, l);
      this.db.put(itemName, item);
    }

    return this.db.get(itemName);
  }

}
