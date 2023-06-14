import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Bank{
    private HashMap<Integer, Float> contas = new HashMap<>();

    public void deposit(int id, float value){
        if (this.contas.containsKey(id)){
            float tempSaldo = this.contas.get(id);
            this.contas.put(id, tempSaldo+value);
        }
        else{
            this.contas.put(id, value);
        }
    }

    public void withdraw(int id, float value){
        if (this.contas.containsKey(id)){
            float tempSaldo = this.contas.get(id);
            this.contas.put(id, tempSaldo-value);
        }
    }

    public float totalBalance(int account){
        return this.contas.get(account);
    }

}

class myMultiThread extends Thread{
    private Lock lock = new ReentrantLock();
    private Bank banco;
    private int threadNum;

    public myMultiThread(Bank banco,int i){
        lock.lock();
        this.banco=banco;
        this.threadNum=i;
        lock.unlock();
    }

    public synchronized void deposit(int account, float value){
        lock.lock();
        this.banco.deposit(account, value);
        System.out.println("Acabou deposit thread "+threadNum);
        lock.unlock();
    }

    public synchronized void withdraw(int account, float value){
        lock.lock();
        this.banco.withdraw(account, value);
        System.out.println("Acabou withdraw thread "+threadNum);
        lock.unlock();
    }

    public synchronized float totalBalance(int account){
        lock.lock();
        System.out.println("Acabou total thread "+threadNum);
        lock.unlock();
        return this.banco.totalBalance(account);
        //lock.unlock();
    }

}

public class Main {
    public static void main(String args[]){
        Bank banco = new Bank();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Numero de contas a gerar: ");
        int numeroDeContasParaGerar = scanner.nextInt();
        scanner.close();

        for (int i=0; i<numeroDeContasParaGerar; i++){
            myMultiThread thread1 = new myMultiThread(banco,i);
            float saldo =  (float)(0.5+Math.random()*(100-0.5));
            thread1.deposit(i,saldo);
            System.out.println(thread1.totalBalance(i));
            thread1.start();
        }
    }
}
