package ProductService.controller;

import ProductService.dto.ApiResponse;
import ProductService.dto.request.AddProductToCartRequest;
import ProductService.dto.request.ProductRequest;
import ProductService.dto.response.ProductResponse;
import ProductService.service.QueryProductService;
import ProductService.service.ProductCommandService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
    ProductCommandService saveProductService;
    QueryProductService queryProductService;

    @GetMapping("/products")
    ApiResponse<List<ProductResponse>> getAll() {
        return ApiResponse.<List<ProductResponse>>builder()
                .message("get products successful")
                .result(queryProductService.getAll())
                .build();
    }

    @PostMapping("/product")
    ProductResponse getProduct(@RequestBody AddProductToCartRequest request) {
        return queryProductService.getProduct(request);
    }

    @PostMapping("/create")
    ApiResponse<ProductResponse> create(@RequestBody ProductRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .message("Create product Successful")
                .result(saveProductService.createProduct(request))
                .build();
    }
}
