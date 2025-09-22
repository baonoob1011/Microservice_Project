package UserService.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    private final String[] PUBLIC_ENDPOINTS = {
            "/login",
            "/create",
            "/introspect",
    };

    @Bean
    // FilterChainProxy chứa tất cả filter của Spring Security (AuthenticationFilter, AuthorizationFilter, ExceptionTranslationFilter…)
// Nó là 1 Filter duy nhất mà Servlet container có thể gọi
// DelegatingFilterProxy là cầu nối giữa Servlet container và FilterChainProxy
// Khi bạn khai báo SecurityFilterChain Bean, Spring Boot tự động tạo DelegatingFilterProxy trỏ tới FilterChainProxy

    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, CustomAccessDeniedHandler customAccessDeniedHandler) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .anyRequest().authenticated()
                );
        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer ->
                                jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(customAccessDeniedHandler) // 403
                );
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new CustomAuthoritiesConverter());
        return jwtAuthenticationConverter;
    }

}