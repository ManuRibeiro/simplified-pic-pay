package com.picpaychallenge.simplifiedpicpay.repository;

import com.picpaychallenge.simplifiedpicpay.model.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface userRepository extends JpaRepository<User,Long> {
    Optional<User> findUsersByDocument(String document);
    Optional<User> findUsersById(Long Id);
}
