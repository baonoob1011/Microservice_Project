package CartService.controller;

import CartService.dto.ApiResponse;
import CartService.dto.request.AddProductToCartRequest;
import CartService.dto.response.CartResponse;
import CartService.service.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {

    CartService cartService;

    @PostMapping("/add")
    public ApiResponse<CartResponse> addToCart(@RequestBody AddProductToCartRequest request) {
        return ApiResponse.<CartResponse>builder()
                .message("add to cart successful")
                .result(cartService.addToCart(request))
                .build();
    }
}
