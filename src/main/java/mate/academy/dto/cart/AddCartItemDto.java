package mate.academy.dto.cart;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCartItemDto {
    @NotNull
    private Long bookId;
    @NotNull
    private int quantity;
}
