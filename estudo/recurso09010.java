import java.util.concurrent.locks.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

class Controlador{

    private int C;
    private int P;
    private int threadsRunning;
    private List<Integer> prioridade;
    private Lock l;
    private Condition isFull;

    public Controlador(int C, int P){
        this.C = C;
        this.P = P;
        this.prioridade = new ArrayList<>();
        this.l = new ReentrantLock();
        this.isFull = this.l.newCondition();
        this.threadsRunning=0;
    }


    public void executa(Runnable tarefa, int prioridade) throws InterruptedException{
        this.l.lock();
        try{
            if (prioridade<=this.P){
                while(this.threadsRunning>=this.C && Collections.max(this.prioridade)>prioridade ) this.isFull.await();

                this.threadsRunning++;
                this.prioridade.add(prioridade);
                tarefa.run();
                this.prioridade.remove(prioridade);
                this.threadsRunning--;
                this.isFull.signalAll();
            }

        }
        finally{
            this.l.unlock();
        }
    }

}

public class recurso09010 {
    
}
