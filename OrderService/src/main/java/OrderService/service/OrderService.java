package OrderService.service;

import OrderService.dto.CartItem;
import OrderService.dto.CartItemDTO;
import OrderService.dto.request.OrderRequest;
import OrderService.dto.response.OrderResponse;
import OrderService.entity.Order;
import OrderService.mapper.OrderMapper;
import OrderService.repository.OrderRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class OrderService {
    OrderRepository orderRepository;
    OrderMapper orderMapper;
}
