package mate.academy;

import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.CreateBookRequestDto;
import mate.academy.dto.category.CategoryDto;
import mate.academy.dto.category.CreateCategoryDto;
import mate.academy.model.Book;
import mate.academy.model.Category;

import java.math.BigDecimal;

public class TestDataHelper {

    public static Book getBook() {
        Book book = new Book();
        book.setTitle("1984");
        book.setAuthor("George Orwell");
        book.setIsbn("9783844908213");
        book.setPrice(BigDecimal.valueOf(15.50));
        return book;
    }

    public static Book getBookSecond() {
        Book book = new Book();
        book.setTitle("The Lord of the Rings");
        book.setAuthor("JGeorge R.R. Martin");
        book.setIsbn("9788845292613");
        book.setPrice(BigDecimal.valueOf(20));
        return book;
    }

    public static BookDto getBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setTitle("1984");
        bookDto.setAuthor("George Orwell");
        bookDto.setIsbn("9783844908213");
        bookDto.setPrice(BigDecimal.valueOf(15.50));
        return bookDto;
    }

    public static CreateBookRequestDto getCreateBookDto() {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setTitle("The Lord of the Rings");
        createBookRequestDto.setAuthor("JGeorge R.R. Martin");
        createBookRequestDto.setIsbn("9788845292613");
        createBookRequestDto.setPrice(BigDecimal.valueOf(20.00));
        return createBookRequestDto;
    }

    public static Category getCategory() {
        Category category = new Category();
        category.setName("Fantasy");
        category.setDescription("Fantasy books");
        return category;
    }

    public static CategoryDto getCategoryDto() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Fantasy");
        categoryDto.setDescription("Fantasy books");
        return categoryDto;
    }

    public static CreateCategoryDto getCreateCategoryDto() {
        CreateCategoryDto createCategoryDto = new CreateCategoryDto();
        createCategoryDto.setName("Detective");
        createCategoryDto.setDescription("Detective book");
        return createCategoryDto;
    }
}
