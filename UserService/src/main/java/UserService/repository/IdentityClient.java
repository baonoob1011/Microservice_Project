package UserService.repository;

import UserService.dto.identity.ExchangeTokenParam;
import UserService.dto.identity.TokenExchangeResponse;
import UserService.dto.identity.UserCreationParam;
import UserService.dto.response.IntrospectResponse;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "identity-service", url = "${ipd.url}")
public interface IdentityClient {
    @PostMapping(value = "/realms/baonoob/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenExchangeResponse exchangeToken(@QueryMap ExchangeTokenParam param);

    @PostMapping(value = "/admin/realms/baonoob/users",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createUserAccount(@RequestHeader("authorization") String token,
                                        @RequestBody UserCreationParam param);

    @PostMapping(value = "/realms/baonoob/protocol/openid-connect/token/introspect",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    IntrospectResponse introspect(@QueryMap ExchangeTokenParam param);

    @PostMapping(value = "/realms/baonoob/protocol/openid-connect/revoke",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    IntrospectResponse logout(@QueryMap ExchangeTokenParam param);
}
