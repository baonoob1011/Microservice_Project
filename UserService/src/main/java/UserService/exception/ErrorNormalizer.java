package UserService.exception;

import UserService.dto.identity.ErrorKeyCloakMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ErrorNormalizer {
    ObjectMapper objectMapper;
    Map<String, ErrorCode> errorCodeMap;

    public ErrorNormalizer() {
        objectMapper=new ObjectMapper();
        errorCodeMap=new HashMap<>();
        errorCodeMap.put("User exists with same username", ErrorCode.USER_EXISTED);
        errorCodeMap.put("User exists with same email", ErrorCode.EMAIL_EXISTED);
        errorCodeMap.put("User name is missing", ErrorCode.USERNAME_IS_MISSING);
    }

    public AppException handlerFeignException(FeignException e) {
        try{
            var response=objectMapper.readValue(e.contentUTF8(), UserService.dto.identity.ErrorKeyCloakMessage.class);
            if(Objects.nonNull(response.getErrorMessage())&&
            Objects.nonNull(errorCodeMap.get(response.getErrorMessage()))){
                return new AppException(errorCodeMap.get(response.getErrorMessage()));
            }
        } catch (JsonProcessingException ex) {
            log.error("Cannot serialize content",e);
        }
        return new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
    }
}
