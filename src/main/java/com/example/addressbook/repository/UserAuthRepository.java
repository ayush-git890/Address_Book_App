package com.example.addressbook.repository;

import com.example.addressbook.model.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {
    @Query(value = "SELECT * FROM ACCOUNTS WHERE EMAIL = :email", nativeQuery = true)
    UserAuth findByEmail(@Param("email") String email);
    UserAuth findByResetToken(String resetToken);
}
