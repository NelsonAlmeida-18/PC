import java.util.Random;
import java.util.Scanner;

class guiao2_1Thread extends Thread{
    private int numOfIncrements=0;
    private int threadNum;
    private counter_2 contador;

    public guiao2_1Thread(int numOfIncrements, int threadNum, counter_2 contador){
        this.numOfIncrements=numOfIncrements;
        this.threadNum=threadNum;
        this.contador=contador;
    }

    public void run(){
        for(int i=0; i<this.numOfIncrements;i++){
            this.contador.increment();
            System.out.println("Thread "+this.threadNum+" incremented the counter_2");
        }
    }
}


class counter_2{
    public int value=0;

    public synchronized void increment(){
        this.value++;
    }
}

class Bank{
    private Integer[] contas;
    private Random random = new Random();

    public Bank(int numContas){
        this.contas = new Integer[numContas];
        for(int i=0;i<numContas; i++){
            //o exercício pede para que sejam criadas com valor inicial nulo
            //no entanto tomei a liberdade de as criar com um saldo arbitário com limite 100
            this.contas[i]= random.nextInt(100);
        }
   }
    
    public void deposit(int id, int val) throws InvalidAccount{
        if (id<this.contas.length)
          contas[id]+=val;
        else{
            throw new InvalidAccount();
        }
    }

    public void withdraw(int id, int val) throws InvalidAccount, NotEnoughFunds{
        if (id<this.contas.length){
            if (this.contas[id]>=val)
                this.contas[id]-=val;
    
            else 
                throw new NotEnoughFunds();
        }
        else{
            throw new InvalidAccount();
        }
    }

    public int totalBalance(int accounts[]) throws InvalidAccount{
        int total=0;
        for(int i=0;i<this.contas.length;i++) total+=this.contas[accounts[i]];
        return total;
    }

    public synchronized void transfereSync(int from, int to, int ammount) throws NotEnoughFunds, InvalidAccount{
        
        if(from>=0 && from <this.contas.length && to>=0 && to<this.contas.length)
            if(this.contas[from]>=ammount){
                withdraw(from, ammount); deposit(to, ammount);
            }
            else
                throw new NotEnoughFunds();
        else
            throw new InvalidAccount();
    }

    public void transfer(int from, int to, int ammount) throws NotEnoughFunds, InvalidAccount{
        
        if(from>=0 && from <this.contas.length && to>=0 && to<this.contas.length)
            synchronized(this.contas){
                if(this.contas[from]>=ammount){
                    withdraw(from, ammount); deposit(to, ammount);
                }
                else
                    throw new NotEnoughFunds();
            }
        else
            throw new InvalidAccount();

    }

}

public class guiao2 {


    //com o counter_2 como synchronized podemos verificar a serialização das threads
    public static void ex1(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Numero de Threads: ");
        int numOfThreads = sc.nextInt();
        System.out.print("\nLimite: ");
        int numToPrint = sc.nextInt();
        
        guiao2_1Thread[] threads = new guiao2_1Thread[numOfThreads];
        counter_2 contador = new counter_2();

        for(int i=0;i<numOfThreads;i++){
            threads[i] = new guiao2_1Thread(numToPrint, i, contador);
        }
        for(int i=0;i<numOfThreads;i++){
            threads[i].start();
        }
        for(int i=0;i<numOfThreads;i++){
            try{
                threads[i].join();
            }
            catch(Exception e){e.printStackTrace();}
        }
        System.out.println("Terminou");
        sc.close();
    }

    public static void main(String [] args){
        ex1();
    }
}

