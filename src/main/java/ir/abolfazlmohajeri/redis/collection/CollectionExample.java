package ir.abolfazlmohajeri.redis.collection;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.redisson.api.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Created by abolfazl on 2025-05-15
 **/
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CollectionExample {
    RedissonClient redissonClient;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        RBucket<String> bucket = redissonClient.getBucket("myKey");
        bucket.set("Hello, Redisson!");
        System.out.println(bucket.get());
        bucket.set("Hello, Redisson with TTL!", Duration.ofSeconds(5));
        System.out.println(bucket.get());

        RList<String> list = redissonClient.getList("myList");
        list.add("Apple");
        list.add("Banana");
        System.out.println(list.get(0));

        RSet<String> set = redissonClient.getSet("mySet");
        set.add("A");
        set.add("B");
        set.add("A");
        System.out.println(set.contains("B"));
        redissonClient.getKeys().expire("mySet", 30, TimeUnit.SECONDS);

        RMap<String, Integer> map = redissonClient.getMap("myMap");
        map.put("views", 10);
        System.out.println(map.get("views"));

        RScoredSortedSet<String> scoredSet = redissonClient.getScoredSortedSet("mySortedSet");
        scoredSet.add(9.5, "Alice");
        scoredSet.add(8.0, "Bob");
        System.out.println(scoredSet.first());

        RGeo<String> geo = redissonClient.getGeo("myGeo");
        geo.add(13.361389, 38.115556, "Palermo");
        geo.add(15.087269, 37.502669, "Catania");
        System.out.println(geo.dist("Palermo", "Catania", GeoUnit.KILOMETERS));

        RBitSet bitSet = redissonClient.getBitSet("myBitSet");
        bitSet.set(0, true);
        bitSet.set(1, false);
        System.out.println(bitSet.get(0));

        RMapCache<String, String> mapCache = redissonClient.getMapCache("myMapCache");
        mapCache.put("hello", "world", 30, TimeUnit.SECONDS);

        RMapCacheNative<String, String> mapCacheNative = redissonClient.getMapCacheNative("myNativeMapCache");
        mapCacheNative.put("hello", "world", Duration.ofSeconds(10));
    }
}
