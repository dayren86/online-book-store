package mate.academy.service;

import lombok.RequiredArgsConstructor;
import mate.academy.dto.user.UserRegistrationRequestDto;
import mate.academy.dto.user.UserResponseDto;
import mate.academy.exception.RegistrationException;
import mate.academy.mapper.UserMapper;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.repository.ShoppingCartRepository;
import mate.academy.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto registrationDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new RegistrationException("Email already exist: " + registrationDto.getEmail());
        }
        User userModel = userMapper.toUserModel(registrationDto);
        userRepository.save(userModel);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(userModel);
        shoppingCartRepository.save(shoppingCart);
        return userMapper.toDto(userModel);
    }
}
