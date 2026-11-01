import java.util.concurrent.ConcurrentHashMap;
import java.util.function.LongSupplier;

/**
 * Token bucket rate limiter, one bucket per client key.
 *
 * Each bucket holds up to `capacity` tokens and refills at `refillPerSecond`.
 * A request spends one token; with none left it is rejected.
 * State per client is two numbers, so millions of clients fit in memory.
 *
 * In a multi-server deployment the same logic runs atomically inside the shared
 * store (for example a Redis Lua script) instead of in this process.
 */
public class TokenBucketRateLimiter {
    private final double capacity;
    private final double refillPerNano;
    private final LongSupplier nanoClock;
    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    private static final class Bucket {
        double tokens;
        long lastRefill;

        Bucket(double tokens, long lastRefill) {
            this.tokens = tokens;
            this.lastRefill = lastRefill;
        }
    }

    public TokenBucketRateLimiter(int capacity, double refillPerSecond) {
        this(capacity, refillPerSecond, System::nanoTime);
    }

    // The clock is injectable so behaviour can be tested without sleeping.
    TokenBucketRateLimiter(int capacity, double refillPerSecond, LongSupplier nanoClock) {
        this.capacity = capacity;
        this.refillPerNano = refillPerSecond / 1_000_000_000.0;
        this.nanoClock = nanoClock;
    }

    public boolean tryAcquire(String key) {
        long now = nanoClock.getAsLong();
        Bucket b = buckets.computeIfAbsent(key, k -> new Bucket(capacity, now));
        synchronized (b) {
            double refill = (now - b.lastRefill) * refillPerNano;
            b.tokens = Math.min(capacity, b.tokens + refill);
            b.lastRefill = now;
            if (b.tokens >= 1.0) {
                b.tokens -= 1.0;
                return true;
            }
            return false;
        }
    }

    public static void main(String[] args) {
        long[] fakeNow = {0};
        TokenBucketRateLimiter rl = new TokenBucketRateLimiter(3, 1.0, () -> fakeNow[0]);

        for (int i = 1; i <= 4; i++) {
            System.out.println("request " + i + " at t=0s  -> " + rl.tryAcquire("alice"));
        }
        fakeNow[0] += 2_000_000_000L; // two seconds later, two tokens have refilled
        for (int i = 1; i <= 3; i++) {
            System.out.println("request " + i + " at t=2s  -> " + rl.tryAcquire("alice"));
        }
        System.out.println("bob is independent  -> " + rl.tryAcquire("bob"));
    }
}
