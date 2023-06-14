import java.util.concurrent.Semaphore;
import java.util.Random;

class BoundedBuffer_4_2{

    private int n;
    private int itemsInBuffer;
    private int[] buffer;
    private Semaphore slots;
    private Semaphore items;
    private Semaphore mutex;

    public BoundedBuffer_4_2(int size){
        this.n=size;
        this.itemsInBuffer=0;
        this.buffer = new int[size];
        this.items = new Semaphore(0);
        this.slots = new Semaphore(size);
        this.mutex = new Semaphore(1);
    }

    public int get() throws InterruptedException{
        this.items.acquire();
        this.mutex.acquire();
        int valor = buffer[0];
        this.itemsInBuffer-=1;
        this.slots.release();
        this.mutex.release();
        return valor;
    }

    public void put(int x) throws InterruptedException{
        this.slots.acquire();
        this.mutex.acquire();
        this.buffer[this.itemsInBuffer] = x;
        this.itemsInBuffer = (this.itemsInBuffer+1)%n;
        this.items.release();
        this.mutex.release();
    }
}

class Barreira_4_2{

    private Semaphore threadBlocker;
    private int N;
    private int numThreadsBlocked=0;

    public Barreira_4_2(int N){
        this.threadBlocker=new Semaphore(0, false);
        this.N=N;
    }

    public void await() throws InterruptedException{
        this.numThreadsBlocked++;
        this.threadBlocker.acquire();

        if(this.numThreadsBlocked == this.N){
            this.threadBlocker.release(N);
        }

        numThreadsBlocked=0;
    }
}


public class guiao4_2 {
    
    public static void ex1(){
        BoundedBuffer_4_2 bb = new BoundedBuffer_4_2(10);
        Random rd = new Random();
        try{
            for(int i=0;i<10;i++){
                if(rd.nextInt(2)==0){
                    new Thread( () -> {
                        try{
                            int value = rd.nextInt(100);
                            bb.put(value);
                            System.out.println("Inserted "+value);
                            Thread.sleep(200);
                        }
                        catch(Exception e){}
                        
                    }).start();
                    
                }
                else{
                    new Thread( () -> {
                        try{
                            int result = bb.get();
                            System.out.println("Got "+result);
                            Thread.sleep(200);
                        }
                        catch(Exception e){}
                    }).start();
                }
                Thread.sleep(200);
            }
        }
        catch(Exception e){}
    }

    public static void ex2(){

        int N=10;
        Barreira b = new Barreira(N);
        Random rd = new Random();

        for(int i=0; i<N;i++){
            try{
                new Thread(() -> {
                    try{
                        System.out.println("Thread iniciada");
                        b.await();
                        System.out.println("Thread Terminada");
                    }
                    catch(Exception e){e.printStackTrace();}
                }).start();
                Thread.sleep(rd.nextInt(1000));
            }
            catch(Exception e){e.printStackTrace();}
            
        }

    }


    public static void main(String[] args){
        ex2();
    }

}
