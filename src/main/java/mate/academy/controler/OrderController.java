package mate.academy.controler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.order.CreateOrderDto;
import mate.academy.dto.order.OrderDTo;
import mate.academy.dto.order.OrderItemDto;
import mate.academy.dto.order.UpdateStatusDto;
import mate.academy.model.User;
import mate.academy.service.OrdersService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order api")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrdersService ordersService;

    @Operation(summary = "Get user order history")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    public Page<OrderDTo> getUserOrdersHistory(@AuthenticationPrincipal User user,
                                               Pageable pageable) {
        return ordersService.getOrdersByUser(user.getEmail(), pageable);
    }

    @Operation(summary = "Create order from shopping cart")
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTo createOrder(@AuthenticationPrincipal User user,
                             @Valid @RequestBody CreateOrderDto createOrderDto) {
        return ordersService.addOrderFromShoppingCart(user.getEmail(),
                createOrderDto.getShippingAddress());
    }

    @Operation(summary = "Get all items from order")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{orderId}/items")
    public List<OrderItemDto> getOrderItems(@AuthenticationPrincipal User user,
                                            @PathVariable Long orderId) {
        return ordersService.findOrderItemsByOrderId(user.getEmail(), orderId);
    }

    @Operation(summary = "Get specific item from order by id")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("{orderId}/items/{id}")
    public OrderItemDto getSpecificItemInOrder(@AuthenticationPrincipal User user,
                                       @PathVariable Long orderId,
                                       @PathVariable Long id) {
        return ordersService.getSpecificItem(user.getEmail(), orderId, id);
    }

    @Operation(summary = "Update order status")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateOrderStatus(@PathVariable Long id,
                                  @RequestBody @Valid UpdateStatusDto status) {
        ordersService.updateStatus(id, status);
    }
}
