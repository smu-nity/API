package com.smunity.api.domain.account.repository;

import com.smunity.api.domain.account.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    User getByUsername(String username);
}