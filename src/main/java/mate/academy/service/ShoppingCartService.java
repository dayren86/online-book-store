package mate.academy.service;

import mate.academy.dto.cart.AddCartItemDto;
import mate.academy.dto.cart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCat(String emailUser);

    void addBookToShoppingCart(String emailUser, AddCartItemDto addCartItemDto);

    ShoppingCartDto updateQuantity(String emailUser, Long id, int quantity);

    void deleteCartItem(String emailUser, Long id);
}
