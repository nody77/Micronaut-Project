package example.micronaut.service;

import example.micronaut.entity.Customer;
import example.micronaut.exception.CustomerNotFoundException;
import example.micronaut.repository.CustomerRepositoryImpl;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;

@Singleton
public class CustomerService {

    private final CustomerRepositoryImpl customerRepository;

    public CustomerService(CustomerRepositoryImpl customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer getCustomerbyId(long id){
        return customerRepository.getCustomerByID(id);
    }

    public Customer save(@NotBlank String name, @NotBlank String phoneNumber){
         return customerRepository.addCustomer(name, phoneNumber);
    }


    public void delete(long id){
        customerRepository.deleteCustomer(id);
    }


    public void update(long id, @NotBlank String name, @NotBlank String phoneNumber) {
        customerRepository.updateCustomer(id, name, phoneNumber);
    }
}
