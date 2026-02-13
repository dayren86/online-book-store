package mate.academy.dto.book;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import mate.academy.dto.category.CategoryDto;

@Getter
@Setter
public class BookDtoWithCategory {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private String description;
    private String coverImage;
    private Set<CategoryDto> categories = new HashSet<>();
}
