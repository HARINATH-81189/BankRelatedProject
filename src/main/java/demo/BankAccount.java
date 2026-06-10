package demo;

public class BankAccount {

    private final int accountNumber;
    private final String accountHolder;
    private final double balance;

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