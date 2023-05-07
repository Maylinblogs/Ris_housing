package by.bsuir.housing.mapper;

import by.bsuir.housing.dto.ReviewDto;
import by.bsuir.housing.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UserMapper.class})
public interface ReviewMapper {
    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    @Mapping(target = "estateId", source = "estate.id")
    @Mapping(target = "authorId", source = "author.id")
    ReviewDto toDto(Review review);

    @Mapping(target = "estate.id", source = "estateId")
    @Mapping(target = "author.id", source = "authorId")
    Review fromDto(ReviewDto dto);
}
