package CartService.service;
import common.dto.ProductStockUpdateEvent;

import CartService.dto.request.AddProductToCartRequest;
import CartService.dto.response.CartResponse;
import CartService.dto.response.ProductResponse;
import CartService.entity.Cart;
import CartService.entity.CartItem;
import CartService.exception.AppException;
import CartService.exception.ErrorCode;
import CartService.mapper.CartMapper;
import CartService.repository.CartRepository;
import CartService.repository.httpClient.ProductClient;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService {
    ProductClient productClient;
    CartRepository cartRepository;
    CartMapper cartMapper;
    KafkaTemplate<String, ProductStockUpdateEvent> kafkaTemplate;

    public CartResponse addToCart(AddProductToCartRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId;
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            userId = jwt.getSubject();
        } else {
            throw new RuntimeException("User not authenticated");
        }

        ProductResponse product;
        try {
            product = productClient.getProduct(request);
            if (product == null) throw new RuntimeException("Product not found");
        } catch (FeignException e) {
            throw new AppException(ErrorCode.HET_SAN_PHAM);
        }

        log.info("ProductId: {}", product.getProductId());

        Cart cart = cartRepository.findByUserId(userId)
                .orElse(Cart.builder()
                        .userId(userId)
                        .items(new ArrayList<>())
                        .build());

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId() != null
                        && item.getProductId().equals(request.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            item.setPrice(product.getPrice() * item.getQuantity()); // total = unitPrice * quantity
        } else {
            CartItem newItem = CartItem.builder()
                    .productId(product.getProductId())
                    .productName(product.getProductName())
                    .price(product.getPrice() * request.getQuantity()) // tổng tiền = đơn giá * số lượng
                    .quantity(request.getQuantity())
                    .build();
            cart.getItems().add(newItem);
        }

        try {
            // Retry save
            Cart savedCart = saveCartWithRetry(cart);

            // Gửi event stock update
            ProductStockUpdateEvent stockEvent = new ProductStockUpdateEvent(product.getProductId(), request.getQuantity());
            kafkaTemplate.send("productStockUpdate", stockEvent);

            return cartMapper.toCartResponse(savedCart);
        } catch (Exception e) {
            log.error("Cannot save cart, rolling back stock", e);

            // Gửi event rollback stock
            ProductStockUpdateEvent rollbackEvent = new ProductStockUpdateEvent(product.getProductId(), -request.getQuantity());
            kafkaTemplate.send("productStockUpdate", rollbackEvent);

            throw new RuntimeException("Add to cart failed, stock rolled back", e);
        }
    }


    @Retryable(
            value = { DataAccessException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000))
    public Cart saveCartWithRetry(Cart cart) {
        return cartRepository.save(cart);
    }
}
