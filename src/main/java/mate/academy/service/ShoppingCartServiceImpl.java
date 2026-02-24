package mate.academy.service;

import lombok.RequiredArgsConstructor;
import mate.academy.dto.cart.AddCartItemDto;
import mate.academy.dto.cart.ShoppingCartDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.ShoppingCartMapper;
import mate.academy.model.Book;
import mate.academy.model.CartItem;
import mate.academy.model.ShoppingCart;
import mate.academy.repository.BookRepository;
import mate.academy.repository.CartItemRepository;
import mate.academy.repository.ShoppingCartRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public ShoppingCartDto getShoppingCat(String emailUser) {
        ShoppingCart shoppingCartByUser = findShoppingCartByUser(emailUser);
        return shoppingCartMapper.toDto(shoppingCartByUser);
    }

    @Override
    public void addBookToShoppingCart(String emailUser, AddCartItemDto addCartItemDto) {
        ShoppingCart shoppingCartByUser = findShoppingCartByUser(emailUser);

        Book book = bookRepository.findById(addCartItemDto.getBookId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id: "
                        + addCartItemDto.getBookId())
        );

        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(shoppingCartByUser);
        cartItem.setBook(book);
        cartItem.setQuantity(addCartItemDto.getQuantity());
        cartItemRepository.save(cartItem);
    }

    @Override
    public ShoppingCartDto updateQuantity(String emailUser, Long id, int quantity) {
        ShoppingCart shoppingCartByUser = findShoppingCartByUser(emailUser);
        CartItem findCartItem = findCartItem(shoppingCartByUser, id);
        findCartItem.setQuantity(quantity);
        cartItemRepository.save(findCartItem);
        return shoppingCartMapper.toDto(shoppingCartByUser);
    }

    @Override
    public void deleteCartItem(String emailUser, Long id) {
        ShoppingCart shoppingCartByUser = findShoppingCartByUser(emailUser);
        CartItem findCartItem = findCartItem(shoppingCartByUser, id);
        cartItemRepository.delete(findCartItem);
    }

    private ShoppingCart findShoppingCartByUser(String emailUser) {
        return shoppingCartRepository.findByUserEmail(emailUser);
    }

    private CartItem findCartItem(ShoppingCart shoppingCart, Long id) {
        return shoppingCart.getCartItems()
                .stream()
                .filter(cartItem -> cartItem.getId().equals(id))
                .findFirst()
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find cart item by id: " + id));
    }
}
