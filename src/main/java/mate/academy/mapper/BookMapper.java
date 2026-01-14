package mate.academy.mapper;

import mate.academy.config.MapperConfig;
import mate.academy.dto.BookDto;
import mate.academy.dto.CreateBookRequestDto;
import mate.academy.dto.UpdateBookDto;
import mate.academy.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toBookModel(CreateBookRequestDto createBookRequestDto);

    Book updateBook(@MappingTarget Book book, UpdateBookDto updateBookDto);
}
