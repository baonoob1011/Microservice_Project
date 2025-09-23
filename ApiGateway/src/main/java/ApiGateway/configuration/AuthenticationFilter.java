package ApiGateway.configuration;

import ApiGateway.dto.ApiResponse;
import ApiGateway.dto.request.IntrospectRequest;
import ApiGateway.dto.response.IntrospectResponse;
import ApiGateway.service.IdentityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Component
@Slf4j
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final ObjectMapper objectMapper;
    private final ApplicationContext context;

    @Value("${app.api-prefix}")
    private String apiPrefix;

    private final String[] PUBLIC_ENDPOINT = {
            "/identity/login",
            "/identity/introspect",
            "/identity/create"
    };

    public AuthenticationFilter(ObjectMapper objectMapper, ApplicationContext context) {
        this.objectMapper = objectMapper;
        this.context = context;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (isPublicEndpoint(request)) {
            log.info("Accessing public endpoint: {}", request.getURI().getPath());
            return chain.filter(exchange);
        }

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Missing or invalid Authorization header");
            return unauthenticated(exchange.getResponse());
        }

        String token = authHeader.substring(7);
        log.info("Extracted Bearer token: {}", token);

        // Lazy get IdentityService tại runtime
        return Mono.fromCallable(() -> {
                    IdentityService identityService = context.getBean(IdentityService.class);
                    IntrospectResponse resp = identityService.introspect(
                            IntrospectRequest.builder().token(token).build()
                    );
                    log.info("Introspect response: {}", resp);
                    return resp;
                })
                .flatMap(resp -> {
                    if (resp.isActive()) {
                        log.info("Token is active, continue filter chain");
                        return chain.filter(exchange);
                    } else {
                        log.warn("Token inactive, returning 401");
                        return unauthenticated(exchange.getResponse());
                    }
                })
                .onErrorResume(e -> {
                    log.error("Error during introspect call", e);
                    return unauthenticated(exchange.getResponse());
                });
    }

    private boolean isPublicEndpoint(ServerHttpRequest request) {
        return Arrays.stream(PUBLIC_ENDPOINT)
                .anyMatch(s -> request.getURI().getPath().startsWith(apiPrefix + s));
    }

    private Mono<Void> unauthenticated(ServerHttpResponse response) {
        try {
            ApiResponse<?> apiResponse = ApiResponse.builder()
                    .code(1401)
                    .message("Unauthenticated")
                    .build();
            String body = objectMapper.writeValueAsString(apiResponse);

            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

            return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8))));
        } catch (Exception e) {
            log.error("Error writing unauthenticated response", e);
            return Mono.error(e);
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
