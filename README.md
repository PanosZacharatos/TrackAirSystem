Technologies: Java 17, Springboot, Hibernate, Kafka, Postegresql, Docker, Python, JUnit, Mockito


Architecture

External Producer(python) -> Kafka(track update) -> Kafka Consumer -> Validation and Processing -> Database 
-> Current Picture & History -> API


How to run:

Prerequisites: Java17, Docker, Python 3.x

1.Start postgresql and kafka: docker compose up -d

2. Create kafka topic: docker exec track-app-kafka /opt/kafka/bin/kafka-topics.sh --create --if-not-exists --topic track-updates --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1

3. Initialize the Database:
   a)docker cp src/main/resources/schema.sql track-app-postgres:/tmp/schema.sql
   b)docker exec -e PGPASSWORD=1234 track-app-postgres psql -U sa -d AirDefenceSystem -v ON_ERROR_STOP=1 -f /tmp/schema.sql

4. Start the application: .\mvnw.cmd spring-boot:run

5. Run the producer: 
    a)python -m pip install -r src/producer/requirements.txt
    b)python src/producer/producer.py

The producer publishes 18 messages including duplicates, out of order updates and invalid messages. If the db is empty
of records the expected result is 3 tracks and 16 history records.


REST API's:

Base URL: http://localhost:8080

1) GET /picture -> fetches all current tracks
2) GET /tracks/{trackId} -> Fetches the current track state
3) GET /tracks/{trackId}/history -> Fetches the history(in desc order by timestamp) and supports optionally from & to filters.

Design Decisions:
    Ordering: The producer uses the trackId as Kafka message key to keep updates for the same track in the same partition.
              Older updates are stored in history without overwriting the current picture.
    Validation: Invalid messages are rejected before persistence and the rejection reason is logged.
    Idempotency: Since no event ID is provided, a SHA-256 fingerprint and a unique database constraint prevent duplicate records.
    Storage: Postgresql was chosen for its transactions, constraints, indexing and atomic upsert operations. Picture and history are updated within the same transaction.
    Failure handling: Kafka offsets are committed after successful processing. Recoverable failures are retried with a fixed backoff. Idempotency ensures safe redelivery.


Testing:
21 unit tests cover validation, fingerprint generation and persistence logic: .\mvnw.cmd "-Dtest=EventFingerprintServiceTest,TrackUpdateServiceTest,TrackPersistenceServiceTest" test

Trade-offs and future improvements:
    1)The application uses a single service and a single Kafka broker to keep the implementation simple.
    2)Identical update content is treated as a duplicate. Timestamps are stored with microsecond precision, and the first update wins when timestamps are equal. Track IDs are limited to 255 characters by the database. Unlimited retries may block processing on permanent failures.
    3)For future improvements, I would add stricter validation, dead-letter handling, integration tests, history pagination, track staleness detection and consumer lag monitoring.