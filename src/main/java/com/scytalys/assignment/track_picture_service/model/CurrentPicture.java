package com.scytalys.assignment.track_picture_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "current_picture")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrentPicture {

    @Id
    @Column(name = "track_id")
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
