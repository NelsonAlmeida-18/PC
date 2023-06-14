package guiao1;
import java.lang.Thread;
import java.util.Scanner;

class myThread extends Thread{
    public void run(){
        for(int i=0;i<10;i++){
            System.out.println(i);
            try{
                Thread.sleep(1000);
            }
            catch(Exception e){}
        }
    }
}

class Main{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.print("Number of threads: ");
        int N =sc.nextInt();
        myThread threads[] = new myThread[N];

        for(int i=0; i<N; i++){
            myThread thread = new myThread();
            threads[i]=thread;
        }

        for(int i=0;i<N;i++){
            System.out.println("Thread "+i+ " started");
            threads[i].start();
        }

        for(int i=0;i<N;i++){
            try{
                threads[i].join();
            }
            catch(Exception e){}
        }

    }
}