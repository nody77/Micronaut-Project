package example.micronaut.exception;

public class CustomerNotUpdatedException extends  RuntimeException {
    public CustomerNotUpdatedException(String message) {

        super(message);
    }
}
