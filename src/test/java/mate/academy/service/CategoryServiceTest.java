package mate.academy.service;

import mate.academy.TestDataHelper;
import mate.academy.dto.category.CategoryDto;
import mate.academy.dto.category.CreateCategoryDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.CategoryMapper;
import mate.academy.model.Category;
import mate.academy.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    private static final Long CATEGORY_ID = 2L;
    private static final Long NON_EXISTING_CATEGORY_ID = 100L;

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryDto categoryDto;
    private CreateCategoryDto createCategoryDto;

    @BeforeEach
    void setUp() {
        category = TestDataHelper.getCategory();
        categoryDto = TestDataHelper.getCategoryDto();
        createCategoryDto = TestDataHelper.getCreateCategoryDto();
    }

    @Test
    @DisplayName("Verify findAll() method works")
    public void findAllCategory_ValidPageable_ReturnAllCategories() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Category> categories = List.of(category);
        Page<Category> categoryPage = new PageImpl<>(categories, pageable, categories.size());

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        Page<CategoryDto> categoryDtoPage = categoryService.getAllCategory(pageable);

        assertThat(categoryDtoPage).hasSize(1);
        assertThat(categoryDtoPage.toList().get(0)).isEqualTo(categoryDto);

        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Verify save() method works")
    public void saveCategory_ValidCreateCategoryRequestDto_ReturnCategory() {
        Category newCategory = new Category();
        newCategory.setName(createCategoryDto.getName());
        newCategory.setDescription(createCategoryDto.getDescription());

        when(categoryMapper.toEntity(createCategoryDto)).thenReturn(newCategory);
        when(categoryRepository.save(newCategory)).thenReturn(newCategory);

        Category saveCategory = categoryService.save(createCategoryDto);

        assertEquals(createCategoryDto.getName(), saveCategory.getName());
        assertEquals(createCategoryDto.getDescription(), saveCategory.getDescription());

        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Verify getCategoryById() method exception")
    public void getCategoryById_NonExistingCategoryId_ShouldException() {
        when(categoryRepository.findById(NON_EXISTING_CATEGORY_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> categoryService.getCategoryById(NON_EXISTING_CATEGORY_ID));

        String expected = "Can't find category by id: " + NON_EXISTING_CATEGORY_ID;

        assertEquals(expected, exception.getMessage());

        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("Verify getCategoryById() method work")
    public void getCategoryById_ExistingCategoryId_returnCategoryDto() {
        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.ofNullable(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto categoryById = categoryService.getCategoryById(CATEGORY_ID);

        assertEquals(category.getName(), categoryById.getName());
        assertEquals(category.getDescription(), categoryById.getDescription());

        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Verify updateCategory() method work")
    public void updateCategory_ExistingCategoryId_returnUpdateCategoryDto() {
        CategoryDto expectedUpdateCategory = new CategoryDto();
        expectedUpdateCategory.setId(CATEGORY_ID);
        expectedUpdateCategory.setName(createCategoryDto.getName());
        expectedUpdateCategory.setDescription(createCategoryDto.getDescription());

        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.ofNullable(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expectedUpdateCategory);

        CategoryDto updateCategory = categoryService.updateCategory(CATEGORY_ID, createCategoryDto);

        assertEquals(expectedUpdateCategory.getId(), updateCategory.getId());
        assertEquals(expectedUpdateCategory.getName(), updateCategory.getName());
        assertEquals(expectedUpdateCategory.getDescription(), updateCategory.getDescription());

        verify(categoryRepository, times(1)).findById(anyLong());
        verify(categoryRepository, times(1)).save(category);
        verify(categoryMapper, times(1)).toDto(category);
        verifyNoMoreInteractions(categoryRepository);
    }
}
