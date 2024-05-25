package engine.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SolveQuizInfoDto {

    private boolean success;

    private String feedback;

}
