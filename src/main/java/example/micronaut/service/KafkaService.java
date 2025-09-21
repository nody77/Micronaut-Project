package example.micronaut.service;

import example.micronaut.messaging.producer.KafkaCustomerCreationProducer;
import example.micronaut.entity.Customer;
import jakarta.inject.Singleton;

@Singleton
public class KafkaService {

    public final KafkaCustomerCreationProducer kafkaCustomerCreationProducer;

    public KafkaService(KafkaCustomerCreationProducer kafkaCustomerCreationProducer) {
        this.kafkaCustomerCreationProducer = kafkaCustomerCreationProducer;
    }


    public void sendKafkaMessage(Customer customer){
        String kafkaMessage = "Customer name = " + customer.getName() + " Customer phone number = " + customer.getPhoneNumber();
        kafkaCustomerCreationProducer.sendCustomerCreated(customer.getId().toString(), kafkaMessage);
    }
}
