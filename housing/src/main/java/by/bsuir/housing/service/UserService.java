package by.bsuir.housing.service;

import by.bsuir.housing.dto.DealDto;
import by.bsuir.housing.dto.UserDto;
import by.bsuir.housing.entity.Estate;
import by.bsuir.housing.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.List;

public interface UserService {

    Page<Estate> listFavouritesForUser(Pageable pageable, Integer id);

    List<DealDto> listDealsForUser(Integer id);

    void addFavourite(Integer userId, Integer estateId);

    void removeFavourite(Integer userId, Integer estateId);

    Optional<User> getUserByEmail(String email);

    UserDto addUser(UserDto userDto);
}
