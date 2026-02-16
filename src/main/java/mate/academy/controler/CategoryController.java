package mate.academy.controler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.book.BookDtoWithoutCategory;
import mate.academy.dto.category.CategoryDto;
import mate.academy.dto.category.CreateCategoryDto;
import mate.academy.model.Category;
import mate.academy.service.BookService;
import mate.academy.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category api")
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @Operation(summary = "Create category")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category createCategory(@Valid @RequestBody CreateCategoryDto createCategoryDto) {
        return categoryService.save(createCategoryDto);
    }

    @Operation(summary = "Get all categories")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    public Page<CategoryDto> getAll(Pageable pageable) {
        return categoryService.getAllCategory(pageable);
    }

    @Operation(summary = "Get category by id")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @Operation(summary = "Update category")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public CategoryDto updateCategory(@PathVariable Long id,
                                      @Valid @RequestBody CreateCategoryDto createCategoryDto) {
        return categoryService.updateCategory(id,createCategoryDto);
    }

    @Operation(summary = "Delete category by id")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
    }

    @Operation(summary = "Get all books by category")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{id}/books")
    public List<BookDtoWithoutCategory> getBooksByCategoryId(@PathVariable Long id) {
        return bookService.findAllByCategoryId(id);
    }
}
