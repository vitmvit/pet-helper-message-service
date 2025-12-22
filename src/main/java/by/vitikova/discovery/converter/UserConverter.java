package by.vitikova.discovery.converter;

import by.vitikova.discovery.UserDto;
import by.vitikova.discovery.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserConverter {

    User convert(UserDto source);
}