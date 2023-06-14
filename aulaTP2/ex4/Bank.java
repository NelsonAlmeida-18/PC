import java.util.Scanner;
import java.util.Random;

class InvalidDepositValue extends Exception {
  public InvalidDepositValue() {
    System.out.println("Invalid Deposit Ammount");
  }
}

class InvalidAccount extends Exception {
  public InvalidAccount() {
    System.out.println("Invalid Account");
  }
}

class NotEnoughFunds extends Exception {
  public NotEnoughFunds() {
    System.out.println("Not enough funds");
  }
}

class Bank {
  private Account[] accs;
  private int numAccounts;
  private float totalBalance;

  Bank(int numAcs) {
    this.accs = new Account[numAcs];
    this.numAccounts = numAcs;
    for (int i = 0; i < numAcs; i++) {
      this.accs[i] = new Account();
    }
  }

  public void Deposit(int id, float val) throws InvalidDepositValue, InvalidAccount {
    if (id >= 0 && id < this.numAccounts) {
      accs[id].deposit(val);
      this.totalBalance += val;
    } else
      throw new InvalidAccount();
  }

  public void Withdraw(int id, float val) throws NotEnoughFunds, InvalidAccount {
    accs[id].withdraw(val);
    this.totalBalance -= val;
  }

  public void Transfer(int srcId, int destId, float val) throws InvalidAccount, NotEnoughFunds, InvalidDepositValue {
    // dar catch ao problema de a destAccount não existir e a primeira operação
    // já ter sido realizada
    accs[srcId].withdraw(val);
    accs[destId].deposit(val);
  }

  public float totalBalance() {
    return this.totalBalance;
  }

}

class Account {
  private float balance = 0;

  public synchronized void deposit(float val) throws InvalidDepositValue {
    if (val > 0)
      this.balance += val;
    else
      throw new InvalidDepositValue();
  }

  public synchronized void withdraw(float val) throws NotEnoughFunds {
    if (val <= balance) {
      this.balance -= val;
    } else
      throw new NotEnoughFunds();
  }

}

class Main {
  public static void main(String args[]) {

    Scanner scanner = new Scanner(System.in);
    System.out.print("Number of accounts: ");
    int numAccounts = scanner.nextInt();
    Random randomGenerator = new Random();

    scanner.close();
    // cria o objeto banco
    Bank b = new Bank(numAccounts);

    // gera threads com o numero de contas
    for (int j = 0; j < numAccounts; j++) {
      new Thread(() -> {
        // operações random a serem executadas em cada conta
        // 0=deposito, 1=levantamento, 2=transferência
        int operation = randomGenerator.nextInt(3);
        try {
          if (operation == 0) {
            b.Deposit(randomGenerator.nextInt(numAccounts), randomGenerator.nextInt(100) + randomGenerator.nextFloat());
          } else if (operation == 1) {
            b.Withdraw(randomGenerator.nextInt(numAccounts),
                randomGenerator.nextInt(100) + randomGenerator.nextFloat());
          } else if (operation == 2) {
            b.Transfer(randomGenerator.nextInt(numAccounts), randomGenerator.nextInt(numAccounts),
                randomGenerator.nextInt(100) + randomGenerator.nextFloat());
          }
        } catch (Exception e) {
          e.printStackTrace();
        }

      }).start();
    }

    System.out.println("Valor no banco: " + b.totalBalance());

  }
}
