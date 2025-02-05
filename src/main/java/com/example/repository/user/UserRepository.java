package com.example.repository.user;

import com.example.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u.email FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(String email);

}
