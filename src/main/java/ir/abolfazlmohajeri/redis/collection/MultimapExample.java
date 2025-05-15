package ir.abolfazlmohajeri.redis.collection;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.redisson.api.RListMultimap;
import org.redisson.api.RSetMultimap;
import org.redisson.api.RedissonClient;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Created by abolfazl on 2025-05-15
 **/
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MultimapExample {
    RedissonClient redissonClient;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        RSetMultimap<Integer, Integer> set = redissonClient.getSetMultimap("mySetMultimap");
        set.put(1, 1);
        set.put(1, 2);
        set.put(1, 3);
        set.put(1, 3);
        set.put(2, 1);
        System.out.println("SetMultimap:");
        set.keySet().forEach(key -> {
            System.out.println(key + " => " + set.get(key));
        });

        RListMultimap<Integer, Integer> map = redissonClient.getListMultimap("myListMultimap");
        map.put(1, 1);
        map.put(1, 2);
        map.put(1, 3);
        map.put(1, 3);
        map.put(2, 1);
        System.out.println("ListMultimap:");
        map.keySet().forEach(key -> {
            System.out.println(key + " => " + map.get(key));
        });
    }
}
