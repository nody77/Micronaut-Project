package example.micronaut.exception;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Produces
@Singleton
public class JMSMessageIsNotReceivedHandler implements ExceptionHandler<JMSMessageIsNotReceived, HttpResponse<String>>{

    @Override
    public HttpResponse<String> handle(HttpRequest request, JMSMessageIsNotReceived exception) {
        return HttpResponse.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
}
