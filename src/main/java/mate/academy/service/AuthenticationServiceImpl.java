package mate.academy.service;

import lombok.RequiredArgsConstructor;
import mate.academy.dto.user.UserLoginRequestDto;
import mate.academy.dto.user.UserLoginResponseDto;
import mate.academy.dto.user.UserRegistrationRequestDto;
import mate.academy.dto.user.UserResponseDto;
import mate.academy.exception.RegistrationException;
import mate.academy.mapper.UserMapper;
import mate.academy.model.User;
import mate.academy.repository.UserRepository;
import mate.academy.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto registrationDto)
            throws RegistrationException {
        System.out.println(userRepository.existsByEmail(registrationDto.getEmail()));

        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new RegistrationException("Email already exist: " + registrationDto.getEmail());
        }
        User userModel = userMapper.toUserModel(registrationDto);
        userRepository.save(userModel);
        return userMapper.toDto(userModel);
    }

    @Override
    public UserLoginResponseDto authenticate(UserLoginRequestDto requestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword())
        );

        String token = jwtUtil.generateToken(requestDto.getEmail());
        return new UserLoginResponseDto(token);
    }
}
