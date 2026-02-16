package mate.academy.service;

import lombok.RequiredArgsConstructor;
import mate.academy.dto.category.CategoryDto;
import mate.academy.dto.category.CreateCategoryDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.CategoryMapper;
import mate.academy.model.Category;
import mate.academy.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Category save(CreateCategoryDto createCategoryDto) {
        Category category = categoryMapper.toEntity(createCategoryDto);
        return categoryRepository.save(category);
    }

    @Override
    public Page<CategoryDto> getAllCategory(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toDto);
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        return categoryMapper.toDto(findCategory(id));
    }

    @Override
    public CategoryDto updateCategory(Long id, CreateCategoryDto createCategoryDto) {
        Category categoryById = findCategory(id);
        categoryMapper.updateCategory(categoryById, createCategoryDto);

        return categoryMapper.toDto(categoryRepository.save(categoryById));
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }

    private Category findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find category by id: " + id));
    }
}
