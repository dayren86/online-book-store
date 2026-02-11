package mate.academy.service;

import mate.academy.dto.user.UserLoginRequestDto;
import mate.academy.dto.user.UserLoginResponseDto;
import mate.academy.dto.user.UserRegistrationRequestDto;
import mate.academy.dto.user.UserResponseDto;
import mate.academy.exception.RegistrationException;

public interface AuthenticationService {
    UserResponseDto register(UserRegistrationRequestDto registrationDto)
            throws RegistrationException;

    UserLoginResponseDto authenticate(UserLoginRequestDto requestDto);
}
