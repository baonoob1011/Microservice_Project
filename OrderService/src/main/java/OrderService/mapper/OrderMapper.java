package OrderService.mapper;

import OrderService.dto.request.OrderRequest;
import OrderService.dto.response.OrderResponse;
import OrderService.entity.Order;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toOrder(OrderRequest request);

    OrderResponse toOrderResponse(Order order);
}
