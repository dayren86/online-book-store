package mate.academy.service;

import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.BookDtoWithoutCategory;
import mate.academy.dto.book.CreateBookRequestDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.BookMapper;
import mate.academy.model.Book;
import mate.academy.repository.BookRepository;
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
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    private static final Long BOOK_ID = 1L;
    private static final Long NON_EXISTING_BOOK_ID = 100L;

    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private BookDto bookDto;
    private CreateBookRequestDto createBookRequestDto;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setTitle("1984");
        book.setAuthor("George Orwell");
        book.setIsbn("9783844908213");
        book.setPrice(BigDecimal.valueOf(15.50));

        bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());

        createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setTitle("The Lord of the Rings");
        createBookRequestDto.setAuthor("JGeorge R.R. Martin");
        createBookRequestDto.setIsbn("9788845292613");
        createBookRequestDto.setPrice(BigDecimal.valueOf(20.00));
    }

    @Test
    @DisplayName("Verify findAll() method works")
    public void findAllBook_ValidPageable_ReturnAllBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(book);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        Page<BookDto> bookDtoList = bookService.findAll(pageable);

        assertThat(bookDtoList).hasSize(1);
        assertThat(bookDtoList.toList().get(0)).isEqualTo(bookDto);

        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify save() method works")
    public void saveBook_ValidCreateBookRequestDto_ReturnBook() {
        Book newBook = new Book();
        newBook.setTitle(createBookRequestDto.getTitle());
        newBook.setAuthor(createBookRequestDto.getAuthor());
        newBook.setIsbn(createBookRequestDto.getIsbn());
        newBook.setPrice(createBookRequestDto.getPrice());

        when(bookMapper.toBookModel(createBookRequestDto)).thenReturn(newBook);
        when(bookRepository.save(newBook)).thenReturn(newBook);

        Book saveBook = bookService.save(createBookRequestDto);

        assertEquals(createBookRequestDto.getTitle(), saveBook.getTitle());
        assertEquals(createBookRequestDto.getAuthor(), saveBook.getAuthor());
        assertEquals(createBookRequestDto.getIsbn(), saveBook.getIsbn());
        assertEquals(createBookRequestDto.getPrice(), saveBook.getPrice());

        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify getBookById() method exception")
    public void getBookById_NonExistingUserId_ShouldException() {

        when(bookRepository.findById(NON_EXISTING_BOOK_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> bookService.getBookById(NON_EXISTING_BOOK_ID));

        String expected = "Can't find book by id: " + NON_EXISTING_BOOK_ID;

        assertEquals(expected, exception.getMessage());

        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Verify getBookById() method work")
    public void getBookById_ExistingUserId_returnBookDto() {
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.ofNullable(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto bookById = bookService.getBookById(BOOK_ID);

        assertEquals(book.getAuthor(), bookById.getAuthor());
        assertEquals(book.getTitle(), bookById.getTitle());
        assertEquals(book.getIsbn(), bookById.getIsbn());
        assertEquals(book.getPrice(), bookById.getPrice());

        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify updateBook() method work")
    public void updateBook_ExistingUserid_returnUpdateBookDto() {
        BookDto expectedUpdateBook = new BookDto();
        expectedUpdateBook.setId(BOOK_ID);
        expectedUpdateBook.setAuthor(createBookRequestDto.getAuthor());
        expectedUpdateBook.setTitle(createBookRequestDto.getTitle());
        expectedUpdateBook.setIsbn(createBookRequestDto.getIsbn());
        expectedUpdateBook.setPrice(createBookRequestDto.getPrice());

        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.ofNullable(book));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expectedUpdateBook);

        BookDto upadteBookDto = bookService.updateBook(BOOK_ID, createBookRequestDto);

        assertEquals(expectedUpdateBook.getId(), upadteBookDto.getId());
        assertEquals(expectedUpdateBook.getTitle(), upadteBookDto.getTitle());
        assertEquals(expectedUpdateBook.getIsbn(), upadteBookDto.getIsbn());
        assertEquals(expectedUpdateBook.getPrice(), upadteBookDto.getPrice());

        verify(bookRepository, times(1)).findById(anyLong());
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).updateBook(book, createBookRequestDto);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify findAllByCategoryId() method work")
    public void findAllByCategoryId_ExistingIdCategory_ReturnListBookDto() {
        Long categoryId = 1L;

        List<Book> bookList = List.of(book);
        BookDtoWithoutCategory bookDtoWithoutCategory = new BookDtoWithoutCategory();
        bookDtoWithoutCategory.setTitle(book.getTitle());
        bookDtoWithoutCategory.setAuthor(book.getAuthor());
        List<BookDtoWithoutCategory> bookDtoList = List.of(bookDtoWithoutCategory);

        when(bookRepository.findAllByCategories_Id(categoryId)).thenReturn(bookList);
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(bookDtoWithoutCategory);

        List<BookDtoWithoutCategory> allByCategoryId = bookService.findAllByCategoryId(categoryId);

        assertEquals(bookDtoList.size(), allByCategoryId.size());

        verifyNoMoreInteractions(bookRepository, bookMapper);
    }
}
