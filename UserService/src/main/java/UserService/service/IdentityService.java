package UserService.service;

import UserService.dto.identity.Credential;
import UserService.dto.identity.ExchangeTokenParam;
import UserService.dto.identity.TokenExchangeResponse;
import UserService.dto.identity.UserCreationParam;
import UserService.dto.request.IntrospectRequest;
import UserService.dto.request.LoginRequest;
import UserService.dto.request.RegistrationRequest;
import UserService.dto.request.UserProfileRequest;
import UserService.dto.response.IntrospectResponse;
import UserService.dto.response.RegistrationResponse;
import UserService.exception.AppException;
import UserService.exception.ErrorCode;
import UserService.exception.ErrorNormalizer;
import UserService.repository.IdentityClient;
import UserService.repository.httpClient.ProfileClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class IdentityService {
    IdentityClient identityClient;
    ErrorNormalizer errorNormalizer;
    KafkaTemplate<String, String> kafkaTemplate;
    ProfileClient profileClient;

    @NonFinal
    @Value("${ipd.client_id}")
    String clientId;

    @NonFinal
    @Value("${ipd.client_secret}")
    String clientSecret;

    @NonFinal
    @Value("${server.port}")
    String port;

    @NonFinal
    @Value("${test.message}")
    String message;


    public RegistrationResponse registerAccount(RegistrationRequest request) {
        try {
            var token = identityClient.exchangeToken(ExchangeTokenParam.builder()
                    .grant_type("client_credentials")
                    .client_id(clientId)
                    .client_secret(clientSecret)
                    .scope("openid")
                    .build());
            log.info("Token: {}", token);
            var response = identityClient.createUserAccount(
                    "Bearer " + token.getAccessToken(),
                    UserCreationParam.builder()
                            .username(request.getUsername())
                            .email(request.getEmail())
                            .emailVerified(false)
                            .enabled(true)
                            .lastName(request.getLastName())
                            .firstName(request.getFirstName())
                            .credentials(List.of(Credential.builder()
                                    .value(request.getPassword())
                                    .temporary(false)
                                    .type("password")
                                    .build()))
                            .build()
            );
            String userId = extractUserId(response);
            log.info("userId: {}", userId);

            //call profile Service
            profileClient.createUserProfile(UserProfileRequest.builder()
                    .userId(userId)
                    .username(request.getUsername())
                    .phone(request.getPhone())
                    .address(request.getAddress())
                    .dateOfBirth(request.getDateOfBirth())
                    .build());

            //Public Message to Kafka
            kafkaTemplate.send("onboard-successful", "Welcome out new member: " + request.getUsername());
            log.info("Login successful");
            return RegistrationResponse.builder()
                    .userId(userId)
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .build();
        } catch (FeignException e) {
            throw errorNormalizer.handlerFeignException(e);
        }
    }

    public TokenExchangeResponse login(LoginRequest request) {
        try {
            log.info("{} is logging", port);
            log.info("Login successful");
            log.info("Message config server: {}",message);

            return identityClient.exchangeToken(ExchangeTokenParam.builder()
                    .grant_type("password")
                    .client_id(clientId)
                    .client_secret(clientSecret)
                    .username(request.getUsername())
                    .password(request.getPassword())
                    .scope("openid")
                    .build());
        } catch (FeignException e) {
            throw new AppException(ErrorCode.LOGIN_FAILED);
        }

    }

    public IntrospectResponse introspect(IntrospectRequest request) {
        return identityClient.introspect(ExchangeTokenParam.builder()
                .client_id(clientId)
                .client_secret(clientSecret)
                .token(request.getToken())
                .build());
    }
    public IntrospectResponse logout(IntrospectRequest request) {
        return identityClient.logout(ExchangeTokenParam.builder()
                .client_id(clientId)
                .client_secret(clientSecret)
                .token(request.getToken())
                .token_type_hint("refresh_token")
                .build());
    }

    private String extractUserId(ResponseEntity<?> response) {
        String location = response.getHeaders().get("Location").getFirst();
        String[] slitStr = location.split("/");
        return slitStr[slitStr.length - 1];
    }
}
