package mate.academy.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import mate.academy.model.Order;

@Getter
@Setter
public class UpdateStatusDto {
    @NotNull
    private Order.Status status;
}
