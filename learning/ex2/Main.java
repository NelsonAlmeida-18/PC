import java.util.Scanner;

class Counter{
    private int i;

    public void increment(){
        this.i+=1;
    }

    public String toString(){
        return "Valor do i: "+this.i;
    }
}


class myThread extends Thread {
    private int I;
    private Counter counter;

    public myThread(Counter counter, int i){
        this.counter = counter;
        this.I = i;
    }

    public void run() {
        for (int j = 0; j < I; j++) {
            counter.increment();
            try{
                Thread.sleep(500); //sleep na thread para podermos visualisar o output
            }
            catch(Exception e){
                System.out.println("Estourou");
            }
            
        }
    }
    public void setI(int x){
        this.I=x;
    }

    public String imprimeCounter(){
        return this.counter.toString();
    }

}

public class Main{
    public static void main(String args[]){
        Scanner scan = new Scanner(System.in);
        System.out.print("N: ");
        int N = scan.nextInt();
        scan.close();

        Counter counter = new Counter();
        try{
            for (int i=0; i<N; i++){
                myThread t = new myThread(counter, i);
                t.start();
            }
            System.out.println(counter.toString());
        }
        catch(Exception e){
            System.out.println("Erro");
        }

    }
}
