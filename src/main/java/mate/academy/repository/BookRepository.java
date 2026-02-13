package mate.academy.repository;

import java.util.List;
import mate.academy.model.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @EntityGraph(attributePaths = {"categories"})
    List<Book> findAllByCategories_Id(Long categoryId);
}
