package example.micronaut.controller;


import example.micronaut.messaging.listener.JMSMockQueueListener;
import example.micronaut.service.JMSMockQueueService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

@Controller("/messages")
public class JMSMessageController {

    private final JMSMockQueueService jmsMockQueueService;
    private final JMSMockQueueListener jmsMockQueueListener;

    public JMSMessageController(JMSMockQueueService jmsMockQueueService, JMSMockQueueListener jmsMockQueueListener) {
        this.jmsMockQueueService = jmsMockQueueService;
        this.jmsMockQueueListener = jmsMockQueueListener;
    }

    @Post("/send")
    public HttpResponse<String> sendMessage(@Body String message) {
        jmsMockQueueService.sendMessage(message);
        return HttpResponse.ok("Message sent to JMS queue");
    }

    @Get
    public HttpResponse<String> checkMessageCount() {
        String httpBody = "Message sent to JMS queue and the number of messages received equals = " + jmsMockQueueListener.getMessageCount();
        return HttpResponse.ok(httpBody);

    }

}
