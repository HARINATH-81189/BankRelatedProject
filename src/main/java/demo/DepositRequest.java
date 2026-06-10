package demo;

public class DepositRequest {

    private final double amount;

    public DepositRequest(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}