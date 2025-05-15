package ir.abolfazlmohajeri.redis.task;

import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.redisson.api.RScheduledExecutorService;
import org.redisson.api.RedissonClient;
import org.redisson.api.WorkerOptions;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Created by abolfazl on 2025-05-15
 **/
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskExample {
    RedissonClient redissonClient;

    @PostConstruct
    public void init() {
        RScheduledExecutorService executorService = redissonClient.getExecutorService("myExecutor");

//        executorService.submit((Runnable & Serializable) () -> {
//            System.out.println("This task runs on one pod only");
//        });

//        executorService.schedule((Runnable & Serializable) () -> {
//            System.out.println("Runs once after 10 seconds");
//        }, 10, TimeUnit.SECONDS);

//        executorService.scheduleAtFixedRate((Runnable & Serializable) () -> {
//            System.out.println("Runs every 30 seconds (fixed rate)");
//        }, 0, 30, TimeUnit.SECONDS);
//
//        executorService.scheduleWithFixedDelay((Runnable & Serializable) () -> {
//            System.out.println("Runs again 1 min after task completes");
//        }, 0, 1, TimeUnit.MINUTES);

        executorService.shutdown();
    }
}
