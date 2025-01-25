package carrental;

public class Order {
    private Request request;

    public Order(Request request) {
        this.request = request;
    }

    public Request getRequest() {
        return request;
    }
}
