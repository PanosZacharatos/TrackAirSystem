package com.scytalys.assignment.track_picture_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.kafka.shaded.com.google.protobuf.Internal;

import java.time.Instant;

@Entity
@Table(name = "track_history", uniqueConstraints = @UniqueConstraint(name = "uk_history_fingerprint", columnNames = "event_fingerprint"))
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrackHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_fingerprint", nullable = false, length = 64)
    private String eventFingerprint;

    @Column(name = "track_id", nullable = false)
    private String trackId;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "altitude")
    private Double altitude;

    @Column(name = "heading")
    private Double heading;

    @Enumerated(EnumType.STRING)
    @Column(name = "identity", nullable = false)
    private Identity identity;

}
