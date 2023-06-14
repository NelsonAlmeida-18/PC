import java.util.Random;

class BoundedBuffer_5_2{

    private int N;
    private int numElemsBuffer=0;
    private int[] buffer;
    //private Condition notEmpty;
    //private Condition notFull;
    //private Lock lock = new ReentrantLock();

    public BoundedBuffer_5_2(int N){
        this.N=N;
        this.buffer= new int[N];
        //this.notEmpty = lock.newCondition();
        //this.notFull = lock.newCondition();
    }

    public synchronized int get() throws InterruptedException{
        
        while(numElemsBuffer%N==0){
            wait();
        }

        int valor = buffer[numElemsBuffer];
        numElemsBuffer--;

        notifyAll();

        return valor;
    }

    public synchronized void put(int x) throws InterruptedException{

        while(numElemsBuffer==N){
            wait();
        }

        this.buffer[this.numElemsBuffer]=x;
        this.numElemsBuffer = (this.numElemsBuffer+1)%N;
        notifyAll();
    }

}

class Barreira_5_2{
    private int numThreads;
    private int limitThreads;
    
    public Barreira_5_2(int N){
        this.numThreads=0;
        this.limitThreads=N;
    }

    public synchronized void await() throws InterruptedException{
        this.numThreads++;
        while(this.numThreads<this.limitThreads) wait();
        notifyAll();
    }

}


public class guiao5_2 {
    

    public static void ex1(){

        int N=10;
        Random rd = new Random();
        BoundedBuffer_5_2 bb = new BoundedBuffer_5_2(N);
        for(int i=0;i<N;i++){
            try{
                new Thread(() -> {
                    int op=rd.nextInt(2);
                    try{
                        if(op==1){
                            bb.put(rd.nextInt(10));
                            System.out.println("Colocou");
                        }
                        else{
                            bb.get();
                            System.out.println("Tirou");
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                }).start();
            }catch(Exception e){e.printStackTrace();}
        }
    }

    public static void ex2(){
        int numThreads = 10;

        Barreira_5_2 b = new Barreira_5_2(numThreads);

        for(int i=0; i<numThreads; i++){
            new Thread(() -> {
                try{
                    System.out.println("Entrou");
                    b.await();
                    System.out.println("Saiu");
                }catch(Exception e){e.printStackTrace();} 
            }).start();
        }
    }

    public static void main(String [] args){
        ex2();
    }
}
