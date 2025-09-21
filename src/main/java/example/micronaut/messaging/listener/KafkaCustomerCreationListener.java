package example.micronaut.messaging.listener;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@KafkaListener
public class KafkaCustomerCreationListener {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaCustomerCreationListener.class);

    @Topic("customer.created")
    public void receive(String message) {
        LOG.info("Received message from Kafka topic 'customer.created': {}", message);
    }
}
