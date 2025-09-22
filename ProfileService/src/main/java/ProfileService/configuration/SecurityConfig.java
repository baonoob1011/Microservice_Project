package ProfileService.configuration;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    private final String[] PUBLIC_ENDPOINTS = {
            "/create",
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, CustomAccessDeniedHandler customAccessDeniedHandler) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .anyRequest().authenticated()
                );
        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults())
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(customAccessDeniedHandler) // 403
                );
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }


}