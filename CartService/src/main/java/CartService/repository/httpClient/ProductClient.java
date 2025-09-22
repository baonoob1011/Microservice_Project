package CartService.repository.httpClient;

import CartService.dto.ApiResponse;
import CartService.dto.request.AddProductToCartRequest;
import CartService.dto.response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "product-service")
public interface ProductClient {
    @PostMapping(value = "/product/product")
    ProductResponse getProduct(AddProductToCartRequest request);
}
