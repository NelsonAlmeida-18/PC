import java.util.concurrent.locks.*;

class reparticaoPublica{

    private int numServicos;
    private int numPostos;
    private int numAtendidos;
    private Lock l;
    private Condition isAvaillableCondition;
    private Condition isEmpty;
    private Boolean[] postos;

    public reparticaoPublica(int P, int S){
        this.numServicos=S;
        this.numPostos=P;
        this.numAtendidos=0;
        this.l = new ReentrantLock();
        this.isAvaillableCondition = this.l.newCondition();
        this.isEmpty = this.l.newCondition();
        this.postos = new Boolean[P];
    }

    public int obtem_posto(int servico) throws InterruptedException{
        int numPosto=-1;
        this.l.lock();
        try{

            // deviamos verificar se o número de serviço é menor do que this.numServicos
            while(this.numAtendidos == this.numPostos) this.isAvaillableCondition.await();
            
            for(int i=0; i<this.numPostos;i++) 
                if (this.postos[i]==false){
                    this.postos[i]=true;
                    numPosto=i;
                    break;
                }
            this.numAtendidos++;
            this.isEmpty.signalAll();
            return numPosto;
        }
        finally{
            this.l.unlock();
        }
    }

    public void fim_atendimento(int posto) throws InterruptedException{
        this.l.lock();
        try{
            while(this.numAtendidos==0) this.isEmpty.await();
            this.numAtendidos--;
            this.postos[posto]=false;
            this.isAvaillableCondition.signalAll();
        }
        finally{
            this.l.unlock();
        }
    }
    
}

public class recurso0708 {
}
