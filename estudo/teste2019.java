import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.*;
import java.time.LocalTime;
import java.util.Random;

class Partida{
    private int value;
    private LocalTime initTime;
    private Lock l;
    private Condition notInGame;
    private int numTentativas;


    public Partida(){
        Random rd = new Random();
        this.value = rd.nextInt(1,101);
        this.l = new ReentrantLock();
        this.initTime = LocalTime.now();
        this.notInGame = this.l.newCondition();
        this.numTentativas=0;
    }

    public String adivinha(int n) throws InterruptedException{
        this.l.lock();
        try{
            LocalTime rn = LocalTime.now();
            this.numTentativas++;
            String result="";
            if(this.numTentativas>=100){
                result = "TENTATIVAS";
            }
            else if(rn.isBefore(initTime.plusMinutes(1))){
                result = "TEMPO";
            }
            else if (n==this.value){
                result = "GANHOU";
            }
            else if(n>this.value){
                result =  "MENOR";
            }
            else if(n<this.value){
                result = "MAIOR";
            }
            else{
                result= "PERDEU";
            }
            return result;
        }
        finally{
            this.l.unlock();
        }
    }

}

class Jogo{
    private int numMaxThreads;
    private int numThreadsBlocked;
    private Lock l;
    private Condition notFullLoby;
    private Partida partida;


    public Jogo(int lobbySize){
        this.numMaxThreads = lobbySize;
        this.numThreadsBlocked = 0;
        this.l = new ReentrantLock();
        this.notFullLoby = this.l.newCondition();
        this.partida = new Partida();
    }

    public Partida participa() throws InterruptedException{
        this.numThreadsBlocked++;
        while(numThreadsBlocked<this.numMaxThreads) this.notFullLoby.await();

        this.notFullLoby.signalAll();
        this.numThreadsBlocked=0;
        return this.partida;
    }
}
