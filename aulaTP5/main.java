import BoundedBuffer.*;
import Consumidor.*;
import Produtor.*;

class Main {
  public static void main(String args[]) {

    Scanner scanner = new Scanner(System.in);
    System.out.print("Number of accounts: ");
    int numAccounts = scanner.nextInt();

    scanner.close();
  }
}
