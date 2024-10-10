package com.mpt.journal.repository;

import com.mpt.journal.model.AlbumModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<AlbumModel, Long> {
    boolean existsByAlbumTitle(String albumTitle);

}
