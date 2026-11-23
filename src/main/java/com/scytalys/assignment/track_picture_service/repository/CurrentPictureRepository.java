package com.scytalys.assignment.track_picture_service.repository;

import com.scytalys.assignment.track_picture_service.model.CurrentPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;

public interface CurrentPictureRepository extends JpaRepository<CurrentPicture, String> {

    @Modifying
    @Query(value = """
        INSERT INTO current_picture(
            track_id,timestamp,latitude,longitude,altitude,heading,identity
        )
        values(:trackId,:timestamp,:latitude,:longitude,:altitude,:heading,:identity)
        on conflict (track_id) do update set
            timestamp = EXCLUDED.timestamp,
            latitude = EXCLUDED.latitude,
            longitude = EXCLUDED.longitude,
            altitude = EXCLUDED.altitude,
            heading = EXCLUDED.heading,
            identity = EXCLUDED.identity
            where current_picture.timestamp < EXCLUDED.timestamp
        """,nativeQuery = true)
    int upserted(
            @Param("trackId") String trackId,
            @Param("timestamp") Instant timestamp,
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("altitude") Double altitude,
            @Param("heading") Double heading,
            @Param("identity") String identity
    );
}
