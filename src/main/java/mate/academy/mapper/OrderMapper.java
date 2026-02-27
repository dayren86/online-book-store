package mate.academy.mapper;

import mate.academy.config.MapperConfig;
import mate.academy.dto.order.OrderDTo;
import mate.academy.dto.order.OrderItemDto;
import mate.academy.model.Order;
import mate.academy.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderItemsDto", source = "orderItems")
    OrderDTo orderToDto(Order order);

    @Mapping(target = "bookId", source = "book.id")
    OrderItemDto orderItemToDto(OrderItem orderItem);
}
