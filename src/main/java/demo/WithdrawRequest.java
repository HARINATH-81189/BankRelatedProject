package demo;

public class WithdrawRequest {

    private final double amount;

    public WithdrawRequest(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}