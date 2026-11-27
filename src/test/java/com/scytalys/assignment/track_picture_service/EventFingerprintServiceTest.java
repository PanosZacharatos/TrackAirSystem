package com.scytalys.assignment.track_picture_service;

import com.scytalys.assignment.track_picture_service.dto.TrackUpdateDto;
import com.scytalys.assignment.track_picture_service.model.Identity;
import com.scytalys.assignment.track_picture_service.service.EventFingerprintService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Instant;

public class EventFingerprintServiceTest {

    private final EventFingerprintService fingerprintService = new EventFingerprintService();

    @Test
    void identicalUpdate(){

        TrackUpdateDto first = createUpdate();
        TrackUpdateDto second = createUpdate();

        assertEquals(fingerprintService.generate(first),fingerprintService.generate(second));
    }

    @Test
    void differentTimestamps(){
        TrackUpdateDto first = createUpdate();
        TrackUpdateDto second = createUpdate();

        second.setTimestamp(first.getTimestamp().plusSeconds(1));

        assertNotEquals(fingerprintService.generate(first),fingerprintService.generate(second));
    }

    @Test
    void differentCoordinates(){
        TrackUpdateDto first = createUpdate();
        TrackUpdateDto second = createUpdate();

        second.setLatitude(38.10);

        assertNotEquals(fingerprintService.generate(first),fingerprintService.generate(second));

    }

    @Test
    void differentTrackId(){
        TrackUpdateDto first = createUpdate();
        TrackUpdateDto second = createUpdate();

        second.setTrackId("TEST-002");

        assertNotEquals(fingerprintService.generate(first),fingerprintService.generate(second));

    }

    @Test
    void nullFields(){

        TrackUpdateDto updateDto = createUpdate();

        updateDto.setAltitude(null);
        updateDto.setHeading(null);

        String fingerprint = assertDoesNotThrow(()->fingerprintService.generate(updateDto));

        assertEquals(64,fingerprint.length());
    }

    @Test
    void fingerprintHashed(){
        String fingerprint = fingerprintService.generate(createUpdate());

        assertTrue(fingerprint.matches("[0-9a-f]{64}"));
    }

    private  TrackUpdateDto createUpdate(){

        TrackUpdateDto updateDto = new TrackUpdateDto();

        updateDto.setTrackId("TEST-001");
        updateDto.setTimestamp(Instant.parse("2026-11-25T09:00:00Z"));
        updateDto.setLatitude(37.98);
        updateDto.setLongitude(23.72);
        updateDto.setAltitude(1000.0);
        updateDto.setHeading(90.0);
        updateDto.setIdentity(Identity.FRIEND);

        return updateDto;
    }

}
