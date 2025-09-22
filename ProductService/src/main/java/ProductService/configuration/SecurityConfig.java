package ProductService.configuration;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    private final String[] PUBLIC_ENDPOINTS = {
            "/product/**",
            "/products",
    };
    private final String[] PUBLIC_ADMIN_ENDPOINTS = {
            "/create",

    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, CustomAccessDeniedHandler customAccessDeniedHandler) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(PUBLIC_ADMIN_ENDPOINTS).hasAnyRole("ADMIN")
                        .anyRequest().authenticated()
                );
        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer ->
                                jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(customAccessDeniedHandler) // 403
                );
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
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