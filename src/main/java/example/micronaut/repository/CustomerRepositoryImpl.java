package example.micronaut.repository;

import example.micronaut.entity.Customer;
import example.micronaut.exception.CustomerNotFoundException;
import example.micronaut.exception.CustomerNotUpdatedException;
import io.micronaut.transaction.annotation.ReadOnly;
import io.micronaut.transaction.annotation.Transactional;

import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotBlank;

@Singleton
public class CustomerRepositoryImpl implements CustomerRepository{

    private final EntityManager entityManager;

    public CustomerRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    //exception handler needed

    @Override
    @ReadOnly
    public Customer getCustomerByID(long id){
        Customer customer = entityManager.find(Customer.class, id);
        if(customer == null) {
             throw new CustomerNotFoundException("Customer with id " + id + " not found");
        }
        return customer;
    }

    @Override
    @Transactional
    public Customer addCustomer(@NotBlank String name, @NotBlank String phoneNumber){

        Customer newCustomer = new Customer(name, phoneNumber);
        entityManager.persist(newCustomer);
        return newCustomer;
    }

    @Override
    @Transactional
    public void deleteCustomer(long id){
        Customer customer = getCustomerByID(id);
        if (customer == null){
            throw new CustomerNotFoundException("Customer with id " + id + " not found");
        }
        entityManager.remove(customer);
    }


    @Override
    @Transactional
    public int updateCustomer(long id, @NotBlank String name, @NotBlank String phoneNumber){

         int numberOfEntitiesUpdated = entityManager.createQuery("UPDATE Customer c SET name =: name, phoneNumber =: phoneNumber WHERE id =: id")
                .setParameter("name", name).setParameter("phoneNumber", phoneNumber).setParameter("id", id)
                .executeUpdate();

        //handler or put it service
        if(numberOfEntitiesUpdated == 0){
             throw new CustomerNotUpdatedException("Customer with id " + id + " not found");
        }

        return numberOfEntitiesUpdated;
    }
}
