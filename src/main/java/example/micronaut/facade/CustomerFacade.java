package example.micronaut.facade;

import example.micronaut.entity.Customer;
import example.micronaut.service.CustomerService;
import example.micronaut.service.KafkaService;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;

@Singleton
public class CustomerFacade {

    private final CustomerService customerService;
    private final KafkaService kafkaService;

    public CustomerFacade(CustomerService customerService, KafkaService kafkaService) {
        this.customerService = customerService;
        this.kafkaService = kafkaService;
    }


    public Customer getCustomerById(long id){
        return customerService.getCustomerbyId(id);
    }


    public Customer registerNewCustomer(@NotBlank String name, @NotBlank String phoneNumber) {
        Customer customer = customerService.save(name, phoneNumber);
        kafkaService.sendKafkaMessage(customer);
        return customer;
    }

    public void removeCustomer(long id){
        customerService.delete(id);
    }

    public void updateCustomerInfo(long id, @NotBlank String name, @NotBlank String phoneNumber){
        customerService.update(id, name, phoneNumber);
    }
}
