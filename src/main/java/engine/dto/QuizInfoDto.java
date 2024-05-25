package engine.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizInfoDto {

    private int id;

    private String title;

    private String text;

    private List<String> options;

}
