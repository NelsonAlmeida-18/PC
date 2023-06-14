import java.util.concurrent.Semaphore;
import java.util.Random;

class BoundedBuffer{
    private int size;
    private int numOfItems=0;
    private Semaphore items;
    private Semaphore slots;
    private int[] buffer;

    public BoundedBuffer(int size){
        this.size = size;
        this.items = new Semaphore(0);
        this.slots = new Semaphore(size);
        this.buffer = new int[size];
    }

    public int get() throws InterruptedException {
        this.items.acquire();
        int valor = buffer[numOfItems];
        numOfItems = (numOfItems + 1) % size;
        this.slots.release();
        return valor;
    }
    
    public void put(int x) throws InterruptedException {
        this.slots.acquire();
        buffer[numOfItems] = x;
        numOfItems = (numOfItems + 1) % size;
        this.items.release();
    }
    

}

class Barreira{
    private Semaphore threads;
    private Semaphore mutual= new Semaphore(1);
    private int counter=0;
    private int N;

    public Barreira(int N){
        this.threads = new Semaphore(0);
        this.N = N;
    }

    public void await() throws InterruptedException{
        this.mutual.acquire();
        this.counter++;
        int temp = counter;
        this.mutual.release();

        if(temp==N){
            //liberta todas as threads
            threads.release(N);
        }
        else{
            threads.acquire();
        }
    }


}

public class guiao4 {
    
    public static void ex1(){

        BoundedBuffer bb = new BoundedBuffer(10);
        Random rd = new Random();

        Thread td = new Thread(() -> {
            try{
                int i=0;
                while(i<10){
                    int op = rd.nextInt(2);
                    if(op==0){
                        System.out.println("Trying to get in "+i);
                        int val = bb.get();
                        System.out.println("Got item "+val);
                    }
                    else{
                        System.out.println("Trying to put "+i);
                        bb.put(rd.nextInt(10));
                        System.out.println("Put item");
                    }
                    i++;
                }
            }
            catch(Exception e){e.printStackTrace();}
        });

        try {
            td.start();
            td.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ex2(){
        try{
            int N = 10;

            Barreira b = new Barreira(N);

            for(int i=0; i<N;i++){
                new Thread( () -> {
                    try{
                        System.out.println("Thread  waiting");
                        b.await();
                        System.out.println("Thread released");
                    }
                    catch(Exception e){e.printStackTrace();}
                }).start();
                Thread.sleep(200);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String [] args){
        ex1();
    }

}
