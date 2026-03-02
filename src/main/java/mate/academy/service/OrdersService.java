package mate.academy.service;

import java.util.List;
import mate.academy.dto.order.OrderDTo;
import mate.academy.dto.order.OrderItemDto;
import mate.academy.dto.order.UpdateStatusDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrdersService {
    Page<OrderDTo> getOrdersByUser(String emailUser, Pageable pageable);

    OrderDTo addOrderFromShoppingCart(String emailUser, String shippingAddress);

    void updateStatus(Long id, UpdateStatusDto status);

    List<OrderItemDto> findOrderItemsByOrderId(String emailUser, Long id);

    OrderItemDto getSpecificItem(String emailUser, Long orderId, Long id);
}
