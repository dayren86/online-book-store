package mate.academy.service;

import mate.academy.dto.category.CategoryDto;
import mate.academy.dto.category.CreateCategoryDto;
import mate.academy.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Category save(CreateCategoryDto createCategoryDto);

    Page<CategoryDto> getAllCategory(Pageable pageable);

    CategoryDto getCategoryById(Long id);

    CategoryDto updateCategory(Long id, CreateCategoryDto createCategoryDto);

    void deleteCategoryById(Long id);
}
