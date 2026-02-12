package mate.academy.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import mate.academy.validation.Passwords;

@Getter
@Setter
@Passwords
public class UserLoginRequestDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 5, max = 200)
    private String password;
}
