package example.micronaut.messaging.producer;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface KafkaCustomerCreationProducer {

    @Topic("customer.created")
    void sendCustomerCreated(String key, String message);

}
