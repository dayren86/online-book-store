package mate.academy.repository;

import java.util.List;
import java.util.Optional;

import mate.academy.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> getAll();

    Optional<Book> findById(Long id);
}
