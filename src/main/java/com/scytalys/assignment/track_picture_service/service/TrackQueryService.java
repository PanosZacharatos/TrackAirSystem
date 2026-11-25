package com.scytalys.assignment.track_picture_service.service;

import com.scytalys.assignment.track_picture_service.dto.TrackResponseDto;
import com.scytalys.assignment.track_picture_service.model.CurrentPicture;
import com.scytalys.assignment.track_picture_service.model.TrackHistory;
import com.scytalys.assignment.track_picture_service.repository.CurrentPictureRepository;
import com.scytalys.assignment.track_picture_service.repository.TrackHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrackQueryService {

    private final CurrentPictureRepository currentPictureRepository;
    private final TrackHistoryRepository trackHistoryRepository;

    public List<TrackResponseDto> getPicture(){
        return currentPictureRepository.findAll().stream().map(this::toDto).toList();
    }

    public Optional<TrackResponseDto> getTrack(String trackId){
        return currentPictureRepository.findById(trackId).map(this::toDto);
    }

    public List<TrackResponseDto> getHistory(String trackId, Instant from, Instant to){
        List<TrackHistory> history;

        if(from != null && to != null){
            history = trackHistoryRepository.findByTrackIdAndTimestampBetweenOrderByTimestampDesc(trackId,from,to);

        } else if (from != null) {
            history = trackHistoryRepository.findByTrackIdAndTimestampGreaterThanEqualOrderByTimestampDesc(trackId,from);

        } else if (to != null) {
            history = trackHistoryRepository.findByTrackIdAndTimestampLessThanEqualOrderByTimestampDesc(trackId,to);
        }
        else {
            history = trackHistoryRepository.findByTrackIdOrderByTimestampDesc(trackId);
        }

        return history.stream().map(this::toDto).toList();
    }

    private TrackResponseDto toDto(CurrentPicture currentPicture){
        return new TrackResponseDto(
          currentPicture.getTrackId(),
          currentPicture.getTimestamp(),
          currentPicture.getLatitude(),
          currentPicture.getLongitude(),
          currentPicture.getAltitude(),
          currentPicture.getHeading(),
          currentPicture.getIdentity()
        );
    }

    private TrackResponseDto toDto(TrackHistory trackHistory){
        return new TrackResponseDto(
                trackHistory.getTrackId(),
                trackHistory.getTimestamp(),
                trackHistory.getLatitude(),
                trackHistory.getLongitude(),
                trackHistory.getAltitude(),
                trackHistory.getHeading(),
                trackHistory.getIdentity()
        );
    }
}
