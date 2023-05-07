package by.bsuir.housing.mapper;

import by.bsuir.housing.dto.UserDto;
import by.bsuir.housing.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "password", source = "password", ignore = true)
    @Mapping(source = "role.roleName", target = "role")
    @Mapping(source = "person.firstName", target = "firstName")
    @Mapping(source = "person.lastName", target = "lastName")
    @Mapping(source = "person.passIdentificationNumber", target = "passIdentificationNumber")
    UserDto toDto(User user);

    @Mapping(source = "role", target = "role.roleName")
    @Mapping(source = "firstName", target = "person.firstName")
    @Mapping(source = "lastName", target = "person.lastName")
    @Mapping(source = "passIdentificationNumber", target = "person.passIdentificationNumber")
    User fromDto(UserDto dto);
}
