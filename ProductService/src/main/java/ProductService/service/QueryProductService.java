package ProductService.service;

import ProductService.dto.request.AddProductToCartRequest;
import ProductService.dto.request.ProductRequest;
import ProductService.dto.response.ProductResponse;
import ProductService.entity.Product;
import ProductService.exception.AppException;
import ProductService.exception.ErrorCode;
import ProductService.mapper.ProductMapper;
import ProductService.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QueryProductService {
    ProductMapper productMapper;
    ProductRepository productRepository;

    public List<ProductResponse> getAll() {
        var products = productRepository.findAll();
        return products.stream().
                map(productMapper::toProductResponse)
                .toList();
    }

    public ProductResponse getProduct(AddProductToCartRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        if (request.getQuantity() > product.getQuantity()) {
            throw new RuntimeException("het san pham");
        }
        return productMapper.toProductResponse(product);
    }
}
