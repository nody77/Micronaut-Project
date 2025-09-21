package example.micronaut.service;

import example.micronaut.entity.Customer;
import example.micronaut.messaging.producer.KafkaCustomerCreationProducer;
import example.micronaut.repository.CustomerRepositoryImpl;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;

@Singleton
public class CustomerService {

    private final CustomerRepositoryImpl customerRepository;
    public final KafkaCustomerCreationProducer kafkaCustomerCreationProducer;

    public CustomerService(CustomerRepositoryImpl customerRepository, KafkaCustomerCreationProducer kafkaCustomerCreationProducer) {
        this.customerRepository = customerRepository;
        this.kafkaCustomerCreationProducer = kafkaCustomerCreationProducer;
    }

    public Customer getCustomerbyId(long id){
        return customerRepository.getCustomerByID(id);
    }

    public Customer save(@NotBlank String name, @NotBlank String phoneNumber){
        Customer customer = customerRepository.addCustomer(name, phoneNumber);

        String kafkaMessage = "Customer name = " + name + " Customer phone number = " + phoneNumber;
        kafkaCustomerCreationProducer.sendCustomerCreated(customer.getId().toString(), kafkaMessage);
        System.out.println("Kafka message sent: " + kafkaMessage);
        return customer;
    }


    public void delete(long id){
        customerRepository.deleteCustomer(id);
    }


    public void update(long id, @NotBlank String name, @NotBlank String phoneNumber) {
        customerRepository.updateCustomer(id, name, phoneNumber);
    }
}
