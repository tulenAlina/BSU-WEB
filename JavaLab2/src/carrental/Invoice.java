package carrental;

public class Invoice {
    private Order order;
    private double amount;

    public Invoice(Order order, double amount) {
        this.order = order;
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}
