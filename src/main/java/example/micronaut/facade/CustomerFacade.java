package example.micronaut.facade;

import example.micronaut.dto.CustomerDTO;
import example.micronaut.entity.Customer;
import example.micronaut.mapper.CustomerMapper;
import example.micronaut.service.CustomerService;
import example.micronaut.service.KafkaService;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;

@Singleton
public class CustomerFacade {

    private final CustomerService customerService;
    private final KafkaService kafkaService;
    private final CustomerMapper customerMapper;

    public CustomerFacade(CustomerService customerService, KafkaService kafkaService, CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.kafkaService = kafkaService;
        this.customerMapper = customerMapper;
    }


    public Customer getCustomerById(long id){
        return customerService.getCustomerbyId(id);
    }


    public Customer registerNewCustomer(CustomerDTO customerDTO) {

        Customer customer = customerMapper.toEntity(customerDTO);
        customer = customerService.save(customer.getName(), customerDTO.getPhoneNumber());
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
