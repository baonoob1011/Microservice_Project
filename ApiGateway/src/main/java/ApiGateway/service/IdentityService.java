package ApiGateway.service;

import ApiGateway.dto.identity.ExchangeTokenParam;
import ApiGateway.dto.request.IntrospectRequest;
import ApiGateway.dto.response.IntrospectResponse;
import ApiGateway.repository.httpClient.IdentityClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {
    IdentityClient identityClient;

    @NonFinal
    @Value("${ipd.client_id}")
    String clientId;

    @NonFinal
    @Value("${ipd.client_secret}")
    String clientSecret;


    public IntrospectResponse introspect(IntrospectRequest request) {
        return identityClient.introspect(ExchangeTokenParam.builder()
                .client_id(clientId)
                .client_secret(clientSecret)
                .token(request.getToken())
                .build());
    }
}
