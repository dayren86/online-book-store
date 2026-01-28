package mate.academy.dto.book;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBookRequestDto {
    @NotBlank
    @Size(min = 5, max = 200)
    private String title;
    @NotBlank
    @Size(min = 5, max = 200)
    private String author;
    @NotBlank
    @Pattern(regexp = "^[0-9]{10}|[0-9]{13}$")
    private String isbn;
    @NotNull
    @DecimalMin(value = "0.00")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal price;
    private String description;
    private String coverImage;
}
