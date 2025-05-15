package ir.abolfazlmohajeri.redis.health;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.redisson.api.redisnode.RedisCluster;
import org.redisson.api.redisnode.RedisClusterMaster;
import org.redisson.api.redisnode.RedisClusterSlave;
import org.redisson.api.redisnode.RedisNodes;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Created by abolfazl on 18.05.24
 **/
@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RedisMastersHealthIndicator implements HealthIndicator {
    RedissonClient redissonClient;

    @Override
    public Health health() {
        RedisCluster cluster = redissonClient.getRedisNodes(RedisNodes.CLUSTER);

        Collection<RedisClusterMaster> masters = cluster.getMasters();
        for (RedisClusterMaster master : masters) {
            try {
                if (!master.ping()) {
                    log.error("Redis slave node {} is DOWN", master);
                    return Health.down().withDetail("failedNode", master.toString()).build();
                }
            } catch (Exception e) {
                log.error("Redis slave node {} is DOWN", master, e);
                return Health.down().withDetail("failedNode", master.toString()).build();
            }
        }

        return Health.up().withDetail("mastersChecked", masters.size()).build();
    }
}
