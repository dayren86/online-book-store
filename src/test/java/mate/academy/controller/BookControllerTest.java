package mate.academy.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.CreateBookRequestDto;
import mate.academy.model.Book;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.math.BigDecimal;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    private static final int BOOK_ID = 2;
    private static final int INCORRECT_BOOK_ID = 4;

    protected static MockMvc mockMvc;

    private Book book;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setTitle("The Lord of the Rings");
        book.setAuthor("JGeorge R.R. Martin");
        book.setIsbn("9788845292613");
        book.setPrice(BigDecimal.valueOf(20));
    }

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Successfully created a book and received book with id in the response")
    @WithMockUser(authorities = {"ADMIN"})
    @Sql(scripts = {
            "classpath:db/book/remove_book_after_create.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_CreateBookRequestDto_ReturnSaveBook() throws Exception {
        CreateBookRequestDto bookDto = new CreateBookRequestDto();
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn("9788845292611");
        bookDto.setPrice(book.getPrice());

        String jsonCreateBookDto = objectMapper.writeValueAsString(bookDto);

        MvcResult mvcResult = mockMvc.perform(
                post("/books")
                        .content(jsonCreateBookDto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        Book bookActual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Book.class);

        assertEquals(bookDto.getTitle(), bookActual.getTitle());
        assertEquals(bookDto.getAuthor(), bookActual.getAuthor());
        assertEquals(bookDto.getIsbn(), bookActual.getIsbn());
        assertEquals(0, bookDto.getPrice().compareTo(bookActual.getPrice()));
    }

    @Test
    @DisplayName("Get all books from the database")
    @WithMockUser(authorities = {"USER"})
    void getAllBook_GetAllBookFromDB_ReturnListBookDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get("/books")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString());

        List<BookDto> bookDtos = objectMapper.readValue(jsonNode.get("content").toString(), new TypeReference<>() {
        });

        assertEquals(2, bookDtos.size());
    }
    @Test
    @WithMockUser(authorities = "USER")
    @DisplayName("Get book by id if the id is correct,Book should be returned")
    void getBookById_CorrectId_ReturnBook() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get("/books/" + BOOK_ID)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto bookActual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);

        assertEquals(book.getTitle(), bookActual.getTitle());
        assertEquals(book.getAuthor(), bookActual.getAuthor());
        assertEquals(book.getIsbn(), bookActual.getIsbn());
        assertEquals(0, book.getPrice().compareTo(bookActual.getPrice()));
    }

    @Test
    @WithMockUser(authorities = "USER")
    @DisplayName("Get book by id if the id is incorrect, should be returned exception message")
    void getBookById_InCorrectId_ReturnExceptionMessage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get("/books/" + INCORRECT_BOOK_ID)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String exception = mvcResult.getResponse().getContentAsString();

        assertEquals("Can't find book by id: " + INCORRECT_BOOK_ID, exception);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Book update if the id is correct, update BookDto should be returned")
    @Sql(scripts = {
            "classpath:db/book/return_book_after_update.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateBook_CorrectId_ReturnUpdateBookDto() throws Exception {
        CreateBookRequestDto bookDto = new CreateBookRequestDto();
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn("9782845492616");
        bookDto.setPrice(book.getPrice());

        String jsonCreateBookDto = objectMapper.writeValueAsString(bookDto);

        MvcResult mvcResult = mockMvc.perform(
                        put("/books/" + BOOK_ID)
                                .content(jsonCreateBookDto)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actualBookDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);

        assertEquals(actualBookDto.getTitle(), bookDto.getTitle());
        assertEquals(actualBookDto.getAuthor(), bookDto.getAuthor());
        assertEquals(actualBookDto.getIsbn(), bookDto.getIsbn());
        assertEquals(0, actualBookDto.getPrice().compareTo(bookDto.getPrice()));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Book update if the id is incorrect, should be returned exception message")
    void updateBook_InCorrectId_ReturnExceptionMessage() throws Exception {
        CreateBookRequestDto bookDto = new CreateBookRequestDto();
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn("9782845492616");
        bookDto.setPrice(book.getPrice());

        String jsonCreateBookDto = objectMapper.writeValueAsString(bookDto);

        MvcResult mvcResult = mockMvc.perform(
                        put("/books/" + INCORRECT_BOOK_ID)
                                .content(jsonCreateBookDto)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String exception = mvcResult.getResponse().getContentAsString();

        assertEquals("Can't find book by id: " + INCORRECT_BOOK_ID, exception);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Deleting a book if the id is correct, returns status 204 No Content")
    @Sql(scripts = {
            "classpath:db/book/return_book_after_delete.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteBook_CorrectId_Status204() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        delete("/books/" + BOOK_ID)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }
}

