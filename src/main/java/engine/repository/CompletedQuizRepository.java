package engine.repository;

import engine.entity.CompletedQuiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletedQuizRepository extends JpaRepository<CompletedQuiz, Integer> {
    Page<CompletedQuiz> findAllByUserEmailOrderByCompletedAtDesc(String email, Pageable pageable);
}
