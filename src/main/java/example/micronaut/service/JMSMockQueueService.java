package example.micronaut.service;

import example.micronaut.messaging.listener.JMSMockQueueListener;
import example.micronaut.messaging.producer.JMSMockQueueProducer;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;

@Singleton
public class JMSMockQueueService {

    private final JMSMockQueueProducer jmsMockQueueProducer;

    public JMSMockQueueService(JMSMockQueueProducer jmsMockQueueProducer) {
        this.jmsMockQueueProducer = jmsMockQueueProducer;
    }

    public void sendMessage(@NotNull String message){
        jmsMockQueueProducer.send(message);
    }

}
