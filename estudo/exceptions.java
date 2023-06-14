class InvalidAccount extends Exception{
    public InvalidAccount(){
        System.out.println("Invalid Account");
    }
}

class NotEnoughFunds extends Exception{
    public NotEnoughFunds(){
        System.out.println("Not Enough Funds");
    }
}
