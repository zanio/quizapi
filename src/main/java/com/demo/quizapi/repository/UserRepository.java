package com.demo.quizapi.repository;

import com.demo.quizapi.entities.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserRepository extends PagingAndSortingRepository<User, UUID> {
  Optional<User> findByEmail(String email);
}
