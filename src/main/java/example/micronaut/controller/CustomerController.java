package example.micronaut.controller;

import example.micronaut.entity.Customer;
import example.micronaut.facade.CustomerFacade;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import jakarta.validation.Valid;
import java.net.URI;

import static io.micronaut.http.HttpHeaders.LOCATION;

@ExecuteOn(TaskExecutors.BLOCKING)
@Controller("/customer")
public class CustomerController {

    private final CustomerFacade customerFacade;

    public CustomerController(CustomerFacade customerFacade) {
        this.customerFacade = customerFacade;
    }

    @Get("/{id}")
    Customer show(Long id){
        return customerFacade.getCustomerById(id);
    }

    @Put("/{id}")
    HttpResponse<Object> update(Long id, @Body @Valid Customer customer) {
        customerFacade.updateCustomerInfo(id, customer.getName(), customer.getPhoneNumber());

        return HttpResponse
                .noContent()
                .header(LOCATION, location(customer.getId()).getPath());
    }


    @Post
    HttpResponse<Customer> save(@Body @Valid Customer customer) {
        Customer newcustomer = customerFacade.registerNewCustomer(customer.getName(), customer.getPhoneNumber());

        return HttpResponse
                .created(newcustomer)
                .headers(headers -> headers.location(location(newcustomer.getId())));
    }

    @Delete("/{id}")
    HttpResponse<Void> delete(Long id) {
        customerFacade.removeCustomer(id);
        return HttpResponse.noContent();
    }

    private URI location(Long id) {
        return URI.create("/customer/" + id);
    }
}
