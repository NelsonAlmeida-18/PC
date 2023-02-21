package ex1;
import java.lang.Runnable;
import java.lang.Thread;
import java.util.Scanner;

class myThreadRunner extends Thread {
    private  int i;
    private  int threadNum;
    
    public myThreadRunner(int j, int thread){
        this.i=j;
        this.threadNum = thread;
    }

    @Override
    public void run(){
        for (int k=0; k<this.i;k++){
            System.out.println("Thread "+this.threadNum+" contador: "+ k);
        }
    }
}


class Main{
    public static void main(String[] args) throws InterruptedException{
        Scanner scaner = new Scanner(System.in);
        System.out.println("N: ");
        int n= scaner.nextInt();
        System.out.println("I: ");
        int i = scaner.nextInt();
        scaner.close();
        
        Thread[] thread = new Thread[n];
        for(int j=0; j<n;j++){
            thread[j] = new myThreadRunner(i,j);
        }
        for(int j=0; j<n;j++){
            thread[j].start();
        }
        for(int j=0; j<n;j++){
            thread[j].join();
        }
        
    }
}