package mate.academy.mapper;

import mate.academy.config.MapperConfig;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.BookDtoWithCategory;
import mate.academy.dto.book.CreateBookRequestDto;
import mate.academy.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toBookModel(CreateBookRequestDto createBookRequestDto);

    void updateBook(@MappingTarget Book book, CreateBookRequestDto updateBookDto);

    BookDtoWithCategory toDtoWithCategories(Book book);
}
