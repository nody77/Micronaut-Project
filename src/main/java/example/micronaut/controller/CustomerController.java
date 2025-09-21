package example.micronaut.controller;

import example.micronaut.entity.Customer;
import example.micronaut.service.CustomerService;

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

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Get("/{id}")
    Customer show(Long id){
        return customerService.getCustomerById(id);
    }

    @Put("/{id}")
    HttpResponse<Object> update(Long id, @Body @Valid Customer customer) {
        customerService.update(id, customer.getName(), customer.getPhoneNumber());

        return HttpResponse
                .noContent()
                .header(LOCATION, location(customer.getId()).getPath());
    }


    @Post
    HttpResponse<Customer> save(@Body @Valid Customer customer) {
        Customer newcustomer = customerService.save(customer.getName(), customer.getPhoneNumber());

        return HttpResponse
                .created(newcustomer)
                .headers(headers -> headers.location(location(newcustomer.getId())));
    }

    @Delete("/{id}")
    HttpResponse<Void> delete(Long id) {
        customerService.delete(id);
        return HttpResponse.noContent();
    }

    private URI location(Long id) {
        return URI.create("/customer/" + id);
    }
}
