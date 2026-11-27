package com.scytalys.assignment.track_picture_service;

import com.scytalys.assignment.track_picture_service.dto.TrackUpdateDto;
import com.scytalys.assignment.track_picture_service.service.TrackPersistenceService;
import com.scytalys.assignment.track_picture_service.service.TrackUpdateService;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import static org.mockito.Mockito.*;

public class TrackUpdateServiceTest {

    private TrackPersistenceService persistenceService;
    private TrackUpdateService trackUpdateService;
    private ValidatorFactory validatorFactory;

    public static final String VALID_MESSAGE = """
            {
                "trackId": "TEST-001",
                "timestamp": "2026-11-25T09:00:00Z",
                "latitude": 37.98,
                "longitude": 23.72,
                "altitude": 1000,
                "heading": 90,
                "identity": "FRIEND"
            }
            """;

    @BeforeEach
    void setUp(){
        persistenceService = mock(TrackPersistenceService.class);
        validatorFactory = Validation.buildDefaultValidatorFactory();

        trackUpdateService = new TrackUpdateService(JsonMapper.builder().build(),validatorFactory.getValidator(),persistenceService);

    }

    @AfterEach
    void tearDown(){
        validatorFactory.close();
    }

    @Test
    void valid(){
        trackUpdateService.process(VALID_MESSAGE);

        verify(persistenceService,times(1)).persist(any(TrackUpdateDto.class));
    }

    @Test
    void malformed(){
        trackUpdateService.process("Invalid message");

        verifyNoInteractions(persistenceService);
    }

    @Test
    void nullMsg(){
        trackUpdateService.process("null");
        verifyNoInteractions(persistenceService);
    }

    @Test
    void invalidTrackID(){
        trackUpdateService.process(VALID_MESSAGE.replace("TEST-001",""));

        verifyNoInteractions(persistenceService);
    }

    @Test
    void invalidLatitude(){
        trackUpdateService.process(VALID_MESSAGE.replace("37.98","120"));
        verifyNoInteractions(persistenceService);
    }

    @Test
    void invalidLongitude(){
        trackUpdateService.process(VALID_MESSAGE.replace("23.72","200"));
        verifyNoInteractions(persistenceService);

    }

    @Test
    void invalidAltitude(){
        trackUpdateService.process(VALID_MESSAGE.replace("1000","-100"));
        verifyNoInteractions(persistenceService);
    }

    @Test
    void invalidHeading(){
        trackUpdateService.process(VALID_MESSAGE.replace("90","360"));
        verifyNoInteractions(persistenceService);
    }

    @Test
    void invalidIdentity(){
        trackUpdateService.process(VALID_MESSAGE.replace("FRIEND","INVALID"));
        verifyNoInteractions(persistenceService);
    }

    @Test
    void noTimestamp(){
        trackUpdateService.process(VALID_MESSAGE.replace("\"2026-11-25T09:00:00Z\"", "null"));
        verifyNoInteractions(persistenceService);
    }

    @Test
    void optionalFields(){
        trackUpdateService.process(VALID_MESSAGE.replace("1000","null").replace("90","null"));
        verify(persistenceService, times(1)).persist(any(TrackUpdateDto.class));
    }

}
