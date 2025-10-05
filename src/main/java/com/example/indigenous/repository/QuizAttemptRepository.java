package com.example.indigenous.repository;

import com.example.indigenous.model.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    List<QuizAttempt> findTop20ByUserEmailOrderByTakenAtDesc(String userEmail);
}
