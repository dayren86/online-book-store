package mate.academy.mapper;

import java.math.BigDecimal;
import mate.academy.config.MapperConfig;
import mate.academy.dto.order.OrderDTo;
import mate.academy.dto.order.OrderItemDto;
import mate.academy.model.Book;
import mate.academy.model.CartItem;
import mate.academy.model.Order;
import mate.academy.model.OrderItem;
import mate.academy.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderItemsDto", source = "orderItems")
    OrderDTo orderToDto(Order order);

    @Mapping(target = "bookId", source = "book.id")
    OrderItemDto orderItemToDto(OrderItem orderItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "orderDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "status", expression = "java(Order.OrderStatus.PENDING)")
    @Mapping(target = "shippingAddress", source = "shippingAddress")
    Order toOrderModel(User user, String shippingAddress);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "price", qualifiedByName = "getPrice", source = "cartItem.book")
    OrderItem cartItemToOrderItem(CartItem cartItem, Order order);

    @Named("getPrice")
    default BigDecimal getPrice(Book book) {
        return book.getPrice();
    }
}
