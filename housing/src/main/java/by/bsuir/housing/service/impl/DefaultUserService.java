package by.bsuir.housing.service.impl;

import by.bsuir.housing.dto.DealDto;
import by.bsuir.housing.entity.Estate;
import by.bsuir.housing.exception.NoSuchRecordException;
import by.bsuir.housing.mapper.DealMapper;
import by.bsuir.housing.mapper.UserMapper;
import by.bsuir.housing.dto.UserDto;
import by.bsuir.housing.entity.User;
import by.bsuir.housing.repository.*;
import by.bsuir.housing.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    private final PersonRepository personRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EstateRepository estateRepository;
    private final DealRepository dealRepository;

    @Override
    public Page<Estate> listFavouritesForUser(Pageable pageable, Integer id) {
        return estateRepository.findByFavouritesId(id, pageable);
    }

    @Override
    public void addFavourite(Integer userId, Integer estateId) {
        final var user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchRecordException(String.format("No user for id %d", userId)));
        final var estate = estateRepository.findById(estateId)
                .orElseThrow(() -> new NoSuchRecordException(String.format("No estate for id %d", estateId)));
        user.addToFavourites(estate);
        userRepository.save(user);
    }

    @Override
    public void removeFavourite(Integer userId, Integer estateId) {
        final var user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchRecordException(String.format("No user for id %d", userId)));
        final var estate = estateRepository.findById(estateId)
                .orElseThrow(() -> new NoSuchRecordException(String.format("No estate for id %d", estateId)));
        user.removeFromFavourites(estate);
        userRepository.save(user);
    }

    @Override
    public List<DealDto> listDealsForUser(Integer id) {
        return dealRepository.findByUser_Id(id).stream()
                .map(DealMapper.INSTANCE::toDto)
                .toList();
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        final var user = UserMapper.INSTANCE.fromDto(userDto);
        user.setPerson(personRepository.save(user.getPerson()));
        user.setRole(roleRepository.findByRoleName("User").orElseThrow());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return UserMapper.INSTANCE.toDto(user);
    }
}
