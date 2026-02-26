package mate.academy.mapper;

import mate.academy.config.MapperConfig;
import mate.academy.dto.cart.CartItemDto;
import mate.academy.model.CartItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    CartItemDto toDto(CartItem cartItem);

    @AfterMapping
    default void setBookId(@MappingTarget CartItemDto cartItemDto, CartItem cartItem) {
        cartItemDto.setBookId(cartItem.getBook().getId());
        cartItemDto.setBookTitle(cartItem.getBook().getTitle());
    }
}
