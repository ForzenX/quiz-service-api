package engine.controller;

import engine.dto.CreateQuizDto;
import engine.dto.QuizInfoDto;
import engine.dto.SolveQuizDto;
import engine.dto.SolveQuizInfoDto;
import engine.entity.CompletedQuiz;
import engine.entity.QuizEntity;
import engine.service.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class QuizController {

    private final QuizService quizService;

    @GetMapping("/quizzes/{id}")
    public ResponseEntity<QuizInfoDto> getQuiz(@PathVariable int id) {
        return quizService.getQuiz(id);
    }

    @GetMapping("/quizzes")
    public ResponseEntity<Page<QuizEntity>> getAllQuizzes(@RequestParam(defaultValue = "0") int page) {
        return quizService.getAllQuizzes(page);
    }

    @PostMapping("/quizzes/{id}/solve")
    public ResponseEntity<SolveQuizInfoDto> solveQuiz(
            @PathVariable int id,
            @RequestBody SolveQuizDto request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return quizService.solveQuiz(id, request, userDetails);
    }

    @GetMapping("/quizzes/completed")
    public ResponseEntity<Page<CompletedQuiz>> getCompletedQuizzes(
            @RequestParam(defaultValue = "0") int page,
            @AuthenticationPrincipal UserDetails userDetails) {
        return quizService.getCompletedQuizzes(page, userDetails);
    }

    @PostMapping("/quizzes")
    public ResponseEntity<QuizInfoDto> createQuiz(
            @Valid @RequestBody CreateQuizDto request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return quizService.createQuiz(request, userDetails);
    }

    @DeleteMapping("/quizzes/{id}")
    public ResponseEntity<?> deleteQuiz(
            @PathVariable int id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return quizService.deleteQuiz(id, userDetails);
    }
}
