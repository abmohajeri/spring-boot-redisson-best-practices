package ir.abolfazlmohajeri.redis.lock;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Created by abolfazl on 2025-05-15
 **/
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LockExample {
    RedissonClient redissonClient;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        tryLock();
        lock();
    }

    private void tryLock() {
        RLock lock = redissonClient.getLock("TRYLOCK_ID");
        try {
            if (lock.tryLock(5, 10, TimeUnit.SECONDS)) {
                try {
                    System.out.println("Lock acquired with tryLock");
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void lock() {
        RLock lock = redissonClient.getLock("LOCK_ID");
        lock.lock();
        try {
            System.out.println("Lock acquired with lock");
        } finally {
            lock.unlock();
        }
    }
}
