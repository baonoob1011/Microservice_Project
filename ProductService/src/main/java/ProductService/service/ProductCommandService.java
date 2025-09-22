package ProductService.service;

import ProductService.dto.request.ProductRequest;
import ProductService.dto.response.ProductResponse;
import ProductService.mapper.ProductMapper;
import ProductService.repository.ProductRepository;
import common.dto.ProductStockUpdateEvent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ProductCommandService {
    ProductMapper productMapper;
    ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest request){
        return productMapper.toProductResponse(productRepository
                .save(productMapper.toProduct(request)));
    }
    @KafkaListener(topics = "productStockUpdate", groupId = "product-service",
            containerFactory = "kafkaListenerContainerFactory")
    public void handleStockUpdate(ProductStockUpdateEvent event) {
        log.info("Received stock update: {}", event);
        int updated = productRepository.updateStock(event.getProductId(), event.getQuantity());
        if (updated == 0) {
            log.warn("Not enough stock for productId: {}", event.getProductId());
        } else {
            log.info("Stock updated for productId: {}", event.getProductId());
        }
    }

}
