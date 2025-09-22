package UserService.repository.httpClient;

import UserService.dto.request.UserProfileRequest;
import UserService.dto.response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "profile-service")
public interface ProfileClient {
    @PostMapping(value = "/profile/create", produces = MediaType.APPLICATION_JSON_VALUE)
    UserProfileResponse createUserProfile(@RequestBody UserProfileRequest userProfileRequest);
}
