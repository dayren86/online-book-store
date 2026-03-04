package mate.academy.service;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.order.OrderDTo;
import mate.academy.dto.order.OrderItemDto;
import mate.academy.dto.order.UpdateStatusDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.OrderMapper;
import mate.academy.model.CartItem;
import mate.academy.model.Order;
import mate.academy.model.OrderItem;
import mate.academy.model.ShoppingCart;
import mate.academy.repository.OrderRepository;
import mate.academy.repository.ShoppingCartRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {
    private final OrderRepository orderRepository;
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

        if (shoppingCartByUser.getCartItems().isEmpty()) {
            throw new EntityNotFoundException("Shopping cart is empty for user: " + emailUser);
        }

        Order order = orderMapper.toOrderModel(shoppingCartByUser.getUser(), shippingAddress);

        Set<OrderItem> orderItemSet = new HashSet<>();
        for (CartItem cartItem : shoppingCartByUser.getCartItems()) {
            orderItemSet.add(orderMapper.cartItemToOrderItem(cartItem, order));
        }
        order.setOrderItems(orderItemSet);

        order.setTotal(setTotalPrice(orderItemSet));

        orderRepository.save(order);
        return orderMapper.orderToDto(order);
    }

    @Override
    public void updateStatus(Long id, UpdateStatusDto status) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cant find order by id: " + id));
        order.setStatus(status.getStatus());
        orderRepository.save(order);
    }

    @Override
    public List<OrderItemDto> findOrderItemsByOrderId(String emailUser, Long orderId) {
        Order order = findOrderByIdAndUserEmail(emailUser, orderId);
        return order.getOrderItems()
                .stream()
                .map(orderMapper::orderItemToDto)
                .toList();
    }

    @Override
    public OrderItemDto getSpecificItem(String emailUser, Long orderId, Long id) {
        Order order = findOrderByIdAndUserEmail(emailUser, orderId);

        OrderItem findOrderItem = order.getOrderItems().stream()
                .filter(orderItem -> orderItem.getId().equals(id))
                .findFirst()
                .orElseThrow(
                        () -> new EntityNotFoundException("Cant find order item by id: " + id));
        return orderMapper.orderItemToDto(findOrderItem);
    }

    private ShoppingCart findShoppingCartByUser(String emailUser) {
        return shoppingCartRepository.findByUserEmail(emailUser);
    }

    private Order findOrderByIdAndUserEmail(String emailUser, Long orderId) {
        return orderRepository.findOrderByUserEmailAndId(emailUser, orderId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find order by id: " + orderId));
    }

    private BigDecimal setTotalPrice(Set<OrderItem> orderItemSet) {
        return orderItemSet
                .stream()
                .map(orderItem ->
                        orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
