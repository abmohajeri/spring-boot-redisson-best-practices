package ir.abolfazlmohajeri.redis.task;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.redisson.api.RScheduledExecutorService;
import org.redisson.api.RedissonClient;
import org.redisson.api.WorkerOptions;
import org.redisson.api.executor.TaskSuccessListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by abolfazl on 2025-05-15
 **/
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskExample {
    RedissonClient redissonClient;

    @EventListener(ApplicationReadyEvent.class)
    public void init() throws ExecutionException, InterruptedException {
        WorkerOptions options = WorkerOptions.defaults()
                .workers(1)
                .addListener(new TaskSuccessListener() {
                    @Override
                    public <T> void onSucceeded(String taskId, T result) {
                        System.out.println(taskId + " Succeeded!");
                    }
                });
        RScheduledExecutorService executor = redissonClient.getExecutorService("executor2");
        executor.registerWorkers(options);

        executor.submit((Runnable & Serializable) () -> {
            System.out.println("Runs on one pod only");
        });

        executor.schedule((Runnable & Serializable) () -> {
            System.out.println("Runs after 10 seconds");
        }, 10, TimeUnit.SECONDS);
    }
}
