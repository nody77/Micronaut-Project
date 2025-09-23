package example.micronaut.controller;

import example.micronaut.service.JMSMockQueueService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/messages")
public class JMSMessageController {

    private final JMSMockQueueService jmsMockQueueService;

    public JMSMessageController(JMSMockQueueService jmsMockQueueService) {
        this.jmsMockQueueService = jmsMockQueueService;
    }

    @Post("/send")
    public HttpResponse<String> sendMessage(@Body String message) {
        jmsMockQueueService.sendMessage(message);
        return HttpResponse.ok("Message sent to JMS queue");
    }

    @Get
    public HttpResponse<String> checkMessageCount() {
        String httpBody = "Message sent to JMS queue and the number of messages received equals = " + jmsMockQueueService.receivedMessagesNumber();
        return HttpResponse.ok(httpBody);
    }

}
