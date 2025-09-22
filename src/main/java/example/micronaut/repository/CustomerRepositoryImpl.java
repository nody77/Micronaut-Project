package example.micronaut.repository;

import example.micronaut.entity.Customer;
import example.micronaut.exception.CustomerNotFoundException;
import example.micronaut.exception.DuplicatePhoneNumberException;
import example.micronaut.exception.CustomerNotUpdatedException;
import io.micronaut.transaction.annotation.ReadOnly;
import io.micronaut.transaction.annotation.Transactional;

import jakarta.inject.Singleton;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.exception.ConstraintViolationException;

@Singleton
public class CustomerRepositoryImpl implements CustomerRepository{

    private final EntityManager entityManager;

    public CustomerRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }



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

        try {
            Customer newCustomer = new Customer(name, phoneNumber);
            entityManager.persist(newCustomer);
            entityManager.flush();
            return newCustomer;
        }
        catch(PersistenceException e){
            if (isConstraintViolationException(e)) {
                throw new DuplicatePhoneNumberException("This phone number is already in use");
            }
            throw e;
        }
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
             throw new CustomerNotUpdatedException("Customer with id " + id + " not updated");
        }

        return numberOfEntitiesUpdated;
    }

    private boolean isConstraintViolationException(Throwable e) {
        while (e != null) {
            if (e instanceof ConstraintViolationException) {
                return true;
            }
            e = e.getCause();
        }
        return false;
    }

}
