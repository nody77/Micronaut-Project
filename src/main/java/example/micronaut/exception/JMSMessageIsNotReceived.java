package example.micronaut.exception;

public class JMSMessageIsNotReceived extends RuntimeException {
    public JMSMessageIsNotReceived(String message) {
        super(message);
    }
}
