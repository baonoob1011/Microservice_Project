package NotificationService.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {

    @KafkaListener(topics = "onboard-successful")
    public void listen(String message) {
        log.info("Message receive: {}", message);
    }

    @KafkaListener(topics = "orderSuccessful")
    public void orderSuccessful(String message) {
        log.info("Message receive: {}", message);
    }
}
