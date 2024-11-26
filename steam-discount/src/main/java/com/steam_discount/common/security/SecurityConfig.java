package com.steam_discount.common.security;


import com.steam_discount.common.security.jwt.JwtAuthenticationFilter;
import com.steam_discount.common.security.jwt.user.CustomAuthenticationEntryPoint;
import com.steam_discount.common.security.jwt.user.CustomOAuth2AuthenticationSuccessHandler;
import com.steam_discount.common.security.jwt.user.CustomOAuth2UserService;
import com.steam_discount.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final CorsConfig corsConfig;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()));
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.formLogin(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/api/logout").hasAnyRole(UserRole.USER.name(), UserRole.ADMIN.name())
            .requestMatchers("/api/token-check").hasAnyRole(UserRole.USER.name(), UserRole.ADMIN.name())


            .requestMatchers(HttpMethod.GET,
                "/api/users/email").hasAnyRole(UserRole.USER.name(), UserRole.ADMIN.name())
            .requestMatchers(HttpMethod.POST,
                "/api/boards", "/api/posts/", "/api/users").hasAnyRole(UserRole.USER.name(), UserRole.ADMIN.name())
            .requestMatchers(HttpMethod.PUT,
                "/api/posts/", "/api/users/disable").hasAnyRole(UserRole.USER.name(), UserRole.ADMIN.name())
            .requestMatchers(HttpMethod.PATCH,
                "/api/users/nickname", "/api/users/password").hasAnyRole(UserRole.USER.name(), UserRole.ADMIN.name())

            .requestMatchers(HttpMethod.GET,
                "/api/users/all").hasRole(UserRole.ADMIN.name())
            .requestMatchers(HttpMethod.PUT,
                "/api/users/disable/", "/api/users/enable/").hasRole(UserRole.ADMIN.name())


            .requestMatchers(HttpMethod.POST, "/api/users/create").permitAll()

            .anyRequest().permitAll());

        http.oauth2Login(oauth2 -> oauth2
            .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOAuth2UserService)))
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(customAuthenticationEntryPoint));

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
