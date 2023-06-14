import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.*;
import java.util.Random;

class PonteManager{
    private Lock l;
    private int weightLimit;
    public int currentWeight;
    private Condition isComming;
    private Condition isGoing;

    public PonteManager(int weightLimit){
        this.weightLimit=weightLimit;
        this.currentWeight=0;
        this.l=new ReentrantLock();
        this.isComming=this.l.newCondition();
        this.isGoing = this.l.newCondition();
    }

    public void inicioDaTravessiaIda() throws InterruptedException{
        this.l.lock();
        try{
            while(this.currentWeight==this.weightLimit) this.isGoing.await();

            this.currentWeight++;
        }finally{
            this.l.unlock();
        }
    }

    public void inicioDaTravessiaVolta() throws InterruptedException{
        this.l.lock();
        try{
            while(this.currentWeight==this.weightLimit) this.isComming.await();

            this.currentWeight++;
        }finally{
            this.l.unlock();
        }
    }

    public void fimTravessia(){
        this.l.lock();
        try{
            this.currentWeight--;
            this.isComming.signalAll();
            this.isGoing.signalAll();
        }
        finally{
            this.l.unlock();
        }
    }


}

public class epocaespecial2016 {
    
    public static void main(String [] args){
        
        PonteManager pm = new PonteManager(2);
        int numVisitantes = 10;
        Random rd = new Random();

        try{
            for(int i=0;i<numVisitantes;i++){
                new Thread(() -> {
                    try{
                        System.out.println("Inicio da travessia");
                        pm.inicioDaTravessiaIda();
                        System.out.println(pm.currentWeight);
                        Thread.sleep(rd.nextInt(200));
                        System.out.println("Volta da Travessia");
                        pm.inicioDaTravessiaVolta();
                        System.out.println(pm.currentWeight);
                        Thread.sleep(rd.nextInt(100));
                        pm.fimTravessia();
                        System.out.println(pm.currentWeight);
                    }
                    catch(Exception e){e.printStackTrace();}
                }).start();
            }
        }
        catch(Exception e){e.printStackTrace();}

    }
}
