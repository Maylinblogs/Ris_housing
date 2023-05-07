package by.bsuir.housing.controller;

import by.bsuir.housing.dto.DealDto;
import by.bsuir.housing.dto.UserDto;
import by.bsuir.housing.entity.Deal;
import by.bsuir.housing.entity.Estate;
import by.bsuir.housing.entity.User;
import by.bsuir.housing.service.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    @Value("${jwt.accessToken.lifetime}")
    private long accessTokenLifetime;

    @Value("${jwt.secret}")
    private String secret;

    private final UserService userService;

    @GetMapping("/{id}/favourites")
    @ResponseStatus(HttpStatus.OK)
    public List<Estate> listFavouritesForUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable Integer id
    ) {
        return userService.listFavouritesForUser(PageRequest.of(page, size), id).toList();
    }

    @PostMapping("/{id}/favourites")
    @ResponseStatus(HttpStatus.CREATED)
    public void addFavourite(@PathVariable Integer id, @RequestBody FavouriteRequest favouriteRequest) {
        userService.addFavourite(id, favouriteRequest.estateId);
    }

    @DeleteMapping("/{id}/favourites")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFavourite(@PathVariable Integer id, @RequestBody FavouriteRequest favouriteRequest) {
        userService.removeFavourite(id, favouriteRequest.estateId);
    }

    @GetMapping("/{id}/deals")
    @ResponseStatus(HttpStatus.OK)
    public List<DealDto> listDealsForUser(@PathVariable Integer id) {
        return userService.listDealsForUser(id);
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
//    x-www-form-urlencoded
    public UserDto addUser(UserDto userDto) {
        return userService.addUser(userDto);
    }

    @GetMapping("/refresh-token")
    @SneakyThrows
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authZHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authZHeader != null && authZHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authZHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                Optional<User> optionalUser = userService.getUserByEmail(username);
                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();
                    String accessToken = JWT.create()
                            .withSubject(user.getEmail())
                            .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenLifetime))
                            .withIssuer(request.getRequestURI())
                            .withIssuedAt(new Date(System.currentTimeMillis()))
                            .withClaim("roles", user.getRole().getRoleName())
                            .sign(algorithm);

                    Map<String, String> tokens = new HashMap<>();
                    tokens.put("accessToken", accessToken);
                    tokens.put("refreshToken", refreshToken);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), tokens);
                } else {
                    Map<String, Object> errors = new LinkedHashMap<>();
                    errors.put("httpStatus", HttpStatus.UNAUTHORIZED);
                    errors.put("errorCode", 40104);
                    errors.put("errorMessage", "No user for such username");
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), errors);
                }
            } catch (TokenExpiredException e) {
                Map<String, Object> errors = new LinkedHashMap<>();
                errors.put("httpStatus", HttpStatus.UNAUTHORIZED);
                errors.put("errorCode", 40101);
                errors.put("errorMessage", "Authentication token is expired");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), errors);
            } catch (Exception e) {
                Map<String, Object> errors = new LinkedHashMap<>();
                errors.put("httpStatus", HttpStatus.UNAUTHORIZED);
                errors.put("errorCode", 40102);
                errors.put("errorMessage", "Authentication token is invalid");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), errors);
            }
        } else {
            Map<String, Object> errors = new LinkedHashMap<>();
            errors.put("httpStatus", HttpStatus.UNAUTHORIZED);
            errors.put("errorCode", 40105);
            errors.put("errorMessage", "Authentication token is missing");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), errors);
        }
    }
}

class FavouriteRequest {
    public Integer estateId;
}