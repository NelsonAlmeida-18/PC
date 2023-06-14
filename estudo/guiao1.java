import java.util.Scanner;

class guiao1_1Thread extends Thread{
    private int numToPrint=0;
    private int threadNum;

    public guiao1_1Thread(int numToPrint, int threadNum){
        this.numToPrint=numToPrint;
        this.threadNum=threadNum;
    }

    public void run(){
        for(int i=0; i<this.numToPrint;i++){
            System.out.println("Thread "+this.threadNum+" : "+ i);
        }
    }
}

class guiao1_2_1Thread extends Thread{
    private int numOfIncrements=0;
    private int threadNum;
    private counter contador;

    public guiao1_2_1Thread(int numOfIncrements, int threadNum, counter contador){
        this.numOfIncrements=numOfIncrements;
        this.threadNum=threadNum;
        this.contador=contador;
    }

    public void run(){
        for(int i=0; i<this.numOfIncrements;i++){
            System.out.println("Thread "+this.threadNum+" incremented the counter");
            this.contador.increment();
        }
    }
}

class guiao1_2_2Thread extends Thread{
    private int numOfIncrements=0;
    private int threadNum;
    private counter contador;

    public guiao1_2_2Thread(int numOfIncrements, int threadNum, counter contador){
        this.numOfIncrements=numOfIncrements;
        this.threadNum=threadNum;
        this.contador=contador;
    }

    public void run(){
        for(int i=0; i<this.numOfIncrements;i++){
            System.out.println("Thread "+this.threadNum+" incremented the counter");
            this.contador.value++;
        }
    }
}

class counter{
    public int value=0;

    public void increment(){
        this.value++;
    }
}

public class guiao1{

    public static void ex1(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Numero de Threads: ");
        int numOfThreads = sc.nextInt();
        System.out.print("\nLimite: ");
        int numToPrint = sc.nextInt();
        
        guiao1_1Thread[] threads = new guiao1_1Thread[numOfThreads];

        for(int i=0;i<numOfThreads;i++){
            threads[i] = new guiao1_1Thread(numToPrint, i);
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

    //exercicio 2.1
    public static void ex2_1(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Numero de Threads: ");
        int numOfThreads = sc.nextInt();
        System.out.print("\nNumero de incrementos: ");
        int numIncrements = sc.nextInt();
        
        guiao1_2_1Thread[] threads = new guiao1_2_1Thread[numOfThreads];
        counter contador = new counter();
        for(int i=0;i<numOfThreads;i++){
            threads[i] = new guiao1_2_1Thread(numIncrements, i, contador);
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
        System.out.println("Terminou com "+ contador.value+" no contador");
        sc.close();
    }

    //exercicio 2.2
    public static void ex2_2(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Numero de Threads: ");
        int numOfThreads = sc.nextInt();
        System.out.print("\nNumero de incrementos: ");
        int numIncrements = sc.nextInt();
        
        guiao1_2_2Thread[] threads = new guiao1_2_2Thread[numOfThreads];
        counter contador = new counter();
        for(int i=0;i<numOfThreads;i++){
            threads[i] = new guiao1_2_2Thread(numIncrements, i, contador);
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
        System.out.println("Terminou com "+ contador.value+" no contador");
        sc.close();
    }

    //Exercicio 3
    public static void ex3(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Numero de Testes: ");
        int numOfTests = sc.nextInt();
        System.out.print("Numero de Threads: ");
        int numOfThreads = sc.nextInt();
        System.out.print("\nNumero de incrementos: ");
        int numIncrements = sc.nextInt();
        
        for(int j=0;j<numOfTests;j++){
            guiao1_2_1Thread[] threads1 = new guiao1_2_1Thread[numOfThreads];
            guiao1_2_2Thread[] threads2 = new guiao1_2_2Thread[numOfThreads];

            counter contador1 = new counter();
            counter contador2 = new counter();

            for(int i=0;i<numOfThreads;i++){
                threads1[i] = new guiao1_2_1Thread(numIncrements, i, contador1);
                threads2[i] = new guiao1_2_2Thread(numIncrements, i, contador2);
            }
            for(int i=0;i<numOfThreads;i++){
                threads1[i].start();
                threads2[i].start();
            }
            for(int i=0;i<numOfThreads;i++){
                try{
                    threads1[i].join();
                }
                catch(Exception e){e.printStackTrace();}
            }
            for(int i=0;i<numOfThreads;i++){
                try{
                    threads2[i].join();
                }
                catch(Exception e){e.printStackTrace();}
            }
            System.out.println("Teste "+ j+" terminou com "+ contador1.value+" no contador1 e "+ contador2.value+" no contador2");
        }
        sc.close();

    }

    public static void main(String [] args){
        ex3();
    }
}