package com.mpt.journal.repository;

import com.mpt.journal.model.ArtistModel;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<ArtistModel, Long> {
    boolean existsByArtistName(String artistName);

}
