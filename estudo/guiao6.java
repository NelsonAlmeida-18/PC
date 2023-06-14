import java.util.Scanner;
import java.util.concurrent.locks.*;
import java.util.HashMap;
import java.util.Random;

class BoundedBuffer_6{
    private int num;
    private int numElems;
    private int[] buffer;
    private Lock l;
    private Condition notFull;
    private Condition notEmpty;

    public BoundedBuffer_6(int N){
        this.num=N;
        this.numElems=0;
        this.buffer = new int[N];
        this.l = new ReentrantLock();
        this.notEmpty = this.l.newCondition();
        this.notFull = this.l.newCondition();
    }

    public int get() throws InterruptedException{
        l.lock();
        try{
            while(numElems==0) notEmpty.await();

            int value = buffer[numElems-1];
            numElems--;
            notFull.signal();
            return value;
        }finally{
            this.l.unlock();
        }
    }

    public void put(int x) throws InterruptedException {
        l.lock();
        try {
            System.out.println(numElems);
            while (numElems == num) notFull.await();
    
            buffer[numElems] = x;
            numElems++;
            notEmpty.signal();
        } finally {
            this.l.unlock();
        }
    }
    

}

class Warehouse{
    public HashMap<String, Integer> db;
    private Lock l = new ReentrantLock();
    private Condition notFull;
    private Condition notEmpty;
    private int numElems=0;
    private int limitOfWareHouse;
    
    public Warehouse(int dimension){
        this.db = new HashMap<String, Integer>(10);
        this.notFull = this.l.newCondition();
        this.notEmpty = this.l.newCondition();
        this.limitOfWareHouse=dimension;
    }

    public void supply(String item, int quantity) throws InterruptedException{
        this.l.lock();
        try{
            if(numElems==limitOfWareHouse) notFull.await();
            if(this.db.containsKey(item)){
                this.db.put(item, this.db.get(item)+quantity);
                this.numElems++;
                notEmpty.signalAll();
            }
            else{
                this.db.put(item, quantity);
                this.numElems++;
                notEmpty.signalAll();
            }
        }finally{
            this.l.unlock();
        }
    }

    public void consume(String item, int qtt) throws InterruptedException{
        this.l.lock();
        try{
            if(numElems==0) notEmpty.await();
            if(this.db.containsKey(item)){
                int numItems=this.db.get(item);
                System.out.println(numItems);
                while(numItems<qtt){
                    notEmpty.await();
                    numItems=this.db.get(item);
                }
                int newQtt = this.db.get(item)-qtt;
                this.db.put(item, newQtt);
                if(newQtt==0){
                    this.numElems--;
                    notFull.signalAll();
                }
            }
        }
        finally{this.l.unlock();}
    }
}


public class guiao6 {
    
    public static void ex1(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Tamanho do buffer: ");
        int size = sc.nextInt();

        BoundedBuffer_6 bb = new BoundedBuffer_6(size);
        Random random = new Random();

        Thread Produtor = new Thread(() -> {
            try{
                int producao;
                while(true){
                    producao = random.nextInt(100);
                    bb.put(producao);
                    System.out.println("Foram produzidos "+ producao);
                    Thread.sleep(1000);
                }
            }catch(Exception e){}

        });

        Thread Consumidor = new Thread(() -> {
            try{
                while(true){
                    int valor = bb.get();
                    System.out.println("Foram consumidas "+ valor);
                    Thread.sleep(1000);
                }
            }
            catch(Exception e){}
        });


        Produtor.start();
        Consumidor.start();

        sc.close();

    }


    public static void ex2(){
        Warehouse wh = new Warehouse(10);

        Thread Produtor = new Thread(() -> {
            try{
                int producao = 10;
                String item = "nabos";
                wh.supply(item, 10);
                System.out.println(wh.db.keySet().toString());
                System.out.println("Adicionados "+ producao+ " "+item);
                Thread.sleep(200);
            }catch(Exception e){
                e.printStackTrace();
            }
        });

        Thread Consumidor = new Thread(() -> {
            try{
                int consumo = 30;
                String item = "nabos";
                wh.consume(item, 10);
                System.out.println("Consumidos "+ consumo+ " "+item);
                Thread.sleep(200);
            }catch(Exception e){
                e.printStackTrace();
            }
        });

        Consumidor.start();
        Produtor.start();

        try{
            Consumidor.join();
            Produtor.join();
        }catch(Exception e){}


    }


    public static void main(String [] args){
        ex2();
    }

}
