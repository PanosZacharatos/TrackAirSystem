package com.scytalys.assignment.track_picture_service.messaging;

import com.scytalys.assignment.track_picture_service.service.TrackUpdateService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TrackUpdateConsumer {

    private final TrackUpdateService trackUpdateService;


    public TrackUpdateConsumer(TrackUpdateService trackUpdateService) {
        this.trackUpdateService = trackUpdateService;
    }

    //consumer receives message
    @KafkaListener(
            topics = "track-updates",
            groupId = "track-picture-service"
    )
    public void consume(String message){
        System.out.println("Track update received " + message );
        trackUpdateService.process(message);
    }
}
