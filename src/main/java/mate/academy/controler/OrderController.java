package mate.academy.controler;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.order.CreateOrderDto;
import mate.academy.model.User;
import mate.academy.service.OrdersService;
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
    public void getUserOrdersHistory(@AuthenticationPrincipal User user) {
        ordersService.getOrderByUser(user.getEmail());
    }

    @PostMapping
    public void addOrder(@AuthenticationPrincipal User user, @Valid @RequestBody CreateOrderDto createOrderDto) {
        ordersService.addOrderFromShoppingCart(user.getEmail(), createOrderDto.getShippingAddress());
    }

    @GetMapping("/{orderId}/items")
    public void getOrderItems(@AuthenticationPrincipal User user, @PathVariable Long orderId) {

    }

    @GetMapping("{orderId}/items/{id}")
    public void getSpecificItemInOrder() {

    }
}
