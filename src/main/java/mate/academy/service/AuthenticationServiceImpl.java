package mate.academy.service;

import lombok.RequiredArgsConstructor;
import mate.academy.dto.user.UserLoginRequestDto;
import mate.academy.dto.user.UserLoginResponseDto;
import mate.academy.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserLoginResponseDto authenticate(UserLoginRequestDto requestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getEmail(), requestDto.getPassword())
        );

        String token = jwtUtil.generateToken(requestDto.getEmail());
        return new UserLoginResponseDto(token);
    }
}
