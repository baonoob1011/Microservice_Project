package ProfileService.controller;

import java.util.List;

import ProfileService.dto.request.UserProfileRequest;
import ProfileService.dto.response.UserProfileResponse;
import ProfileService.service.UserProfileService;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileController {
    UserProfileService userProfileService;

    @PostMapping("/create")
    UserProfileResponse createUserProfile(@RequestBody UserProfileRequest userProfileRequest) {
        return userProfileService.createUserProfile(userProfileRequest);
    }
}
