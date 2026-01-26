package mate.academy.service;

import lombok.RequiredArgsConstructor;
import mate.academy.dto.user.UserRegistrationRequestDto;
import mate.academy.dto.user.UserResponseDto;
import mate.academy.mapper.UserMapper;
import mate.academy.model.User;
import mate.academy.repository.AuthenticationRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationRepository authenticationRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto registrationDto) {
        User userModel = userMapper.toUserModel(registrationDto);
        authenticationRepository.save(userModel);
        return userMapper.toDto(userModel);
    }
}
