import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Counter {
    private int i=0;

    public void increment(){
        this.i+=1;
    }

    public int getI(){
        return i;
    }

}

class myMultiThread extends Thread{
    private Counter counter;
    private int i;
    private int threadNum;
    private Lock lock = new ReentrantLock();

    public myMultiThread(Counter c, int i, int threadNum){
        this.counter = c;
        this.i=i;
        this.threadNum=threadNum;
    }
    
    @Override
    public void run(){
        lock.lock();
        for (int j=0; j<i; j++){
            counter.increment();
        }
        System.out.println(""+threadNum);
        lock.unlock();
    }
}

public class Main {
    
    public static void main(String args[]){
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.close();

        Counter contador = new Counter();
        for (int i=0; i<n; i++){
            myMultiThread t = new myMultiThread(contador, n,i);
            t.start();
            try{
                Thread.sleep(1000); //para fins de debug
            }
            catch(Exception e){

            }
        }
        System.out.println(contador.getI());
        

    }

}
