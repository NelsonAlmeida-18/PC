import java.util.Scanner;

class myThread extends Thread {
    private int I;
    private int i;

    public void run() {
        for (int j = 1; j < I; j++) {
            System.out.println("Thread "+i +": "+j);
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

    public void seti(int x){
        this.i=x;
    }

}

public class Main{
    public static void main(String args[]){
        Scanner scan = new Scanner(System.in);
        System.out.print("N: ");
        int N = scan.nextInt();
        scan.close();

        try{
            for (int i=0; i<N; i++){
                myThread t = new myThread();
                t.seti(i);
                t.setI(N);
                t.start(); //inicializa a execução por threads
                t.join();// espera que a thread termine antes de inicar a próxima
            }
        }
        catch(Exception e){
            System.out.println("Erro");
        }

    }
}