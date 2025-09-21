package example.micronaut.service;

import example.micronaut.entity.Customer;
import example.micronaut.service.KafkaService;
import example.micronaut.repository.CustomerRepositoryImpl;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;

@Singleton
public class CustomerService {

    private final CustomerRepositoryImpl customerRepository;
    private final KafkaService kafkaService;

    public CustomerService(CustomerRepositoryImpl customerRepository, KafkaService kafkaService) {
        this.customerRepository = customerRepository;
        this.kafkaService = kafkaService;

    }

    public Customer getCustomerById(long id){
        return customerRepository.getCustomerByID(id);
    }

    public Customer save(@NotBlank String name, @NotBlank String phoneNumber){
        Customer customer = customerRepository.addCustomer(name, phoneNumber);
        kafkaService.sendKafkaMessage(customer);
        return customer;
    }


    public void delete(long id){
        customerRepository.deleteCustomer(id);
    }


    public void update(long id, @NotBlank String name, @NotBlank String phoneNumber) {
        customerRepository.updateCustomer(id, name, phoneNumber);
    }
}
