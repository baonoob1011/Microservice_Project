package UserService.controller;

import UserService.dto.ApiResponse;
import UserService.dto.identity.TokenExchangeResponse;
import UserService.dto.request.IntrospectRequest;
import UserService.dto.request.LoginRequest;
import UserService.dto.request.RegistrationRequest;
import UserService.dto.response.IntrospectResponse;
import UserService.dto.response.RegistrationResponse;
import UserService.service.IdentityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class IdentityController {
    IdentityService identityService;

    @PostMapping("/create")
    ApiResponse<RegistrationResponse> create(@RequestBody RegistrationRequest request) {
        return ApiResponse.<RegistrationResponse>builder()
                .message("Create Successful")
                .result(identityService.registerAccount(request))
                .build();
    }

    @PostMapping("/login")
    ApiResponse<TokenExchangeResponse> login(@RequestBody LoginRequest request) {
        return ApiResponse.<TokenExchangeResponse>builder()
                .message("Create Successful")
                .result(identityService.login(request))
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) {
        return ApiResponse.<IntrospectResponse>builder()
                .message("Introspect Successful")
                .result(identityService.introspect(request))
                .build();
    }

    @PostMapping("/logout-app")
    ApiResponse<IntrospectResponse> logout(@RequestBody IntrospectRequest request) {
        return ApiResponse.<IntrospectResponse>builder()
                .message("logout Successful")
                .result(identityService.logout(request))
                .build();
    }
}
