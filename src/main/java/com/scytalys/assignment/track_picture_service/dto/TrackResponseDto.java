package com.scytalys.assignment.track_picture_service.dto;

import com.scytalys.assignment.track_picture_service.model.Identity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class TrackResponseDto {
    private String trackId;
    private Instant timestamp;
    private Double latitude;
    private Double longitude;
    private Double altitude;
    private Double heading;
    private Identity identity;
}
