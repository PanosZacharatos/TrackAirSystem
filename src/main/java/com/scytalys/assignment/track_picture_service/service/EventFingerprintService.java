package com.scytalys.assignment.track_picture_service.service;

import com.scytalys.assignment.track_picture_service.dto.TrackUpdateDto;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.temporal.ChronoUnit;
import java.util.HexFormat;

@Service
public class EventFingerprintService {

    public String generate(TrackUpdateDto trackUpdate){
        String content = String.join("|",
                trackUpdate.getTrackId(),
                trackUpdate.getTimestamp()
                        .truncatedTo(ChronoUnit.MICROS)
                        .toString(),
                number(trackUpdate.getLatitude()),
                number(trackUpdate.getLongitude()),
                number(trackUpdate.getAltitude()),
                number(trackUpdate.getHeading()),
                trackUpdate.getIdentity().name()
        );

        try{
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(content.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hash);
        }catch (NoSuchAlgorithmException e){
            throw new IllegalStateException("Algorithm not available",e);
        }

    }

    private String number(Double val){
        return val == null ? "null" : Double.toHexString(val);
    }
}
