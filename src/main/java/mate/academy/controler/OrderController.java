package mate.academy.controler;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.order.CreateOrderDto;
import mate.academy.dto.order.OrderDTo;
import mate.academy.dto.order.OrderItemDto;
import mate.academy.model.User;
import mate.academy.service.OrdersService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrdersService ordersService;

    @GetMapping
    public Page<OrderDTo> getUserOrdersHistory(@AuthenticationPrincipal User user,
                                               Pageable pageable) {
        return ordersService.getOrdersByUser(user.getEmail(), pageable);
    }

    @PostMapping
    public OrderDTo addOrder(@AuthenticationPrincipal User user,
                             @Valid @RequestBody CreateOrderDto createOrderDto) {
        return ordersService.addOrderFromShoppingCart(user.getEmail(),
                createOrderDto.getShippingAddress());
    }

    @GetMapping("/{orderId}/items")
    public List<OrderItemDto> getOrderItems(@AuthenticationPrincipal User user,
                                            @PathVariable Long orderId) {
        return ordersService.findOrderItemsByOrderId(user.getEmail(), orderId);
    }

    @GetMapping("{orderId}/items/{id}")
    public void getSpecificItemInOrder() {

    }
}
