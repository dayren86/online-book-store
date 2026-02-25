package mate.academy.service;

import mate.academy.model.Order;

public interface OrdersService {
    Order getOrderByUser(String emailUser);

    void addOrderFromShoppingCart(String emailUser, String shippingAddress);
}
