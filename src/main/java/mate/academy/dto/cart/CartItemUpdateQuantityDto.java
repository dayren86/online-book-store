package mate.academy.dto.cart;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemUpdateQuantityDto {
    @NotNull
    private int quantity;
}
