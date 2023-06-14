import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

class InvalidAccount extends Exception {
  public void InvalidAccount() {
    System.out.println("Invalid Account");
  }
}

class InvalidAmmount extends Exception {
  public void InvalidAmmount() {
    System.out.println("Invalid Ammount");
  }
}

class NotEnoughFunds extends Exception {
  public void NotEnoughFunds() {
    System.out.println("Not Enough Funds");
  }
}

class Account {
  private float balance;
  private int IBAN;
  public Lock l = new ReentrantLock();

  public Account(float initialBalance, int IBAN) {
    this.balance = initialBalance;
    this.IBAN = IBAN;
  }

  public void deposit(float x) {
    this.balance += x;
  }

  public void withdraw(float x) {
    this.balance -= x;
  }

  public float getFunds() {
    return this.balance;
  }

  public int getIban() {
    return this.IBAN;
  }

}

class Bank {
  private Map<Integer, Account> contas = new HashMap<Integer, Account>();
  private float totalFunds = 0;
  private int totalAccounts = 0;
  private Lock l = new ReentrantLock();

  public int createAccount(float initialBalance) {
    l.lock();
    try {
      this.contas.put(totalAccounts, new Account(initialBalance, totalAccounts));
      totalAccounts += 1;
      return totalAccounts - 1;
    } finally {
      l.unlock();
    }
  }

  public float closeAccount(int IBAN) throws InvalidAccount {
    Account targetAccount;
    l.lock();
    try {
      if (!this.contas.containsKey(IBAN)) {
        throw new InvalidAccount();
      }
      targetAccount = this.contas.get(IBAN);
      this.contas.remove(IBAN);
    } finally {
      l.unlock();
    }

    try {
      targetAccount.l.lock();
      return targetAccount.getFunds();
    } finally {
      targetAccount.l.unlock();
    }
  }

  public void deposit(int id, float val) throws InvalidAmmount, InvalidAccount {
    if (val <= 0) {
      throw new InvalidAmmount();
    }

    Account targetAccount = this.contas.get(id);
    if (targetAccount == null) {
      throw new InvalidAccount();
    }
    targetAccount.l.lock();
    targetAccount.deposit(val);
    targetAccount.l.unlock();

  }

  public void withdraw(int id, float val) throws NotEnoughFunds, InvalidAccount {
    Account targetAccount = this.contas.get(id);
    if (targetAccount == null) {
      throw new InvalidAccount();
    }

    if (targetAccount.getFunds() < val) {
      throw new NotEnoughFunds();
    }

    targetAccount.l.lock();
    targetAccount.withdraw(val);
    targetAccount.l.unlock();

  }

  public void transfer(int srcId, int targetId, float val) throws InvalidAmmount, NotEnoughFunds {

    // Não teria de levar locks para previnir que alguma conta seja eliminada
    // enquanto esta
    // transação está a ser executada?

    withdraw(srcId, val);
    deposit(targetId, val);

  }

  public float totalBalance() {
    float totalFunds = 0;
    l.lock();
    for (int i = 0; i < IBAN; i++) {
      if (this.contas.containsKey(i)) {
        this.contas.get(i).l.lock();
      }
    }

    for (int i = 0; i < IBAN; i++) {
      if (this.contas.containsKey(i)) {
        totalFunds += this.contas.get(i).getFunds();
      }
    }

    for (int i = 0; i < IBAN; i++) {
      if (this.contas.containsKey(i)) {
        this.contas.get(i).l.unlock();
      }
    }
    l.unlock();

    return totalFunds;
  }

}

class main {
  public static void main(String args[]) {

    Bank banco = new Bank();

    Scanner scanner = new Scanner(System.in);
    System.out.print("Create Account(0)\nClose Account(1)\nDeposit(2)\nWithdraw(3)\nTransfer(4)\nTotal Balance(5)");
    int option = scanner.nextInt();
    while (option) {
      if (option == 0) {
        System.out.print("Saldo inicial da conta:");
        float initialBalance = scanner.nextFloat();
        banco.createAccount(initialBalance);
      }
      if (option == 1) {
        System.out.print("Conta que pretende fechar:");
        int iban = scanner.nextInt();
        banco.closeAccount(iban);
      }
      if (option == 2) {

      }
      if (option == 3) {

      }
      if (option == 4) {

      }
      if (option == 5) {

      }
    }

    scanner.close();
  }
}
