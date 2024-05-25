package engine.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateQuizDto {

    @NotBlank(message = "title of quiz is required")
    @Pattern(regexp = ".*\\D.*", message = "Title must contain at least one non-digit character")
    private String title;

    @NotBlank(message = "text of quiz is required")
    @Pattern(regexp = ".*\\D.*", message = "Text must contain at least one non-digit character")
    private String text;

    @NotNull(message = "options is required")
    @Size(min = 2)
    private List<String> options;

    private List<Integer> answer;

}
