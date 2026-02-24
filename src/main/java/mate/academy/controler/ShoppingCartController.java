package mate.academy.controler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.cart.AddCartItemDto;
import mate.academy.dto.cart.CartItemUpdateQuantityDto;
import mate.academy.dto.cart.ShoppingCartDto;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart api")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Operation(summary = "Get shopping cart for login user")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    public ShoppingCartDto getUserShoppingCart(@AuthenticationPrincipal User user) {
        return shoppingCartService.getShoppingCat(user.getEmail());
    }

    @Operation(summary = "Add cart item with id book and quantity")
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    public void addBookToShoppingCart(@AuthenticationPrincipal User user,
                                      @Valid @RequestBody AddCartItemDto addCartItemDto) {
        shoppingCartService.addBookToShoppingCart(user.getEmail(), addCartItemDto);
    }

    @Operation(summary = "Update quantity book")
    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/items/{cartItemsId}")
    public ShoppingCartDto update(@AuthenticationPrincipal User user,
                                  @PathVariable Long cartItemsId,
                                  @RequestBody CartItemUpdateQuantityDto updateQuantity) {
        return shoppingCartService
                .updateQuantity(user.getEmail(), cartItemsId, updateQuantity.getQuantity());
    }

    @Operation(summary = "Delete cart item")
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/items/{cartItemsId}")
    public void deleteCartItem(@AuthenticationPrincipal User user,
                               @PathVariable Long cartItemsId) {
        shoppingCartService.deleteCartItem(user.getEmail(), cartItemsId);
    }
}
