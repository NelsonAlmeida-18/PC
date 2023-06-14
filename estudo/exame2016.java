import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.*;


class Eleicoes{

    private HashMap<String, Integer> urna;
    private Condition justVoted;
    private Lock l;

    public Eleicoes(){
        this.urna = new HashMap<>(3);
        this.l = new ReentrantLock();
        this.justVoted = this.l.newCondition();
    }

    public synchronized void vota(String candidato){
        if(this.urna.containsKey(candidato)){
            this.urna.put(candidato, this.urna.get(candidato)+1);
        }
        else{
            this.urna.put(candidato, 1);
        }
        this.justVoted.signalAll();
    }

    public void espera(String c1, String c2, String c3)throws InterruptedException{
        this.l.lock();
        try{
            while(!(this.urna.get(c1)<this.urna.get(c2) && this.urna.get(c2)<this.urna.get(c3) ))this.justVoted.await();
        }finally{
            this.l.unlock();
        }
    }

}
