package mate.academy.dto.cart;

import lombok.Getter;
import lombok.Setter;
import mate.academy.dto.book.BookDtoWithoutCategory;

@Getter
@Setter
public class CartItemDto {
    private Long id;
    private BookDtoWithoutCategory book;
    private int quantity;
}
