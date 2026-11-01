#!/usr/bin/env python3
"""DSA tracker for this repo. Standard library only.

  python tracker/track.py today              what to solve today + reviews due
  python tracker/track.py new 217            create the Java file with a header
  python tracker/track.py done 217 -m "..."  mark solved, schedule reviews, commit, push
  python tracker/track.py review 217         log a spaced-repetition review as done
  python tracker/track.py sync               pick up solutions added by hand
  python tracker/track.py plan --start 2026-11-02 --per-day 2
  python tracker/track.py readme             rebuild README.md and the Notion CSV
"""
import argparse
import csv
import datetime as dt
import json
import re
import subprocess
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).parent))
from catalog import load  # noqa: E402

ROOT = Path(__file__).resolve().parent.parent
STATE = ROOT / "tracker" / "progress.json"
REVIEW_GAPS = (3, 7, 21)
TODAY = dt.date.today()


# ---------- state ----------
def read_state():
    if STATE.exists():
        return json.loads(STATE.read_text())
    return {"config": {"start": TODAY.isoformat(), "per_day": 2}, "solved": {}}


def write_state(state):
    STATE.write_text(json.dumps(state, indent=2, sort_keys=True) + "\n")


def git(*args, check=True):
    return subprocess.run(["git", *args], cwd=ROOT, capture_output=True, text=True, check=check).stdout.strip()


def repo_url():
    url = git("remote", "get-url", "origin", check=False)
    url = re.sub(r"\.git$", "", url)
    return url.replace("git@github.com:", "https://github.com/") or "https://github.com/reddypranith80-max/leetcode-solutions"


