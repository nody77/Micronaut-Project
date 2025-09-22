package example.micronaut.exception;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;

import jakarta.inject.Singleton;

@Produces
@Singleton
public class DuplicatePhoneNumberExceptionHandler implements ExceptionHandler<DuplicatePhoneNumberException, HttpResponse<String>>{
    @Override
    public HttpResponse<String> handle(HttpRequest request, DuplicatePhoneNumberException exception) {
        return HttpResponse.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }
}
