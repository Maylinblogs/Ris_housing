package by.bsuir.housing.security;

import by.bsuir.housing.filter.JwtAuthenticationFilter;
import by.bsuir.housing.filter.JwtAuthorizationFilter;
import by.bsuir.housing.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final SecurityUtils securityUtils;
    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationProvider, userService, securityUtils.getSecret(), securityUtils.getAccessTokenLifetime(), securityUtils.getRefreshTokenLifetime());
        jwtAuthenticationFilter.setFilterProcessesUrl("/user/login");
        http
                .csrf()
                .disable()
                .cors()
                .and()
//                .authorizeHttpRequests()
//                .requestMatchers(HttpMethod.POST, "/user/signup", "/user/login", "/user/refresh-token").permitAll()
//                .and()
//                .authorizeHttpRequests()
//                .requestMatchers(HttpMethod.POST, "/deal/**").hasAuthority("User")
//                .and()
//                .authorizeHttpRequests()
//                .requestMatchers(HttpMethod.GET, "/deal").hasAuthority("User")
//                .and()
//                .authorizeHttpRequests()
//                .requestMatchers(HttpMethod.POST, "/deal/**").hasAuthority("User")
//                .and()
//                .authorizeHttpRequests()
//                .requestMatchers("/estate/*/report").hasAuthority("Admin")
//                .and()
//                .authorizeHttpRequests()
//                .requestMatchers("/user/*/favourites").hasAuthority("User")
//                .and()
//                .authorizeHttpRequests()
//                .requestMatchers(HttpMethod.GET).permitAll()
//                .and()
//                .authorizeHttpRequests()
//                .anyRequest().hasAuthority("Admin")
//                .and()
                .authorizeHttpRequests()//вот это не надо показывать
                .anyRequest().permitAll()//вот это не надо показывать
                .and()//вот это не надо показывать
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilter(jwtAuthenticationFilter)
                .addFilterBefore(new JwtAuthorizationFilter(securityUtils.getSecret()), UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addExposedHeader(HttpHeaders.AUTHORIZATION);
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
