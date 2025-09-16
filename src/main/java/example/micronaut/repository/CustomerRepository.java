package example.micronaut.repository;

import example.micronaut.entity.Customer;
import jakarta.validation.constraints.NotBlank;
import java.util.Optional;

public interface CustomerRepository {

    // Add  a Customer to database
    Customer addCustomer(@NotBlank String name, @NotBlank String phoneNumber);

    // Retrieve a Customer from database
    Customer getCustomerByID(long id);

    // Update a Customer data in the database
    int updateCustomer(long id, @NotBlank String name, @NotBlank String phoneNumber);

    // Delete a Customer from database
    void deleteCustomer(long id);


}
