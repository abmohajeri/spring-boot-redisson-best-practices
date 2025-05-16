package ir.abolfazlmohajeri.redis.stream;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.redisson.api.RStream;
import org.redisson.api.RedissonClient;
import org.redisson.api.StreamMessageId;
import org.redisson.api.stream.StreamAddArgs;
import org.redisson.api.stream.StreamCreateGroupArgs;
import org.redisson.api.stream.StreamReadGroupArgs;
import org.redisson.client.RedisBusyException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by abolfazl on 2025-05-16
 **/
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StreamExample {
    RedissonClient redissonClient;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        RStream<String, String> stream = redissonClient.getStream("usersEvents");
        try {
            stream.createGroup(StreamCreateGroupArgs.name("usersGroup").makeStream());
        } catch (RedisBusyException e) {
            System.out.println("Consumer group 'usersGroup' already exists.");
        }

        Map<String, String> event = Map.of(
                "userId", "123",
                "timestamp", String.valueOf(System.currentTimeMillis())
        );
        stream.add(StreamAddArgs.entries(event));

        Map<StreamMessageId, Map<String, String>> messages =
                stream.readGroup("usersGroup", "consumer-A", StreamReadGroupArgs.neverDelivered());
        messages.entrySet().stream()
                .peek(entry -> {
                    String id = entry.getKey().toString();
                    String body = entry.getValue().entrySet().stream()
                            .map(e -> e.getKey() + "=" + e.getValue())
                            .collect(Collectors.joining(", "));
                    System.out.printf("Processing message [%s]: {%s}%n", id, body);
                })
                .forEach(entry -> stream.ack("usersGroup", entry.getKey()));
    }
}
