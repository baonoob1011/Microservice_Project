package ProductService.mapper;

import ProductService.dto.request.ProductRequest;
import ProductService.dto.response.ProductResponse;
import ProductService.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(ProductRequest productRequest);
    ProductResponse toProductResponse(Product product);
}
