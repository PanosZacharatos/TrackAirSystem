package com.scytalys.assignment.track_picture_service.service;

import com.scytalys.assignment.track_picture_service.dto.TrackUpdateDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackUpdateService {
    private final ObjectMapper objectMapper;
    private final Validator validator;
    private final TrackPersistenceService trackPersistenceService;

    public void process(String message){

        TrackUpdateDto trackUpdateDto;
        try{
            trackUpdateDto = objectMapper.readValue(message, TrackUpdateDto.class);
        }catch (JacksonException e){
            log.warn("Rejected due to invalid message: {}", e.getMessage());
            return;
        }

        if(trackUpdateDto == null){
            log.warn("Rejected due to null message");
            return;
        }

        Set<ConstraintViolation<TrackUpdateDto>> violations = validator.validate(trackUpdateDto);

        if(!violations.isEmpty()){
            for(ConstraintViolation<TrackUpdateDto> violation : violations){
                log.warn("Rejected due to violation rule {}-{}", violation.getPropertyPath(),violation.getMessage());
            }

            return;
        }

        trackPersistenceService.persist(trackUpdateDto);


    }
}
