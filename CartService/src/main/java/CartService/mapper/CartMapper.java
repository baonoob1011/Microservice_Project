package CartService.mapper;

import CartService.dto.CartItemDTO;
import CartService.dto.response.CartResponse;
import CartService.dto.response.ProductResponse;
import CartService.entity.Cart;
import CartService.entity.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
   CartResponse toCartResponse(Cart cart);
   CartItem toCartItem(ProductResponse productResponse);
   CartItemDTO toCartItemDto(Cart cart);
}