# ---------- schedule ----------
def planned_date(state, order):
    """Nth core problem -> calendar date, per_day a day, Sundays off."""
    cfg = state["config"]
    day = dt.date.fromisoformat(cfg["start"])
    for _ in range((order - 1) // cfg["per_day"]):
        day += dt.timedelta(days=1)
        if day.weekday() == 6:
            day += dt.timedelta(days=1)
    if day.weekday() == 6:
        day += dt.timedelta(days=1)
    return day


def find_solution(num):
    for p in sorted(ROOT.rglob(f"{num}.*.java")):
        if "tracker" not in p.parts and ".git" not in p.parts:
            return p
    return None


def first_commit_date(path):
    out = git("log", "--diff-filter=A", "--format=%as", "--", str(path.relative_to(ROOT)), check=False)
    return out.splitlines()[-1] if out else TODAY.isoformat()


def sync(state, problems):
    by_num = {p["num"]: p for p in problems}
    added = []
    for f in ROOT.rglob("*.java"):
        if "tracker" in f.parts or ".git" in f.parts:
            continue
        m = re.match(r"(\d+)\.", f.name)
        if m and int(m.group(1)) in by_num and m.group(1) not in state["solved"]:
            state["solved"][m.group(1)] = {"date": first_commit_date(f), "reviews": []}
            added.append(int(m.group(1)))
    return added


def review_dates(entry):
    d = dt.date.fromisoformat(entry["date"])
    return [d + dt.timedelta(days=g) for g in REVIEW_GAPS]


# ---------- commands ----------
def cmd_new(args, state, problems):
    p = next((x for x in problems if x["num"] == args.num), None)
    if not p:
        sys.exit(f"#{args.num} is not in the catalog")
    existing = find_solution(p["num"])
    if existing:
        sys.exit(f"already exists: {existing.relative_to(ROOT)}")
    safe = re.sub(r"[^A-Za-z0-9]+", "_", p["title"]).strip("_")
    path = ROOT / p["folder"] / f"{p['num']}.{safe}.java"
    path.parent.mkdir(exist_ok=True)
    path.write_text(f"""/*
 * {p['num']}. {p['title']}  [{p['difficulty']}]
 * {p['link']}
 *
 * Task    : {p['summary']}
 * Pattern : {p['pattern']}
 *
 * Brute force:
 *
 * Better idea:
 *
 * Time: O(?)    Space: O(?)
 */
import java.util.*;

class Solution {{

}}
""")
    print(f"created {path.relative_to(ROOT)}")
    print(f"open the problem: {p['link']}")


def cmd_done(args, state, problems):
    p = next((x for x in problems if x["num"] == args.num), None)
    f = find_solution(args.num)
    if not p or not f:
        sys.exit("no catalog entry or no solution file; run `new` first")
    state["solved"][str(args.num)] = {"date": TODAY.isoformat(), "reviews": []}
    write_state(state)
    rebuild(state, problems)
    msg = f"Solve #{p['num']} {p['title']}" + (f" - {args.m}" if args.m else "")
    git("add", "-A")
    git("commit", "-m", msg)
    print(f"committed: {msg}")
    if args.no_push:
        print("skipped push (--no-push)")
    else:
        print(git("push") or "pushed")
    due = ", ".join(d.isoformat() for d in review_dates(state["solved"][str(args.num)]))
    print(f"re-solve from scratch on: {due}")


def cmd_review(args, state, problems):
    e = state["solved"].get(str(args.num))
    if not e:
        sys.exit("not solved yet")
    e["reviews"].append(TODAY.isoformat())
    write_state(state)
    print(f"review logged for #{args.num} ({len(e['reviews'])}/{len(REVIEW_GAPS)})")


def cmd_today(args, state, problems):
    core = [p for p in problems if p["core"]]
    todo = [p for p in core if str(p["num"]) not in state["solved"] and planned_date(state, p["order"]) <= TODAY]
    print(f"== Solve ({len(todo)} on or behind plan, showing 5) ==")
    for p in todo[:5]:
        print(f"  #{p['num']:<5} {p['title']} [{p['difficulty']}]  {p['link']}")
    print("== Re-solve from scratch ==")
    n = 0
    for num, e in state["solved"].items():
        dates = review_dates(e)
        if len(e["reviews"]) < len(dates) and dates[len(e["reviews"])] <= TODAY:
            p = next(x for x in problems if x["num"] == int(num))
            print(f"  #{p['num']:<5} {p['title']}  (review {len(e['reviews']) + 1}/3)")
            n += 1
    if not n:
        print("  nothing due")


def cmd_plan(args, state, problems):
    if args.start:
        dt.date.fromisoformat(args.start)
        state["config"]["start"] = args.start
    if args.per_day:
        state["config"]["per_day"] = args.per_day
    write_state(state)
    core = [p for p in problems if p["core"]]
    print(f"{len(core)} problems, finish around {planned_date(state, len(core))}")
    rebuild(state, problems)


# ---------- outputs ----------
def bar(done, total, width=12):
    filled = round(width * done / total) if total else 0
    return "▰" * filled + "▱" * (width - filled)


def rebuild(state, problems):
    write_notion_csv(state, problems)
    write_readme(state, problems)


def write_notion_csv(state, problems):
    base = repo_url() + "/blob/HEAD/"
    out = ROOT / "tracker" / "notion_import.csv"
    with out.open("w", newline="", encoding="utf-8") as fh:
        w = csv.writer(fh)
        w.writerow(["Name", "LC #", "Difficulty", "Topic", "Pattern", "Statement", "Link", "Premium",
                    "Status", "Planned", "Solved", "Review 1", "Review 2", "Review 3", "Solution"])
        for p in problems:
            e = state["solved"].get(str(p["num"]))
            sol = find_solution(p["num"])
            revs = [d.isoformat() for d in review_dates(e)] if e else ["", "", ""]
            w.writerow([p["title"], p["num"], p["difficulty"], p["topic"], p["pattern"], p["summary"], p["link"],
                        "Yes" if p["premium"] else "", "Solved" if e else "Todo",
                        planned_date(state, p["order"]).isoformat() if p["core"] else "",
                        e["date"] if e else "", *revs,
                        base + sol.relative_to(ROOT).as_posix() if sol else ""])


def write_readme(state, problems):
    core = [p for p in problems if p["core"]]
    solved = state["solved"]
    n_core = sum(1 for p in core if str(p["num"]) in solved)
    by_diff = {d: [0, 0] for d in ("Easy", "Medium", "Hard")}
    for p in problems:
        by_diff[p["difficulty"]][1] += 1
        if str(p["num"]) in solved:
            by_diff[p["difficulty"]][0] += 1
    total_solved = len(solved)

    rows = []
    for topic in dict.fromkeys(p["topic"] for p in core):
        ps = [p for p in core if p["topic"] == topic]
        d = sum(1 for p in ps if str(p["num"]) in solved)
        folder = ps[0]["folder"]
        rows.append(f"| [{topic}]({folder}/) | {bar(d, len(ps))} | {d} / {len(ps)} |")

    recent = sorted(((e["date"], int(n)) for n, e in solved.items()), reverse=True)[:8]
    base = repo_url() + "/blob/HEAD/"
    recent_rows = []
    for date, num in recent:
        p = next(x for x in problems if x["num"] == num)
        sol = find_solution(num)
        link = f"[{p['title']}]({sol.relative_to(ROOT).as_posix()})" if sol else p["title"]
        recent_rows.append(f"| {date} | {num} | {link} | {p['difficulty']} |")

    text = (ROOT / "tracker" / "README.template.md").read_text(encoding="utf-8")
    for key, val in {
        "{{TOTAL}}": str(total_solved),
        "{{CORE_DONE}}": str(n_core),
        "{{CORE_TOTAL}}": str(len(core)),
        "{{EASY}}": f"{by_diff['Easy'][0]}/{by_diff['Easy'][1]}",
        "{{MEDIUM}}": f"{by_diff['Medium'][0]}/{by_diff['Medium'][1]}",
        "{{HARD}}": f"{by_diff['Hard'][0]}/{by_diff['Hard'][1]}",
        "{{CORE_BAR}}": bar(n_core, len(core), 20),
        "{{TOPIC_ROWS}}": "\n".join(rows),
        "{{RECENT_ROWS}}": "\n".join(recent_rows) or "| - | - | nothing yet | - |",
        "{{UPDATED}}": TODAY.isoformat(),
    }.items():
        text = text.replace(key, val)
    (ROOT / "README.md").write_text(text, encoding="utf-8")


def main():
    ap = argparse.ArgumentParser()
    sub = ap.add_subparsers(dest="cmd", required=True)
    for name in ("new", "done", "review"):
        s = sub.add_parser(name)
        s.add_argument("num", type=int)
        if name == "done":
            s.add_argument("-m", help="one-line note on the approach, added to the commit message")
            s.add_argument("--no-push", action="store_true")
    for name in ("today", "sync", "readme"):
        sub.add_parser(name)
    pl = sub.add_parser("plan")
    pl.add_argument("--start")
    pl.add_argument("--per-day", type=int)
    args = ap.parse_args()

    problems = load()
    state = read_state()
    added = sync(state, problems)
    if added:
        print(f"picked up existing solutions: {sorted(added)}")
    write_state(state)

    {"new": cmd_new, "done": cmd_done, "review": cmd_review, "today": cmd_today, "plan": cmd_plan,
     "sync": lambda *a: rebuild(state, problems), "readme": lambda *a: rebuild(state, problems)
     }[args.cmd](args, state, problems)


if __name__ == "__main__":
    main()
