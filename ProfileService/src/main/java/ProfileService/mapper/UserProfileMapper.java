package ProfileService.mapper;

import ProfileService.dto.request.UserProfileRequest;
import ProfileService.dto.response.UserProfileResponse;
import ProfileService.entity.UserProfile;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(UserProfileRequest userProfileRequest);
    UserProfile toUserProfile(UserProfileResponse userProfileResponse);
    UserProfileResponse toUserProfileResponse(UserProfile userProfile);
}
