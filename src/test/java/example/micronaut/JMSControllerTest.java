package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.micronaut.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest
public class JMSControllerTest {

    private BlockingHttpClient blockingClient;

    @Inject
    @Client("/")
    HttpClient client;

    @BeforeEach
    void setup() {
        blockingClient = client.toBlocking();
    }


    @Test
    void testJMSMessageSending(){
        HttpRequest<String> request = HttpRequest.POST("/messages/send", "JMS Test Message");
        HttpResponse<String> response = blockingClient.exchange(request , String.class);

        String responseBody = response.getBody().orElse("");

        assertEquals(OK, response.getStatus());
        assertEquals("Message sent to JMS queue", responseBody);
    }


    @Test
    void testJMSMessageReceived(){
        testJMSMessageSending();

        HttpRequest<String> request = HttpRequest.GET("/messages");
        HttpResponse<String> response = blockingClient.exchange(request , String.class);

        assertEquals(OK, response.getStatus());
        assertEquals("Message sent to JMS queue and the number of messages received equals = 1", response.body());

    }




}
