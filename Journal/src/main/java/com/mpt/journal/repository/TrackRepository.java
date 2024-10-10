package com.mpt.journal.repository;

import com.mpt.journal.model.TrackModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackRepository  extends JpaRepository<TrackModel, Long> {

}
