package mate.academy.service;

import java.util.List;
import mate.academy.dto.order.OrderDTo;
import mate.academy.dto.order.OrderItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrdersService {
    Page<OrderDTo> getOrdersByUser(String emailUser, Pageable pageable);

    OrderDTo addOrderFromShoppingCart(String emailUser, String shippingAddress);

    List<OrderItemDto> findOrderItemsByOrderId(String emailUser, Long id);
}
