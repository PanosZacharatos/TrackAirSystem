package com.scytalys.assignment.track_picture_service.controller;

import com.scytalys.assignment.track_picture_service.dto.TrackResponseDto;
import com.scytalys.assignment.track_picture_service.service.TrackQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TrackController {

    private final TrackQueryService trackQueryService;

    @GetMapping("/picture")
    public ResponseEntity<List<TrackResponseDto>> getPicture(){
        return ResponseEntity.ok(trackQueryService.getPicture());
    }

    @GetMapping("/tracks/{trackId}")
    public ResponseEntity<TrackResponseDto> getTrack(@PathVariable String trackId){
        return ResponseEntity.of(trackQueryService.getTrack(trackId));
    }

    @GetMapping("/tracks/{trackId}/history")
    public ResponseEntity<List<TrackResponseDto>> getHistory(
            @PathVariable String trackId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to){

        if (from != null && to != null && from.isAfter(to)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
                trackQueryService.getHistory(trackId, from, to));
    }


}
