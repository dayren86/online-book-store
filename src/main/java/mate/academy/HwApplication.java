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
                Book bookGameOfThrones = new Book();
                bookGameOfThrones.setTitle("Game of Thrones");
                bookGameOfThrones.setAuthor("George Martin");
                bookGameOfThrones.setIsbn("978-0-00-820910-0");
                bookGameOfThrones.setPrice(BigDecimal.valueOf(100));

                Book bookRings = new Book();
                bookRings.setTitle("Lord of the Rings");
                bookRings.setAuthor("J. R. R. Tolkien");
                bookRings.setIsbn("978-0544003415");
                bookRings.setPrice(BigDecimal.valueOf(150));

                bookService.save(bookGameOfThrones);
                bookService.save(bookRings);

                System.out.println(bookService.findAll());
            }
        };
    }

}
