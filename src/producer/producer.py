
import json
import time
from datetime import datetime, timezone, timedelta

from kafka import KafkaProducer


producer = KafkaProducer(
    bootstrap_servers="localhost:9092",
    key_serializer=lambda key: key.encode("utf-8"),
    value_serializer=lambda value: json.dumps(value).encode("utf-8"),
    acks="all"
)


def publish(update, description):
    metadata = producer.send(
        "track-updates",
        key=update["trackId"],
        value=update
    ).get(timeout=10)

    print(
        f"{description}: {update['trackId']} | "
        f"Partition: {metadata.partition} | "
        f"Offset: {metadata.offset}"
    )


def create_update(track_id, timestamp, latitude, longitude,
                  altitude, heading, identity):

    return {
        "trackId": track_id,
        "timestamp": timestamp.isoformat().replace("+00:00", "Z"),
        "latitude": latitude,
        "longitude": longitude,
        "altitude": altitude,
        "heading": heading,
        "identity": identity
    }


def main():
    start_time = datetime.now(timezone.utc).replace(microsecond=0)

    tracks = [
        ("DEMO-001", 37.98, 23.72, 1000, 90, "FRIEND"),
        ("DEMO-002", 38.10, 23.85, 2000, 180, "NEUTRAL"),
        ("DEMO-003", 38.20, 24.00, 3000, 270, "UNKNOWN")
    ]

    last_update = None

    for step in range(5):
        for track in tracks:
            track_id, lat, lon, altitude, heading, identity = track

            update = create_update(
                track_id,
                start_time + timedelta(seconds=step),
                lat + step * 0.01,
                lon + step * 0.01,
                altitude + step * 10,
                heading,
                identity
            )

            publish(update, "Valid update")

            if track_id == "DEMO-001":
                last_update = update

        time.sleep(1)

    # Duplicate
    publish(last_update, "Duplicate update")

    # Out-of-order
    older_update = create_update(
        "DEMO-001",
        start_time - timedelta(seconds=10),
        37.90, 23.60, 900, 85, "FRIEND"
    )

    publish(older_update, "Out-of-order update")

    # Invalid latitude
    invalid_update = create_update(
        "DEMO-001",
        start_time + timedelta(seconds=10),
        150, 23.80, 1200, 90, "FRIEND"
    )

    publish(invalid_update, "Invalid update")


if __name__ == "__main__":
    try:
        main()
    finally:
        producer.close()
