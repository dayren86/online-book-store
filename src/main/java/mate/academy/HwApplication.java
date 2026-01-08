package mate.academy;

import java.math.BigDecimal;
import mate.academy.model.Book;
import mate.academy.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HwApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(HwApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Book book = new Book();
                book.setTitle("Game of Thrones");
                book.setAuthor("George Martin");
                book.setIsbn("978-0-00-820910-0");
                book.setPrice(BigDecimal.valueOf(100));

                Book book2 = new Book();
                book2.setTitle("Lord of the Rings");
                book2.setAuthor("J. R. R. Tolkien");
                book2.setIsbn("978-0544003415");
                book2.setPrice(BigDecimal.valueOf(150));

                bookService.save(book);
                bookService.save(book2);

                System.out.println(bookService.findAll());
            }
        };
    }

}
