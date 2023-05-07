package by.bsuir.housing.security.jwt;

import by.bsuir.housing.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import by.bsuir.housing.entity.User;

@Service
@Transactional
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        user = userService.getUserByEmail(username).orElseThrow(() -> new UsernameNotFoundException("")); //being caught higher, unsuccessfulAuthentication  method called
        return JwtUserFactory.create(user);
    }
}
