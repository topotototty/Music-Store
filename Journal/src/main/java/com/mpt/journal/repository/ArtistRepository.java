package com.mpt.journal.repository;

import com.mpt.journal.model.ArtistModel;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<ArtistModel, Long> {

}
