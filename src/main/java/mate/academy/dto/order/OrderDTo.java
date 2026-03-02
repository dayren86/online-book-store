package mate.academy.dto.order;

import java.math.BigDecimal;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import mate.academy.model.Order;

@Getter
@Setter
public class OrderDTo {
    private Long id;
    private Long userId;
    private Set<OrderItemDto> orderItemsDto;
    private String orderDate;
    private BigDecimal total;
    private Order.Status status;
}
