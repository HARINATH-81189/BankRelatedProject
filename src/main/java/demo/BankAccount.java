package demo;

public class BankAccount {

    private final int accountNumber;
    private final String accountHolder;
    private final double balance;
//this is the constructor for instance members initialization
    //we using accountNumber, accountHolder, and balance details
    //it is 3rd line comment for git
    public BankAccount(int accountNumber,
                       String accountHolder,
                       double balance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = balance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public double getBalance() {
        return balance;
    }
}