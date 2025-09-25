package example.micronaut.mapper;

import example.micronaut.dto.CustomerDTO;
import example.micronaut.entity.Customer;

import org.mapstruct.Named;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "jsr330")
public interface CustomerMapper {


    @Mapping(target = "name", expression = "java(dto.getFirstName() + \" \" + dto.getLastName())")
    @Mapping(target = "phoneNumber" , source = "phoneNumber")
    Customer toEntity(CustomerDTO dto);


    @Mapping(target = "firstName", source = "name", qualifiedByName = "extractFirstName")
    @Mapping(target = "lastName", source = "name", qualifiedByName = "extractLastName")
    @Mapping(target = "phoneNumber" , source = "phoneNumber")
    CustomerDTO toDto(Customer customer);

    @Named("extractFirstName")
    static String extractFirstName(String name) {
        if (name == null || !name.contains(" ")) return name;
        return name.split(" ")[0];
    }

    @Named("extractLastName")
    static String extractLastName(String name) {
        if (name == null || !name.contains(" ")) return "";
        return name.split(" ")[1];
    }

}
