package ex2;
import java.lang.Thread;
import java.net.ContentHandler;
import java.util.Scanner;

class myThreadRunnerGreedy extends Thread {
    private counter contador;
    private int i;

    public myThreadRunnerGreedy(counter contador, int i){
        this.contador=contador;
        this.i = i;
    }

    @Override
    public void run(){
        for(int j=0;j<i;j++) this.contador.contador+=1;
    }
}

class myThreadRunner extends Thread {
    private counter contador;
    private int i;

    public myThreadRunner(counter contador,int i){
        this.contador=contador;
        this.i = i;
    }

    @Override
    public void run(){
        for(int j=0; j<this.i; j++){
            contador.increment();
        }
    }
}


class counter{
    public int contador;

    public void increment(){
        this.contador+=1;
    }

    public String toString(){
        return "Valor no contador: "+this.contador;
    }
}


class Main{
    public static void main(String[] args) throws InterruptedException{
        Scanner scaner = new Scanner(System.in);
        System.out.print("N: ");
        int n= scaner.nextInt();
        System.out.print("I: ");
        int i = scaner.nextInt();
        scaner.close();
        

        Thread[] threadsGreedy = new Thread[n];
        Thread[] threadsNGreedy = new Thread[n];
        counter contadorGreedy = new counter();
        counter contador = new counter();
        //solucao greedy
        for(int j=0; j<n;j++){
            threadsGreedy[j] = new myThreadRunnerGreedy(contadorGreedy, i);
        }
        for(int j=0; j<n;j++){
            threadsGreedy[j].start();
        }
        for(int j=0; j<n;j++){
            threadsGreedy[j].join();
        }

        //solucao não greedy
        for(int j=0; j<n;j++){
            threadsNGreedy[j] = new myThreadRunner(contador, i);
        }
        for(int j=0; j<n;j++){
            threadsNGreedy[j].start();
        }
        for(int j=0; j<n;j++){
            threadsNGreedy[j].join();
        }

        //3
        System.out.println("Método greedy: "+contadorGreedy.toString());
        System.out.println("Método normal: "+contador.toString());
        //parece que o contador normal acabar por ter resultados não expectaveis
        //para n=1000 e i=1000 temos que 
        //Método greedy: Valor no contador: 997524
        //Método normal: Valor no contador: 988113
    }
}