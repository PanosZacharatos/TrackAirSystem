package com.scytalys.assignment.track_picture_service.repository;

import com.scytalys.assignment.track_picture_service.model.TrackHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface TrackHistoryRepository extends JpaRepository<TrackHistory,Long> {

    List<TrackHistory> findByTrackIdOrderByTimestampDesc(String trackId);

    List<TrackHistory> findByTrackIdAndTimestampBetweenOrderByTimestampDesc(String trackId, Instant from, Instant to);

}
