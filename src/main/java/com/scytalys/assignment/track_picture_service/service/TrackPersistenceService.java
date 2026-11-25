package com.scytalys.assignment.track_picture_service.service;

import com.scytalys.assignment.track_picture_service.dto.TrackUpdateDto;
import com.scytalys.assignment.track_picture_service.repository.CurrentPictureRepository;
import com.scytalys.assignment.track_picture_service.repository.TrackHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackPersistenceService {

    private final TrackHistoryRepository trackHistoryRepository;
    private final CurrentPictureRepository currentPictureRepository;
    private final EventFingerprintService eventFingerprintService;

    @Transactional
    public void persist(TrackUpdateDto update){

        String fingerprint = eventFingerprintService.generate(update);

        Instant timestamp = update.getTimestamp().truncatedTo(ChronoUnit.MICROS);

        int inserted = trackHistoryRepository.inserted(fingerprint,update.getTrackId(),timestamp,update.getLatitude(),update.getLongitude(),update.getAltitude(),update.getHeading(),update.getIdentity().name());

        if (inserted == 0){
            log.info("Duplicate ignored for: {}" , update.getTrackId());
            return;
        }

        //else it is not duplicated
        int upserted = currentPictureRepository.upserted(update.getTrackId(),timestamp,update.getLatitude(),update.getLongitude(),update.getAltitude(),update.getHeading(),update.getIdentity().name());

        if (upserted == 0){
            log.info("History updated done for: {}" , update.getTrackId());
        }
        else {
            log.info("Current picture updated for: {}" , update.getTrackId());
        }

    }


}
