package ProductService.service;

import ProductService.dto.request.ProductRequest;
import ProductService.dto.response.ProductResponse;
import ProductService.entity.Product;
import ProductService.mapper.ProductMapper;
import ProductService.repository.ProductRepository;
import common.dto.ProductStockUpdateEvent;
import com.mongodb.client.result.UpdateResult;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductCommandService {
    SequenceGeneratorService sequenceGeneratorService; // thêm
    ProductMapper productMapper;
    ProductRepository productRepository;
    MongoTemplate mongoTemplate;

    public ProductResponse createProduct(ProductRequest request){
        Product product = productMapper.toProduct(request);
        // Tự generate Long id
        product.setProductId(sequenceGeneratorService.generateSequence("productId"));
        return productMapper.toProductResponse(productRepository.save(product));
    }

    @KafkaListener(topics = "productStockUpdate", groupId = "product-service",
            containerFactory = "kafkaListenerContainerFactory")
    public void handleStockUpdate(ProductStockUpdateEvent event) {
        log.info("Received stock update: {}", event);
        boolean success = updateStock(event.getProductId(), event.getQuantity());
        if (!success) {
            log.warn("Not enough stock for productId: {}", event.getProductId());
        } else {
            log.info("Stock updated for productId: {}", event.getProductId());
        }
    }

    public boolean updateStock(Long productId, long quantity) {
        // Query đúng field productId chứ không phải _id
        Query query = new Query(Criteria.where("productId").is(productId));

        Update update;
        if (quantity > 0) {
            // Giảm stock nhưng tránh stock âm
            query.addCriteria(Criteria.where("quantity").gte(quantity));
            update = new Update().inc("quantity", -quantity);
        } else {
            // Rollback: tăng stock khi có lỗi
            update = new Update().inc("quantity", +quantity);
        }

        UpdateResult result = mongoTemplate.updateFirst(query, update, Product.class);
        log.info("Updated {} document(s) for productId {}", result.getModifiedCount(), productId);
        return result.getModifiedCount() > 0;
    }


}
