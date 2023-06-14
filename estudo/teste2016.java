import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.*;
import java.util.HashMap;

class teste{
    //tipo do processo, numero de sinalizacoes
    private HashMap<Integer,Integer> processos;
    private Lock l = new ReentrantLock();
    private Condition isSignaled;

    public void sinaliza(int tipo){
        this.l.lock();
        try{
            if(this.processos.containsKey(tipo))
                this.processos.put(tipo, this.processos.get(tipo)+1);
            else 
                this.processos.put(tipo, 1);

            this.isSignaled.signalAll();
        }finally{
            this.l.unlock();
        }
    }

    private void espera(int t1, int n1, int t2, int n2) throws InterruptedException{
        this.l.lock();
        try{
            while(this.processos.get(t1)<n1 && this.processos.get(t2)<n2) this.isSignaled.await();

            this.processos.put(t1,0);
            this.processos.put(t2,0);
        }finally{
            this.l.unlock();
        }
    }

}

public class teste2016 {
    
}
