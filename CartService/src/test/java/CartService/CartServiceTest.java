package CartService;

import CartService.dto.request.AddProductToCartRequest;
import CartService.dto.response.CartResponse;
import CartService.dto.response.ProductResponse;
import CartService.mapper.CartMapper;
import CartService.repository.CartRepository;
import CartService.repository.httpClient.ProductClient;
import CartService.service.CartService;
import common.dto.ProductStockUpdateEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    ProductClient productClient;

    @Mock
    CartRepository cartRepository;

    @Mock
    CartMapper cartMapper;

    @Mock
    KafkaTemplate<String, ProductStockUpdateEvent> kafkaTemplate;

    @InjectMocks
    CartService cartService;


    @Test
    void testAddToCart_success() {
        // Mock SecurityContext + Authentication
        Jwt jwt = Mockito.mock(Jwt.class);
        when(jwt.getSubject()).thenReturn("user123");

        Authentication auth = Mockito.mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(jwt);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        // Prepare request + mocks as before
        AddProductToCartRequest request = new AddProductToCartRequest();
        request.setProductId(1L);
        request.setQuantity(2);

        ProductResponse product = new ProductResponse();
        product.setProductId(1L);
        product.setProductName("Product A");
        product.setPrice(100);

        when(productClient.getProduct(any())).thenReturn(product);
        when(cartRepository.findByUserId(anyString())).thenReturn(Optional.empty());
        when(cartRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(cartMapper.toCartResponse(any())).thenReturn(new CartResponse());

        CartResponse response = cartService.addToCart(request);

        verify(cartRepository, times(1)).save(any());
        verify(kafkaTemplate, times(1)).send(eq("productStockUpdate"), any(ProductStockUpdateEvent.class));
        assertNotNull(response);
    }

    @Test
    void testAddToCart_saveFail_thenRollbackStock() {
        AddProductToCartRequest request = new AddProductToCartRequest();
        request.setProductId(1L);
        request.setQuantity(2);

        ProductResponse product = new ProductResponse();
        product.setProductId(1L);
        product.setProductName("Product A");
        product.setPrice(100);

        when(productClient.getProduct(any())).thenReturn(product);
        when(cartRepository.findByUserId(anyString())).thenReturn(Optional.empty());
        when(cartRepository.save(any())).thenThrow(new DataAccessException("DB down") {});

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> cartService.addToCart(request));

        verify(cartRepository, times(3)).save(any()); // 3 retry
        verify(kafkaTemplate, times(1))
                .send(eq("productStockUpdate"), argThat(e -> e.getQuantity() == -2)); // rollback event
    }
}
