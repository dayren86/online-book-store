package mate.academy.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.dto.category.CategoryDto;
import mate.academy.dto.category.CreateCategoryDto;
import mate.academy.model.Category;
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
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    private static final int CATEGORY_ID = 2;
    private static final int INCORRECT_CATEGORY_ID = 4;

    protected static MockMvc mockMvc;

    private Category category;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setName("Fantasy");
        category.setDescription("Fantasy books");
    }

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    @DisplayName("Successfully created a category and received category with id in the response")
    @Sql(scripts = {
            "classpath:db/category/remove_category_after_create.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_CreateCategoryDto_ReturnSaveCategory() throws Exception {
        CreateCategoryDto createCategoryDto = new CreateCategoryDto();
        createCategoryDto.setName("Detective");
        createCategoryDto.setDescription("Detective book");

        String jsonCreateBookDto = objectMapper.writeValueAsString(createCategoryDto);

        MvcResult mvcResult = mockMvc.perform(
                        post("/categories")
                                .content(jsonCreateBookDto)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        Category categoryActual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Category.class);

        assertEquals(createCategoryDto.getName(), categoryActual.getName());
        assertEquals(createCategoryDto.getDescription(), categoryActual.getDescription());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    @DisplayName("Get all categories from the database")
    void getAllCategory_ReturnListCategoryDto() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get("/categories")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString());

        List<CategoryDto> actualCategoryDtoList = objectMapper.readValue(jsonNode.get("content").toString(), new TypeReference<>() {
        });

        assertEquals(2, actualCategoryDtoList.size());
    }
    @Test
    @WithMockUser(authorities = "USER")
    @DisplayName("Get category by id if the id is correct,Category should be returned")
    void getCategoryById_CorrectId_ReturnBook() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get("/categories/" + CATEGORY_ID)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actualCategoryDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CategoryDto.class);

        assertEquals(category.getName(), actualCategoryDto.getName());
        assertEquals(category.getDescription(), actualCategoryDto.getDescription());
    }

    @Test
    @WithMockUser(authorities = "USER")
    @DisplayName("Get category by id if the id is incorrect, should be returned exception message")
    void getCategoryById_InCorrectId_ReturnExceptionMessage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get("/categories/" + INCORRECT_CATEGORY_ID)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String exception = mvcResult.getResponse().getContentAsString();

        assertEquals("Can't find category by id: " + INCORRECT_CATEGORY_ID, exception);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Category update if the id is correct, update CategoryDto should be returned")
    @Sql(scripts = {
            "classpath:db/category/return_category_after_update.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateCategory_CorrectId_ReturnUpdateCategoryDto() throws Exception {
        CreateCategoryDto createCategoryDto = new CreateCategoryDto();
        createCategoryDto.setName("Detective");

        String jsonCreateBookDto = objectMapper.writeValueAsString(createCategoryDto);

        MvcResult mvcResult = mockMvc.perform(
                        put("/categories/" + CATEGORY_ID)
                                .content(jsonCreateBookDto)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actualCategoryDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CategoryDto.class);

        assertEquals(createCategoryDto.getName(), actualCategoryDto.getName());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Category update if the id is incorrect, should be returned exception message")
    void updateCategory_InCorrectId_ReturnExceptionMessage() throws Exception {
        CreateCategoryDto createCategoryDto = new CreateCategoryDto();
        createCategoryDto.setName("Detective");

        String jsonCreateBookDto = objectMapper.writeValueAsString(createCategoryDto);

        MvcResult mvcResult = mockMvc.perform(
                        put("/categories/" + INCORRECT_CATEGORY_ID)
                                .content(jsonCreateBookDto)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String exception = mvcResult.getResponse().getContentAsString();

        assertEquals("Can't find category by id: " + INCORRECT_CATEGORY_ID, exception);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Deleting a category if the id is correct, returns status 204 No Content")
    @Sql(scripts = {
            "classpath:db/category/return_category_after_delete.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteCategory_CorrectId_Status204() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        delete("/categories/" + CATEGORY_ID)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }
}
