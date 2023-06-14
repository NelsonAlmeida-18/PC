import java.util.Scanner;
class BoundedBuffer_5{

    private int numElems;
    private int N;
    private int[] buffer;

    public BoundedBuffer_5(int N){
        this.N=N;
        this.numElems=0;
        this.buffer=new int[N];
    }

    public synchronized int get() throws InterruptedException{

        if(numElems==0) wait();

        int valor = buffer[0];
        numElems--;
        notifyAll();
        return valor;
    }

    public synchronized void put(int x) throws InterruptedException{

        if(numElems==N) wait();

        buffer[numElems]=x;
        numElems = numElems+1;
        notifyAll();
    }

}

class Barreira_5{

    private int N;
    private int numThreadsBlockeadas;

    public Barreira_5(int N){
        this.N=N;
        this.numThreadsBlockeadas=0;
    }   

    public synchronized void await() throws InterruptedException{
        this.numThreadsBlockeadas++;
        if(numThreadsBlockeadas!=N) wait();

        notifyAll();
        numThreadsBlockeadas=0;
    }
}


public class guiao5 {

    public static void ex1(){

        Scanner sc = new Scanner(System.in);
        System.out.print("Size of Bounded Buffer: ");
        int size = sc.nextInt();
        //Random random = new Random();

        BoundedBuffer_5 bb = new BoundedBuffer_5(size);

        Thread produtor = new Thread(() -> {
            try {
                int i = 0;
                while (true) {
                    System.out.println("Guardei " + i);
                    bb.put(i);
                    i++;
                    Thread.sleep(200);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Thread consumidor = new Thread(() -> {
            try {
                while (true) {
                    System.out.println("Consumi " + bb.get());
                    Thread.sleep(2000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        produtor.start();
        consumidor.start();

        sc.close();
    }

    public static void ex3(){

        Barreira_5 bb = new Barreira_5(5);

        try{
            for(int i=0;i<5;i++){
                new Thread(()->{
                    try{
                        System.out.println("Thread iniciada");
                        bb.await();
                        System.out.println("Thread terminada");
                    }
                    catch(Exception e){e.printStackTrace();}
                }).start();
                Thread.sleep(200);
            }
        }
        catch(Exception e){e.printStackTrace();}


    }

    public static void main(String [] args) {
        ex3();
    }
}
