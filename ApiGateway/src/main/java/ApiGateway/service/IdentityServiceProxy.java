package ApiGateway.service;

import ApiGateway.dto.request.IntrospectRequest;
import ApiGateway.dto.response.IntrospectResponse;
import org.springframework.stereotype.Component;

// 1. Tạo class proxy
@Component
public class IdentityServiceProxy {

    private final IdentityService identityService;

    public IdentityServiceProxy(IdentityService identityService) {
        this.identityService = identityService;
    }

    public IntrospectResponse introspectToken(String token) {
        return identityService.introspect(
                IntrospectRequest.builder().token(token).build()
        );
    }
}
