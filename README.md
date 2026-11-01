<div align="center">

# leetcode-solutions

**Java solutions, written to be understood again six months from now.**

![solved](https://img.shields.io/badge/solved-14-2ea44f?style=flat-square)
![roadmap](https://img.shields.io/badge/NeetCode_150-5%2F150-1f6feb?style=flat-square)
![easy](https://img.shields.io/badge/easy-8%2F33-3fb950?style=flat-square)
![medium](https://img.shields.io/badge/medium-6%2F105-d29922?style=flat-square)
![hard](https://img.shields.io/badge/hard-0%2F21-f85149?style=flat-square)
![language](https://img.shields.io/badge/java-b07219?style=flat-square)

[Roadmap](#roadmap) · [How a problem gets solved](#how-a-problem-gets-solved-here) · [System design](system-design/) · [Build projects](projects/)

</div>

---

## Why this repo exists

Solving a problem once proves very little. Being able to solve it again, cold, in front of an
interviewer, proves something. So every file here follows the same loop: read, attempt, compare
approaches, write down why the better one works, and come back to it after 3, 7 and 21 days.

The commit history is part of the notes. Each message is a short explanation of the idea behind the
solution, not just "added file".

## Roadmap

The core list is NeetCode 150, ordered so each topic builds on the one before it.
Progress bars update whenever a problem is marked done.

`▰▱▱▱▱▱▱▱▱▱▱▱▱▱▱▱▱▱▱▱`  **5 of 150**

| Topic | Progress | Solved |
|---|---|---|
| [Arrays & Hashing](Arrays/) | ▰▰▰▰▱▱▱▱▱▱▱▱ | 3 / 9 |
| Two Pointers | ▰▰▰▰▰▱▱▱▱▱▱▱ | 2 / 5 |
| Sliding Window | ▱▱▱▱▱▱▱▱▱▱▱▱ | 0 / 6 |
| Stack | ▱▱▱▱▱▱▱▱▱▱▱▱ | 0 / 7 |
| Binary Search | ▱▱▱▱▱▱▱▱▱▱▱▱ | 0 / 7 |
| Linked List | ▱▱▱▱▱▱▱▱▱▱▱▱ | 0 / 11 |
| Trees | ▱▱▱▱▱▱▱▱▱▱▱▱ | 0 / 15 |
| Tries | ▱▱▱▱▱▱▱▱▱▱▱▱ | 0 / 3 |
| Heap / Priority Queue | ▱▱▱▱▱▱▱▱▱▱▱▱ | 0 / 7 |
| Backtracking | ▱▱▱▱▱▱▱▱▱▱▱▱ | 0 / 9 |
| Graphs | ▱▱▱▱▱▱▱▱▱▱▱▱ | 0 / 13 |
| Advanced Graphs | ▱▱▱▱▱▱▱▱▱▱▱▱ | 0 / 6 |
| 1-D DP | ▱▱▱▱▱▱▱▱▱▱▱▱ | 0 / 12 |
| 2-D DP | ▱▱▱▱▱▱▱▱▱▱▱▱ | 0 / 11 |
| Greedy | ▱▱▱▱▱▱▱▱▱▱▱▱ | 0 / 8 |
| Intervals | ▱▱▱▱▱▱▱▱▱▱▱▱ | 0 / 6 |
| Math & Geometry | ▱▱▱▱▱▱▱▱▱▱▱▱ | 0 / 8 |
| Bit Manipulation | ▱▱▱▱▱▱▱▱▱▱▱▱ | 0 / 7 |

Every problem in the list, with its link, a plain-words statement, difficulty and planned date, is in
[`tracker/notion_import.csv`](tracker/notion_import.csv). It drops straight into a Notion database.

## Latest solutions

| Date | # | Problem | Level |
|---|---|---|---|
| 2026-07-01 | 75 | [Sort Colors](Arrays/75.Sort_Color.java) | Medium |
| 2026-06-29 | 1846 | [Maximum Element After Decreasing and Rearranging](Arrays/1846.Maximum_Elements_after_Decreasing_and_Rearranging.java) | Medium |
| 2026-06-28 | 16 | [3Sum Closest](Arrays/16.3Sum_Closest.java) | Medium |
| 2026-06-28 | 15 | [3Sum](Arrays/15.3Sum.java) | Medium |
| 2026-06-27 | 27 | [Remove Element](Arrays/27.Remove_Element.java) | Easy |
| 2026-06-26 | 977 | [Squares of a Sorted Array](Arrays/977.Squares_of_sorted_array.java) | Easy |
| 2026-06-26 | 88 | [Merge Sorted Array](Arrays/88.Merge_Two_Sorted_Arrays.java) | Easy |
| 2026-06-26 | 80 | [Remove Duplicates from Sorted Array II](Arrays/80.Remove_Duplicates_From_Sorted_Array_2.java) | Medium |

## How a problem gets solved here

1. **Read it, then wait.** Restate the task in one sentence and list the edge cases (empty, one item, duplicates, negatives, overflow).
2. **Brute force first.** Write the obvious version and its complexity. It is the baseline the better answer has to beat.
3. **Name the pattern.** The header comment of each file records which signal in the problem pointed to the technique.
4. **Code, then dry-run.** Walk a small example by hand before submitting.
5. **State the cost.** Time and space, in the header, every time.
6. **Return later.** Re-solve from a blank file after 3, 7 and 21 days. A problem only counts as learned once that passes.

## Using the tracker

Everything is plain Python 3 with no dependencies.

```bash
python tracker/track.py today              # what is planned today, and which reviews are due
python tracker/track.py new 217            # creates Arrays/217.Contains_Duplicate.java with a header
python tracker/track.py done 217 -m "hash set, early exit on repeat"
python tracker/track.py review 217         # log a spaced-repetition review
python tracker/track.py plan --start 2026-11-02 --per-day 2
```

`done` marks the problem solved, schedules the three reviews, regenerates this README and the Notion
CSV, then commits as `Solve #217 Contains Duplicate - ...` and pushes.

## Layout

```
Arrays/  Two_Pointers/  Sliding_Window/  Stack/  Binary_Search/  Linked_List/  Trees/
Tries/  Heap/  Backtracking/  Graphs/  Advanced_Graphs/  DP_1D/  DP_2D/  Greedy/
Intervals/  Math_Geometry/  Bit_Manipulation/  Strings/
system-design/   fundamentals, a design template, worked designs with real-world notes
projects/        product ideas, architecture and a plan to ship and measure them
tracker/         catalog of problems, scheduler, README and Notion export
```

Files are named `<leetcode number>.<Title_With_Underscores>.java`.

<sub>Last updated 2026-11-01. The tables above are generated by `tracker/track.py`; edit the template in `tracker/README.template.md`, not this file.</sub>
