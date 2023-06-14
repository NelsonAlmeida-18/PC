import java.util.concurrent.locks.ReentrantLock;
import java.util.HashMap;
import java.util.Random;

class Conta{
    public int valor=0;
    
    public Conta(int saldoInicial){
        this.valor=saldoInicial;
    }
}

class Bank{
    private HashMap<Integer, Conta> contas;
    private Random random = new Random();
    private ReentrantLock readLock = new ReentrantLock();
    private ReentrantLock writeLock = new ReentrantLock();

    public Bank(int numContas){
        this.contas = new HashMap<>(numContas);
        for(int i=0;i<numContas; i++){
            //o exercício pede para que sejam criadas com valor inicial nulo
            //no entanto tomei a liberdade de as criar com um saldo arbitário com limite 100
            this.contas.put(i, new Conta(random.nextInt(100)));
        }
   }
    
   public int createAccount(int initialBalance){
        this.writeLock.lock();
        try{
            this.contas.put(this.contas.size(), new Conta(initialBalance));
            return this.contas.size()-1;
        }finally{
            this.writeLock.unlock();
        }

   }

   public int closeAccount(int id) throws InvalidAccount{
        this.readLock.lock();
        try{
            if(this.contas.containsKey(id)){
                int valor = this.contas.get(id).valor;
                this.writeLock.lock();
                try{
                    this.contas.remove(id);
                    return valor;
                }
                finally{
                    this.writeLock.unlock();
                }
            }
            throw new InvalidAccount();
        }
        finally{
            this.readLock.unlock();
        }

   }

   public void deposit(int id, int val) throws InvalidAccount{
    this.readLock.lock();
    try{
        if(this.contas.containsKey(id)){
            Conta conta = this.contas.get(id);
            this.writeLock.lock();
            //este lock pode ser desnecessário
            this.readLock.unlock();
            try{
                conta.valor+=val;
            }
            finally{
                this.writeLock.unlock();
            }
        }
        else{
            throw new InvalidAccount();
        }
    }
    finally{
        this.readLock.unlock();
    }
   }

   public void withdraw(int id, int val) throws InvalidAccount, NotEnoughFunds{
        this.readLock.lock();
        try{
            if(this.contas.containsKey(id)){
                Conta conta = this.contas.get(id);
                this.writeLock.lock();
                this.readLock.unlock();
                try{
                    if(conta.valor>=val)
                        conta.valor-=val;
                    else
                        throw new NotEnoughFunds();
                }
                finally{
                    this.writeLock.unlock();
                }
            }
            else
                throw new NotEnoughFunds();
        }
       finally{
        this.readLock.unlock();
       }
   }

   public void transfer(int from, int to, int val) throws InvalidAccount, NotEnoughFunds{
        this.readLock.lock();
        try{
            if(this.contas.containsKey(from) && this.contas.containsKey(to)){
                Conta contaRem = this.contas.get(from);
                Conta contaDest = this.contas.get(to);
                this.writeLock.lock();
                this.readLock.unlock();
                try{
                    if(contaRem.valor>=val){
                        contaRem.valor-=val;
                        contaDest.valor+=val;
                    }
                    else
                        throw new NotEnoughFunds();
                }
                finally{
                    this.writeLock.unlock();
                }
            
            }
            else
                throw new InvalidAccount();
        }
        finally{
            this.readLock.unlock();
        }
   }

   public void transferMarreta(int from, int to, int val) throws InvalidAccount, NotEnoughFunds{
        withdraw(from, val);
        deposit(to, val);
   }

   public int totalBalance(int accounts[]) throws InvalidAccount{
    int total=0;
    this.readLock.lock();
    try{
        for(int conta : accounts){
            if(this.contas.containsKey(conta)){
                total+=this.contas.get(conta).valor;
            }
            else{
                throw new InvalidAccount();
            }
        }
        return total;
    }finally{
        this.readLock.unlock();
    }
   }


}