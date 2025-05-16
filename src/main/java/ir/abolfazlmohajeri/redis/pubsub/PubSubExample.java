package ir.abolfazlmohajeri.redis.pubsub;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Created by abolfazl on 2025-05-15
 **/
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PubSubExample {
    RedissonClient redissonClient;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        RTopic topic = redissonClient.getTopic("myTopic");
        topic.addListener(Integer.class, new MessageListener<Integer>() {
            @Override
            public void onMessage(CharSequence channel, Integer message) {
                System.out.println("Message is: " + message);
            }
        });
        topic.publish(1);
        topic.publish(2);
        topic.publish(3);

        RTopic rTopic = redissonClient.getTopic("myReliableTopic");
        rTopic.addListener(Integer.class, new MessageListener<Integer>() {
            @Override
            public void onMessage(CharSequence channel, Integer message) {
                System.out.println("Message is: " + message);
            }
        });
        rTopic.publish(1);
        rTopic.publish(2);
        rTopic.publish(3);
    }
}
