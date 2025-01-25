package exceptions;

public class CarRentalException extends Exception {
    private static final long serialVersionUID = 1L; // Уникальный ID для сериализации

    public CarRentalException(String message) {
        super(message);
    }

    public CarRentalException(String message, Throwable cause) {
        super(message, cause);
    }
}
