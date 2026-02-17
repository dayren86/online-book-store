package mate.academy.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCategoryDto {
    @NotBlank
    @Size(min = 5, max = 200)
    private String name;
    @Size(min = 5, max = 200)
    private String description;
}
