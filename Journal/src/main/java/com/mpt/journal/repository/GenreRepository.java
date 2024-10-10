package com.mpt.journal.repository;

import com.mpt.journal.model.GenreModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<GenreModel, Long> {
    boolean existsByGenreName(String genreName);

}
