package example.micronaut.service;

import example.micronaut.messaging.producer.JMSMockQueueProducer;
import example.micronaut.messaging.listener.JMSMockQueueListener;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;


@Singleton
public class JMSMockQueueService {

    private final JMSMockQueueProducer jmsMockQueueProducer;
    private final JMSMockQueueListener jmsMockQueueListener;

    public JMSMockQueueService(JMSMockQueueProducer jmsMockQueueProducer, JMSMockQueueListener jmsMockQueueListener) {
        this.jmsMockQueueProducer = jmsMockQueueProducer;
        this.jmsMockQueueListener = jmsMockQueueListener;
    }

    public void sendMessage(@NotNull String message){
        jmsMockQueueProducer.send(message);
    }

    public int receivedMessagesNumber(){
        return jmsMockQueueListener.getMessageCount();
    }

}
