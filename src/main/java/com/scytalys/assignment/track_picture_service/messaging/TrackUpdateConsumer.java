package com.scytalys.assignment.track_picture_service.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TrackUpdateConsumer {

    //consumer receives message
    @KafkaListener(
            topics = "track-updates",
            groupId = "track-picture-service"
    )
    public void consume(String message){
        System.out.println("Track update received " + message );
    }
}
