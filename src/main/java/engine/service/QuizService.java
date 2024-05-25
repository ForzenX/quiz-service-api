package engine.service;

import engine.dto.CreateQuizDto;
import engine.dto.QuizInfoDto;
import engine.dto.SolveQuizDto;
import engine.dto.SolveQuizInfoDto;
import engine.entity.CompletedQuiz;
import engine.entity.QuizEntity;
import engine.entity.UserEntity;
import engine.mapper.QuizMapper;
import engine.repository.CompletedQuizRepository;
import engine.repository.QuizRepository;
import engine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;

    private final CompletedQuizRepository completedQuizRepository;

    private final UserRepository userRepository;

    private final QuizMapper quizMapper;

    public ResponseEntity<QuizInfoDto> getQuiz(int id) {
        QuizEntity quizEntity = quizRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz with this id doesn't exist"));

        return ResponseEntity.ok(quizMapper.mapEntityToDto(quizEntity));
    }

    public ResponseEntity<Page<QuizEntity>> getAllQuizzes(int page) {
        Page<QuizEntity> quizzesPage = quizRepository.findAll(PageRequest.of(page, 10));

        return new ResponseEntity<>(quizzesPage, HttpStatus.OK);
    }

    public ResponseEntity<Page<CompletedQuiz>> getCompletedQuizzes(int page, UserDetails userDetails) {
        String email = userDetails.getUsername();
        Pageable pageable = PageRequest.of(page, 10);

        Page<CompletedQuiz> completedQuizzesPage = completedQuizRepository.findAllByUserEmailOrderByCompletedAtDesc(email, pageable);

        return ResponseEntity.ok(completedQuizzesPage);
    }

    public ResponseEntity<SolveQuizInfoDto> solveQuiz(int id, SolveQuizDto request, UserDetails userDetails) {
        QuizEntity quizEntity = quizRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found"));

        boolean success = checkAnswerEquality(request.getAnswer(), quizEntity.getAnswer());

        SolveQuizInfoDto response = SolveQuizInfoDto.builder()
                .success(success)
                .feedback(success ? "Congratulations, you're right!" : "Wrong answer! Please, try again.")
                .build();

        if (success) {
            CompletedQuiz completedQuiz = CompletedQuiz.builder()
                    .id(id)
                    .userEmail(userDetails.getUsername())
                    .completedAt(LocalDateTime.now())
                    .build();
            completedQuizRepository.save(completedQuiz);
        }

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<QuizInfoDto> createQuiz(CreateQuizDto request, UserDetails userDetails) {
        UserEntity userEntity = userRepository.findUserByEmail(userDetails.getUsername())
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.FORBIDDEN, "User not found"));

        QuizEntity quizEntity = quizMapper.mapDtoToEntity(request, userEntity);

        quizRepository.save(quizEntity);

        return ResponseEntity.ok(quizMapper.mapEntityToDto(quizEntity));
    }

    public ResponseEntity<?> deleteQuiz(int id, UserDetails userDetails) {
        UserEntity userEntity = userRepository.findUserByEmail(userDetails.getUsername())
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.FORBIDDEN, "User not found"));

        QuizEntity quizEntity = quizRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found"));

        if (!Objects.equals(userEntity, quizEntity.getAuthor())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can not delete this quiz!");
        }

        quizRepository.delete(quizEntity);
        return ResponseEntity.noContent().build();
    }

    private boolean checkAnswerEquality(List<Integer> userAnswer, List<Integer> correctAnswer) {
        // Check if both lists are null or empty
        if ((userAnswer == null || userAnswer.isEmpty()) && (correctAnswer == null || correctAnswer.isEmpty())) {
            return true; // Both lists are null or empty, consider them equal
        }

        // If only one of the lists is null or empty, consider them not equal
        if (userAnswer == null || correctAnswer == null || userAnswer.isEmpty() || correctAnswer.isEmpty()) {
            return false;
        }

        // Compare the elements of the lists
        if (userAnswer.size() != correctAnswer.size()) {
            return false;
        }

        for (int i = 0; i < userAnswer.size(); i++) {
            if (!userAnswer.get(i).equals(correctAnswer.get(i))) {
                return false;
            }
        }

        return true;
    }
}
