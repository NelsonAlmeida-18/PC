import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import java.util.HashMap;
import java.util.Random;

class BoundedBuffer_6_2 {

    private int numElems;
    private int limitElems;
    private int[] buffer;
    private Lock lock = new ReentrantLock();
    private Condition notFull;
    private Condition notEmpty;

    public BoundedBuffer_6_2(int N) {
        this.limitElems = N;
        this.numElems = 0;
        this.buffer = new int[N];
        this.notFull = this.lock.newCondition();
        this.notEmpty = this.lock.newCondition();
    }

    public int get() throws InterruptedException {
        lock.lock();
        try {
            while (numElems == 0)
                notEmpty.await();
    
            int valor = buffer[numElems - 1];
            numElems--;
            notFull.signalAll();
            return valor;
        } finally {
            lock.unlock();
        }
    }
    

    public void put(int x) throws InterruptedException {
        lock.lock();
        try {
            while (numElems == limitElems)
                notFull.await();

            buffer[numElems] = x;
            numElems = (numElems + 1) % limitElems;

            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }
}

class Warehouse_6_2 {

    private HashMap<String, Integer> db;
    private Lock lock = new ReentrantLock();
    private Condition notEmpty;

    public Warehouse_6_2() {
        this.db = new HashMap<>();
        this.notEmpty = this.lock.newCondition();
    }

    public void supply(String item, int quantity) {
        this.lock.lock();
        try {
            if (this.db.containsKey(item)) {
                this.db.put(item, this.db.get(item) + quantity);
            } else {
                this.db.put(item, quantity);
            }
            notEmpty.signalAll();
        } finally {
            this.lock.unlock();
        }
    }

    public void consume(String[] items) {
        this.lock.lock();
        try {
            for (String item : items) {
                try {
                    while (!this.db.containsKey(item)) {
                        this.notEmpty.await();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.db.remove(item);
            }
        } finally {
            this.lock.unlock();
        }
    }
}

public class guiao6_2 {

    public static void ex1() {
        int numThreads = 10;
        Random rd = new Random();
        int sizeOfBuffer = 5;
        BoundedBuffer_6_2 bb = new BoundedBuffer_6_2(sizeOfBuffer);

        for (int i = 0; i < numThreads; i++) {
            new Thread(() -> {
                int op = rd.nextInt(2);
                try {
                    if (op == 0) {
                        System.out.println("Colocou");
                        bb.put(rd.nextInt(10));
                    } else {
                        System.out.println("Tirou");
                        bb.get();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public static void ex2() {
        Warehouse_6_2 wh = new Warehouse_6_2();

        String[] items = {"Banana", "Maça", "Pêssego", "Alperce", "Madioca"};
        Random rd = new Random();

        int numThreads = 10;
        for (int i = 0; i < numThreads; i++) {
            int op = rd.nextInt(2);
            if (op == 0) {
                new Thread(() -> {
                    String item = items[rd.nextInt(items.length)];
                    int qtt = rd.nextInt(20);
                    System.out.println("Produzidos " + qtt + " " + item);
                    wh.supply(item, qtt);
                }).start();
            } else {
                new Thread(() -> {
                    String[] item = {items[rd.nextInt(items.length)]};
                    System.out.println("Consumidos " + item[0]);
                    wh.consume(item);
                }).start();
            }
        }
    }

    public static void main(String[] args) {
        ex2();
    }
}
