package com.scytalys.assignment.track_picture_service.repository;

import com.scytalys.assignment.track_picture_service.model.TrackHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface TrackHistoryRepository extends JpaRepository<TrackHistory,Long> {

    List<TrackHistory> findByTrackIdOrderByTimestampDesc(String trackId);

    List<TrackHistory> findByTrackIdAndTimestampBetweenOrderByTimestampDesc(String trackId, Instant from, Instant to);

    List<TrackHistory> findByTrackIdAndTimestampGreaterThanEqualOrderByTimestampDesc(String trackId, Instant from);

    List<TrackHistory> findByTrackIdAndTimestampLessThanEqualOrderByTimestampDesc(String trackId, Instant to);

    @Modifying
    @Query(value = """
        INSERT INTO track_history(
            event_fingerprint,track_id,timestamp,latitude,longitude,altitude,heading,identity
        )
        values(:fingerprint,:trackId,:timestamp,:latitude,:longitude,:altitude,:heading,:identity)
        on conflict (event_fingerprint) do nothing
        """,nativeQuery = true)
    int inserted(
      @Param("fingerprint") String fingerprint,
      @Param("trackId") String trackId,
      @Param("timestamp") Instant timestamp,
      @Param("latitude") Double latitude,
      @Param("longitude") Double longitude,
      @Param("altitude") Double altitude,
      @Param("heading") Double heading,
      @Param("identity") String identity
    );
}
