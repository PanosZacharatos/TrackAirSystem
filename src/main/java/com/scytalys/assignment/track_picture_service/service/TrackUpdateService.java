package com.scytalys.assignment.track_picture_service.service;

import com.scytalys.assignment.track_picture_service.dto.TrackUpdateDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;
import java.util.Set;


@Service
public class TrackUpdateService {
    private final ObjectMapper objectMapper;
    private final Validator validator;


    public TrackUpdateService(ObjectMapper objectMapper, Validator validator) {
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    public void process(String message){
        try{
            TrackUpdateDto trackUpdateDto = objectMapper.readValue(message, TrackUpdateDto.class);
            Set<ConstraintViolation<TrackUpdateDto>> violations = validator.validate(trackUpdateDto);

            if(!violations.isEmpty()){
                for(ConstraintViolation<TrackUpdateDto> violation: violations){
                    System.out.println("Rejected due to violation rule " + violation.getPropertyPath() + " - " + violation.getMessage());
                }
                return;
            }

            System.out.println("Successful track update with id " + trackUpdateDto.getTrackId());

        }catch (Exception e){
            System.out.println("Rejected due to: " + e.getMessage());
        }
    }
}
