package mate.academy.service;

import java.util.List;
import mate.academy.dto.BookDto;
import mate.academy.dto.CreateBookRequestDto;
import mate.academy.dto.UpdateBookDto;
import mate.academy.model.Book;

public interface BookService {
    Book save(CreateBookRequestDto createBookRequestDto);

    List<BookDto> findAll();

    BookDto getBookById(Long id);

    BookDto updateBook(Long id, UpdateBookDto updateBookDto);

    void deleteBookById(Long id);
}
