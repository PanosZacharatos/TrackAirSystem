package com.scytalys.assignment.track_picture_service.dto;

import com.scytalys.assignment.track_picture_service.model.Identity;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class TrackUpdateDto {

    @NotBlank
    String trackId;

    @NotNull
    Instant timestamp;

    @NotNull
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    Double latitude;

    @NotNull
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    Double longitude;

    @DecimalMin("0.0")
    Double altitude;

    @DecimalMin("0.0")
    @DecimalMax(value = "360.0", inclusive = false)
    Double heading;

    @NotNull
    Identity identity;

    public String getTrackId() {
        return trackId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public Double getHeading() {
        return heading;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public void setHeading(Double heading) {
        this.heading = heading;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }
}
