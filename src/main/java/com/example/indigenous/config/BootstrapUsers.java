// src/main/java/com/example/indigenous/config/BootstrapUsers.java
package com.example.indigenous.config;

import com.example.indigenous.model.Role;
import com.example.indigenous.model.User;
import com.example.indigenous.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Set;

@Component
@Profile("mysql") // run only when your mysql profile is active
public class BootstrapUsers implements CommandLineRunner {

    private final UserRepository users;
    private final PasswordEncoder encoder;

    public BootstrapUsers(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        // 1) Admin: always ensure email/roles and reset password to admin123
        var admin = users.findByEmail("admin@example.com").orElseGet(User::new);
        admin.setEmail("admin@example.com");
        admin.setPasswordHash(encoder.encode("admin123")); // <= force reset
        admin.setRoles(Set.of(Role.ADMIN));
        if (admin.getCreatedAt() == null) admin.setCreatedAt(Instant.now());
        users.save(admin);

        // 2) Regular user: create if missing
        users.findByEmail("user@example.com").orElseGet(() -> {
            var u = new User();
            u.setEmail("user@example.com");
            u.setPasswordHash(encoder.encode("user123"));
            u.setRoles(Set.of(Role.USER));
            u.setCreatedAt(Instant.now());
            return users.save(u);
        });
    }
}
