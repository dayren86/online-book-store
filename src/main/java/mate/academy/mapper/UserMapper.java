package mate.academy.mapper;

import mate.academy.config.MapperConfig;
import mate.academy.dto.user.UserRegistrationRequestDto;
import mate.academy.dto.user.UserResponseDto;
import mate.academy.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(config = MapperConfig.class)
public interface UserMapper {

//    @Mapping(qualifiedByName = "encode", target = "password")
    User toUserModel(UserRegistrationRequestDto registration);

//    @Named("encode")
//    default String encodePassword(String password) {
//        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        return encoder.encode(password);
//    }

    UserResponseDto toDto(User user);
}
