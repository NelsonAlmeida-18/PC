class Barreira{
  private int theadSync = 0;
  private int counter = 0;

  Semaphore mutualExclusion = new Semaphore(1);
  Semaphore sem = new Semaphore(0);

  public Barreira(int N){
    this.threadSync = N;
  }

  public void await() throws InterruptedException{
    this.mutualExclusion.aquire();
    this.counter+=1;
    this.mutualExclusion.release();
    
    if (this.counter==threadSync){
      this.sem.release(); 
    }

  }

}
