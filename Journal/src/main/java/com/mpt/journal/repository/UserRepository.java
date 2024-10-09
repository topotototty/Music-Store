package com.mpt.journal.repository;

import com.mpt.journal.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByLogin(String login);
    Optional<UserModel> findByEmail(String email);
}

