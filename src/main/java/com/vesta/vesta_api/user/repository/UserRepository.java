package com.vesta.vesta_api.user.repository;

import com.vesta.vesta_api.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
