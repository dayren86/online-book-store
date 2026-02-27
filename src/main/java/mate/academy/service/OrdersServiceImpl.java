package mate.academy.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.order.OrderDTo;
import mate.academy.dto.order.OrderItemDto;
import mate.academy.mapper.OrderMapper;
import mate.academy.model.CartItem;
import mate.academy.model.Order;
import mate.academy.model.OrderItem;
import mate.academy.model.ShoppingCart;
import mate.academy.repository.OrderItemsRepository;
import mate.academy.repository.OrderRepository;
import mate.academy.repository.ShoppingCartRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {
    private final OrderRepository orderRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;

    @Override
    public Page<OrderDTo> getOrdersByUser(String emailUser, Pageable pageable) {
        return orderRepository.findByUserEmail(emailUser, pageable)
                .map(orderMapper::orderToDto);
    }

    @Override
    public OrderDTo addOrderFromShoppingCart(String emailUser, String shippingAddress) {
        ShoppingCart shoppingCartByUser = findShoppingCartByUser(emailUser);

        Order order = new Order();
        order.setUser(shoppingCartByUser.getUser());
        order.setStatus(Order.Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(shippingAddress);

        Set<OrderItem> orderItemSet = new HashSet<>();
        for (CartItem cartItem : shoppingCartByUser.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice());
            orderItemSet.add(orderItem);
        }
        order.setOrderItems(orderItemSet);
        order.setTotal(orderItemSet
                .stream()
                .map(orderItem ->
                        orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        orderRepository.save(order);
        return orderMapper.orderToDto(order);
    }

    @Override
    public List<OrderItemDto> findOrderItemsByOrderId(String emailUser, Long id) {
        Order order = orderRepository.findOrderByUserEmailAndId(emailUser, id);
        return order.getOrderItems()
                .stream()
                .map(orderMapper::orderItemToDto)
                .toList();
    }

    private ShoppingCart findShoppingCartByUser(String emailUser) {
        return shoppingCartRepository.findByUserEmail(emailUser);
    }
}
