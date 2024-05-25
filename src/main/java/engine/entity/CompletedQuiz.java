package engine.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Completed_Quizzes")
public class CompletedQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonIgnore
    private int completedQuizId;

    @Column(name = "quiz_id")
    private int id;

    @Column(name = "user_email")
    @JsonIgnore
    private String userEmail;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

}
