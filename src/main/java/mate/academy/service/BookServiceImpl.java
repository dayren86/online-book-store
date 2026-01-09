package mate.academy.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.BookDto;
import mate.academy.dto.CreateBookRequestDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.BookMapper;
import mate.academy.model.Book;
import mate.academy.repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Override
    public Book save(CreateBookRequestDto createBookRequestDto) {
        Book bookModel = bookMapper.toBookModel(createBookRequestDto);
        return bookRepository.save(bookModel);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.getAll().stream().map(book -> bookMapper.toDto(book)).toList();
    }

    @Override
    public BookDto getBookById(Long id) {
        Book bookById = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book by id"));
        return bookMapper.toDto(bookById);
    }
}
