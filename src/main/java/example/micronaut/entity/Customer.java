package example.micronaut.entity;

import io.micronaut.serde.annotation.Serdeable;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import jakarta.persistence.GeneratedValue;
import static jakarta.persistence.GenerationType.AUTO;

import jakarta.persistence.Id;

import jakarta.validation.constraints.NotNull;


@Serdeable
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "phoneNumber", nullable = false, unique = true)
    private String phoneNumber;

    public Customer() {}

    public Customer(@NotNull  String name, @NotNull String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone number='" + phoneNumber + '\'' +
                '}';
    }
}
