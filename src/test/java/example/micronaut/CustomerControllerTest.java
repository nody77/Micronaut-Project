package example.micronaut;

import example.micronaut.entity.Customer;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.render.BearerAccessRefreshToken;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.micronaut.http.HttpHeaders.LOCATION;
import static io.micronaut.http.HttpStatus.*;
import static io.micronaut.http.HttpStatus.OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest
class CustomerControllerTest {

    private BlockingHttpClient blockingClient;

    @Inject
    @Client("/")
    HttpClient client;

    @BeforeEach
    void setup() {
        blockingClient = client.toBlocking();
    }

    @Test
    void testFindNonExistingCustomerReturns404() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(HttpRequest.GET("/customer/99"))
        );

        assertNotNull(thrown.getResponse());
        assertEquals(NOT_FOUND, thrown.getStatus());
    }


    @Test
    void testCustomerCrudOperations() {

        List<Long> customerIds = new ArrayList<>();

        HttpRequest<?> request = HttpRequest.POST("/customer", new Customer("Ali", "0"));
        HttpResponse<?> response = blockingClient.exchange(request);
        customerIds.add(entityId(response));

        assertEquals(CREATED, response.getStatus());

        request = HttpRequest.POST("/customer", new Customer("Ahmed", "1"));
        response = blockingClient.exchange(request);

        assertEquals(CREATED, response.getStatus());

        Long id = entityId(response);
        customerIds.add(id);
        request = HttpRequest.GET("/customer/" + id);

        Customer customer = blockingClient.retrieve(request, Customer.class);

        assertEquals("Ahmed", customer.getName());

        request = HttpRequest.PUT("/customer/" + id, new Customer("Sarah" , "1"));
        response = blockingClient.exchange(request);

        assertEquals(NO_CONTENT, response.getStatus());

        request = HttpRequest.GET("/customer/" + id);
        customer = blockingClient.retrieve(request, Customer.class);
        assertEquals("Sarah", customer.getName());

        Long invalidID = customerIds.getLast() + 1;
        HttpRequest<?> inalidPUTrequest = HttpRequest.PUT("/customer/" + invalidID, new Customer("Farah" , "1"));
        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () -> {
            blockingClient.exchange(inalidPUTrequest);
        });

        assertEquals(NOT_FOUND, exception.getStatus());

        // cleanup:
        for (Long customerId : customerIds) {
            request = HttpRequest.DELETE("/customer/" + customerId);
            response = blockingClient.exchange(request);
            assertEquals(NO_CONTENT, response.getStatus());
        }
    }

    private Long entityId(HttpResponse response) {
        String path = "/customer/";
        String value = response.header(LOCATION);
        if (value == null) {
            return null;
        }

        int index = value.indexOf(path);
        if (index != -1) {
            return Long.valueOf(value.substring(index + path.length()));
        }

        return null;
    }

    @Test
    void testJMSMessageSending(){

        UsernamePasswordCredentials creds = new UsernamePasswordCredentials("user", "password");
        HttpRequest<?> loginRequest = HttpRequest.POST("/login", creds);
        HttpResponse<BearerAccessRefreshToken> rsp = client.toBlocking().exchange(loginRequest, BearerAccessRefreshToken.class);
        assertEquals(OK, rsp.getStatus());
        BearerAccessRefreshToken bearerAccessRefreshToken = rsp.body();
        String accessToken = bearerAccessRefreshToken.getAccessToken();


        HttpRequest<String> request = HttpRequest.POST("/messages/send", "JMS Test Message").bearerAuth(accessToken);
        HttpResponse<String> response = blockingClient.exchange(request , String.class);

        String responseBody = response.getBody().orElse("");

        assertEquals(OK, response.getStatus());
        assertEquals("Message sent to JMS queue", responseBody);
    }

    @Test
    void accessingASecuredUrlWithoutAuthenticatingReturnsUnauthorized() {
        HttpClientResponseException e = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.GET("/messages"));
        });

        assertEquals(UNAUTHORIZED, e.getStatus());
    }

    @Test
    void testJMSMessageReceived(){

        testJMSMessageSending();

        UsernamePasswordCredentials creds = new UsernamePasswordCredentials("user", "password");
        HttpRequest<?> loginRequest = HttpRequest.POST("/login", creds);
        HttpResponse<BearerAccessRefreshToken> rsp = client.toBlocking().exchange(loginRequest, BearerAccessRefreshToken.class);
        assertEquals(OK, rsp.getStatus());
        BearerAccessRefreshToken bearerAccessRefreshToken = rsp.body();
        String accessToken = bearerAccessRefreshToken.getAccessToken();

        HttpRequest<Object> request = HttpRequest.GET("/messages").bearerAuth(accessToken);
        HttpResponse<String> response = blockingClient.exchange(request , String.class);

        assertEquals(OK, response.getStatus());
        assertEquals("Message sent to JMS queue and the number of messages received equals = 1", response.body());

    }

    @Test
    void testDuplicationOfPhoneNumber() {
        HttpRequest<Customer> request1 = HttpRequest.POST("/customer", new Customer("Ali", "0"));
        HttpResponse<Customer> response = blockingClient.exchange(request1, Customer.class);
        assertEquals(CREATED, response.getStatus());

        HttpClientResponseException ex = assertThrows(HttpClientResponseException.class, () -> {
            blockingClient.exchange(HttpRequest.POST("/customer", new Customer("Farah", "0")), String.class);
        });

        assertEquals(CONFLICT, ex.getStatus());
        assertEquals("This phone number is already in use", ex.getResponse().getBody(String.class).orElse(""));
    }


    @Test
    void testDeletingNonExistingCustomer(){
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () ->
                blockingClient.exchange(HttpRequest.DELETE("/customer/99"))
        );

        assertNotNull(thrown.getResponse());
        assertEquals(NOT_FOUND, thrown.getStatus());
    }
}
