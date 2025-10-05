package com.example.indigenous.repository;
import com.example.indigenous.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);
  boolean existsByEmail(String email);

    // NEW: for Admin Dashboard "recent users" table
    List<User> findTop10ByOrderByCreatedAtDesc();
}

