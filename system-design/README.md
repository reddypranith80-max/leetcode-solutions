# System Design

Coding rounds test whether you can solve a problem. Design rounds test whether you can decide
*what to build, what to leave out, and what breaks first*. This folder is the second half of the
repo, built the same way as the first: one repeatable method, then a growing set of worked examples.

## The method

Every design in [`designs/`](designs/) follows [`TEMPLATE.md`](TEMPLATE.md):

1. **Requirements.** What must it do, what is out of scope, what numbers does it have to survive.
2. **Estimates.** Requests per second, storage per year, read to write ratio. Rough is fine, missing is not.
3. **API and data model.** The contract first, then the tables or keys behind it.
4. **High-level design.** A diagram with five to eight boxes, not twenty.
5. **Deep dive.** Pick the one or two parts that are actually hard and go deep there.
6. **Failure and trade-offs.** What happens when a node dies, a queue backs up, or a hot key appears.
7. **In production.** How real systems of this kind are built, and where they differ from the interview answer.

## Start here

| Read | Why |
|---|---|
| [`fundamentals/cheatsheet.md`](fundamentals/cheatsheet.md) | Latency numbers, CAP, consistency, caching, sharding, queues in one page |
| [`TEMPLATE.md`](TEMPLATE.md) | The skeleton to copy for every new design |

## Worked designs

| Design | Core problem it teaches |
|---|---|
| [URL shortener](designs/url-shortener.md) | ID generation, read-heavy caching, redirects |
| [Rate limiter](designs/rate-limiter.md) | Algorithms, shared counters, races |
| [News feed](designs/news-feed.md) | Fan-out on write vs read, celebrity problem |
| [Chat system](designs/chat-system.md) | Persistent connections, ordering, delivery guarantees |
| [Notification system](designs/notification-system.md) | Queues, retries, idempotency, user preferences |

## Runnable code

Design is easier to trust once some of it runs. [`implementations/`](implementations/) holds small Java
versions of the parts that are mostly algorithm: a token-bucket rate limiter and a Base62 ID encoder.

## Suggested pace

Two sessions a week. One to study a design, one to redo it on a blank page in 45 minutes without
looking. Add a new design to `designs/` every time you finish one, using the template.

Next up: distributed cache, web crawler, search autocomplete, video streaming, ride matching,
payment system, distributed lock, metrics and monitoring.

## Books and references worth the time

- *Designing Data-Intensive Applications*, Martin Kleppmann. Chapters 1 to 3 and 5 to 7 first.
- *System Design Interview* volumes 1 and 2, Alex Xu.
- Engineering blogs of companies that run these systems at scale. Read them for the numbers and the mistakes.
