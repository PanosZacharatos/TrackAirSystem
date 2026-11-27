
package com.scytalys.assignment.track_picture_service;

import com.scytalys.assignment.track_picture_service.dto.TrackUpdateDto;
import com.scytalys.assignment.track_picture_service.model.Identity;
import com.scytalys.assignment.track_picture_service.repository.CurrentPictureRepository;
import com.scytalys.assignment.track_picture_service.repository.TrackHistoryRepository;
import com.scytalys.assignment.track_picture_service.service.EventFingerprintService;
import com.scytalys.assignment.track_picture_service.service.TrackPersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.dao.DataAccessResourceFailureException;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TrackPersistenceServiceTest {

    private TrackHistoryRepository historyRepository;
    private CurrentPictureRepository pictureRepository;
    private EventFingerprintService fingerprintService;
    private TrackPersistenceService persistenceService;

    private TrackUpdateDto update;
    private String fingerprint;

    @BeforeEach
    void setUp() {
        historyRepository = mock(TrackHistoryRepository.class);
        pictureRepository = mock(CurrentPictureRepository.class);
        fingerprintService = new EventFingerprintService();

        persistenceService = new TrackPersistenceService(
                historyRepository,
                pictureRepository,
                fingerprintService
        );

        update = createUpdate();
        fingerprint = fingerprintService.generate(update);
    }

    @Test
    void validUpdate() {

        mockHistoryInsert(1);
        mockPictureUpsert(1);

        persistenceService.persist(update);

        InOrder order = inOrder(historyRepository, pictureRepository);

        order.verify(historyRepository).inserted(
                fingerprint,
                update.getTrackId(),
                update.getTimestamp(),
                update.getLatitude(),
                update.getLongitude(),
                update.getAltitude(),
                update.getHeading(),
                update.getIdentity().name()
        );

        order.verify(pictureRepository).upserted(
                update.getTrackId(),
                update.getTimestamp(),
                update.getLatitude(),
                update.getLongitude(),
                update.getAltitude(),
                update.getHeading(),
                update.getIdentity().name()
        );
    }

    @Test
    void duplicateUpdate() {

        mockHistoryInsert(0);

        persistenceService.persist(update);

        verify(historyRepository, times(1)).inserted(
                fingerprint,
                update.getTrackId(),
                update.getTimestamp(),
                update.getLatitude(),
                update.getLongitude(),
                update.getAltitude(),
                update.getHeading(),
                update.getIdentity().name()
        );

        verifyNoInteractions(pictureRepository);
    }

    @Test
    void outOfOrderUpdate() {

        mockHistoryInsert(1);
        mockPictureUpsert(0);

        assertDoesNotThrow(() -> persistenceService.persist(update));

        verify(historyRepository, times(1)).inserted(
                fingerprint,
                update.getTrackId(),
                update.getTimestamp(),
                update.getLatitude(),
                update.getLongitude(),
                update.getAltitude(),
                update.getHeading(),
                update.getIdentity().name()
        );

        verify(pictureRepository, times(1)).upserted(
                update.getTrackId(),
                update.getTimestamp(),
                update.getLatitude(),
                update.getLongitude(),
                update.getAltitude(),
                update.getHeading(),
                update.getIdentity().name()
        );
    }

    @Test
    void databaseFailure() {

        when(historyRepository.inserted(
                fingerprint,
                update.getTrackId(),
                update.getTimestamp(),
                update.getLatitude(),
                update.getLongitude(),
                update.getAltitude(),
                update.getHeading(),
                update.getIdentity().name()
        )).thenThrow(
                new DataAccessResourceFailureException("Database unavailable")
        );

        assertThrows(
                DataAccessResourceFailureException.class,
                () -> persistenceService.persist(update)
        );

        verifyNoInteractions(pictureRepository);
    }

    private void mockHistoryInsert(int result) {

        when(historyRepository.inserted(
                fingerprint,
                update.getTrackId(),
                update.getTimestamp(),
                update.getLatitude(),
                update.getLongitude(),
                update.getAltitude(),
                update.getHeading(),
                update.getIdentity().name()
        )).thenReturn(result);
    }

    private void mockPictureUpsert(int result) {

        when(pictureRepository.upserted(
                update.getTrackId(),
                update.getTimestamp(),
                update.getLatitude(),
                update.getLongitude(),
                update.getAltitude(),
                update.getHeading(),
                update.getIdentity().name()
        )).thenReturn(result);
    }

    private TrackUpdateDto createUpdate() {

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
