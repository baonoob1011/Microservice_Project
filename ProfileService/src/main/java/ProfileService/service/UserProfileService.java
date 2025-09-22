package ProfileService.service;

import java.util.ArrayList;
import java.util.List;

import ProfileService.dto.request.UserProfileRequest;
import ProfileService.dto.response.UserProfileResponse;
import ProfileService.entity.UserProfile;
import ProfileService.mapper.UserProfileMapper;
import ProfileService.repository.UserProfileRepository;
import lombok.AccessLevel;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;

    // tạo user profile
    public UserProfileResponse createUserProfile(UserProfileRequest userProfileRequest) {
        UserProfile userProfile = userProfileMapper.toUserProfile(userProfileRequest);
        return userProfileMapper.toUserProfileResponse(
                userProfileRepository.save(userProfileMapper.toUserProfile(userProfileRequest)));
    }
}
