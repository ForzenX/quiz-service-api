package engine.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SolveQuizDto {

    private List<Integer> answer;

}
