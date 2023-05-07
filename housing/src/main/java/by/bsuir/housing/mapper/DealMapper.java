package by.bsuir.housing.mapper;

import by.bsuir.housing.dto.DealDto;
import by.bsuir.housing.entity.Deal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UserMapper.class})
public interface DealMapper {
    DealMapper INSTANCE = Mappers.getMapper(DealMapper.class);

    @Mapping(target = "estateId", source = "estate.id")
    @Mapping(target = "userId", source = "user.id")
    DealDto toDto(Deal deal);

    @Mapping(target = "estate.id", source = "estateId")
    @Mapping(target = "user.id", source = "userId")
    Deal fromDto(DealDto dto);
}
