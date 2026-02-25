package mate.academy.service;

import lombok.RequiredArgsConstructor;
import mate.academy.model.CartItem;
import mate.academy.model.Order;
import mate.academy.model.OrderItem;
import mate.academy.model.ShoppingCart;
import mate.academy.repository.OrderRepository;
import mate.academy.repository.ShoppingCartRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {
    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public Order getOrderByUser(String emailUser) {
        return orderRepository.findByUserEmail(emailUser);
    }

    @Override
    public void addOrderFromShoppingCart(String emailUser, String shippingAddress) {
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
        order.setTotal(BigDecimal.valueOf(orderItemSet
                .stream()
                .mapToDouble(orderItem -> orderItem.getId() * orderItem.getQuantity())
                .sum()));

        orderRepository.save(order);
    }

    private ShoppingCart findShoppingCartByUser(String emailUser) {
        return shoppingCartRepository.findByUserEmail(emailUser);
    }
}
