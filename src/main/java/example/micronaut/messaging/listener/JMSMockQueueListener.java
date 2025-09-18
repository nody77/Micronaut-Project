package example.micronaut.messaging.listener;

import example.micronaut.exception.JMSMessageIsNotReceived;
import io.micronaut.jms.annotations.JMSListener;
import io.micronaut.jms.annotations.Queue;
import io.micronaut.messaging.annotation.MessageBody;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.micronaut.jms.activemq.classic.configuration.ActiveMqClassicConfiguration.CONNECTION_FACTORY_BEAN_NAME;

@Singleton
@JMSListener(CONNECTION_FACTORY_BEAN_NAME)
public class JMSMockQueueListener {

    private final List<String> messages = Collections.synchronizedList(new ArrayList<>());

    @Queue(value = "queue_text") // (2)
    public void receive(@MessageBody String body) { // (3)
        try {
            messages.add(body);
        }
        catch (Exception e){
            throw new JMSMessageIsNotReceived("The Listener did not receive any messages.");
        }
    }


    public int getMessageCount(){
        return messages.size();
    }

}
