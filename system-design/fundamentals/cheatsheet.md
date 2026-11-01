# Fundamentals cheat sheet

## Numbers worth memorising (orders of magnitude)

| Operation | Rough cost |
|---|---|
| Read from RAM | ~100 ns |
| SSD random read | ~100 microseconds |
| Round trip inside one data centre | ~0.5 ms |
| Read 1 MB sequentially from SSD | ~1 ms |
| Round trip across continents | ~100 to 150 ms |

Useful conversions: a day has ~86,400 seconds (call it 100k), so **1 million requests a day is about 12 per second**.
A year is ~30 million seconds. A single well-tuned relational database node handles on the order of
thousands of simple queries per second, not millions.

## Estimating quickly

1. Start from daily active users and actions per user per day.
2. Divide by 100,000 for average QPS. Multiply by 2 to 5 for peak.
3. Storage = records per day x bytes per record x 365 x retention years. Add replication (usually 3x).
4. Reads vs writes decides almost everything else: read-heavy leans on caches and replicas, write-heavy on partitioning and queues.

## Consistency and availability

- **CAP**: during a network partition you pick consistency or availability. When the network is healthy you get both. The real day-to-day trade is latency vs consistency.
- **Strong consistency**: every read sees the latest write. Needed for balances, inventory, uniqueness.
- **Eventual consistency**: replicas converge given time. Fine for feeds, counters, view counts.
- **Read-your-writes**: a user sees their own update immediately. Often enough, and cheaper than full strong consistency.

## Caching

| Pattern | How it works | Watch out for |
|---|---|---|
| Cache-aside | App checks cache, falls back to DB, then fills cache | Stale data, stampede on a cold key |
| Write-through | Write to cache and DB together | Slower writes |
| Write-behind | Write to cache, flush to DB later | Data loss if the cache dies |

Eviction: LRU is the default. TTLs bound staleness. Protect hot keys with request coalescing or a short local cache in front of the shared one.

## Partitioning (sharding)

- **Range**: easy range scans, risk of hot ranges (all today's data on one shard).
- **Hash**: even spread, range queries get expensive.
- **Consistent hashing**: adding or removing a node moves only a fraction of keys.
- Pick the shard key by your most common query. A bad key means cross-shard joins forever.

## Replication

- Leader/follower: writes to the leader, reads from followers. Followers lag, so reads can be stale.
- Multi-leader and leaderless: better write availability, you must handle conflicts.

## Queues and streams

Use a queue to absorb bursts, decouple slow work, and retry. The price is that you now have
at-least-once delivery, so **consumers must be idempotent**. Use a dead-letter queue for messages that keep failing.

## Reliability checklist

- Timeouts on every network call. Retries with exponential backoff and jitter. A circuit breaker around flaky dependencies.
- Idempotency keys on anything that moves money or sends a message.
- Health checks, graceful degradation (serve cached or partial data instead of an error).
- Metrics for the four golden signals: latency, traffic, errors, saturation.

## SQL or NoSQL

Default to a relational database until a specific need rules it out: massive write volume, flexible
schema, key-value access at huge scale, or a very specific data shape (graph, time series, search).
