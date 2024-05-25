package engine.dto;

import lombok.*;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDto {

    @Pattern(regexp = "\\w+[.\\w+]*@\\w+\\.\\w+", message = "Email should be valid")
    String email;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    String password;

}
