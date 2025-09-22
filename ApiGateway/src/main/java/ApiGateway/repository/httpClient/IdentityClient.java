package ApiGateway.repository.httpClient;

import ApiGateway.dto.identity.ExchangeTokenParam;
import ApiGateway.dto.response.IntrospectResponse;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
@FeignClient(name = "identity-service", url = "${ipd.url}")
public interface IdentityClient {
    @PostMapping(value = "/realms/baonoob/protocol/openid-connect/token/introspect",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    IntrospectResponse introspect(@QueryMap ExchangeTokenParam param);
}
