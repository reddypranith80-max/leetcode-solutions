import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * DSA tracker for this repo. Needs only a JDK (11 or newer), run from anywhere inside the repo:
 *
 *   java tracker/Tracker.java today                what to solve today + reviews due
 *   java tracker/Tracker.java new 217              create the Java file with a header
 *   java tracker/Tracker.java done 217 -m "idea"   mark solved, schedule reviews, commit, push
 *   java tracker/Tracker.java review 217           log a spaced-repetition review
 *   java tracker/Tracker.java plan --start 2026-11-02 --per-day 2
 *   java tracker/Tracker.java sync                 pick up solutions added by hand
 *   java tracker/Tracker.java readme               rebuild README.md and the Notion CSV
 *
 * Data files in tracker/: problems.txt (catalog), progress.tsv (what is solved), config.properties.
 */
public class Tracker {

    record Problem(int num, String title, String slug, String difficulty, boolean premium, String topic,
                   String folder, String pattern, String summary, boolean core, int order) {
        String link() {
            return "https://leetcode.com/problems/" + slug + "/";
        }
    }

    record Solved(LocalDate date, List<LocalDate> reviews) {
    }

    static final int[] REVIEW_GAPS = {3, 7, 21};
    static final LocalDate TODAY = LocalDate.now();

    static Path root;
    static Path trackerDir;
    static List<Problem> problems;
    static Map<Integer, Solved> solved = new TreeMap<>();
    static LocalDate planStart = TODAY;
    static int perDay = 2;

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("commands: today | new <n> | done <n> [-m note] [--no-push] | review <n> | plan | sync | readme");
            return;
        }
        root = findRoot();
        trackerDir = root.resolve("tracker");
        problems = loadCatalog();
        loadState();

        List<Integer> added = sync();
        if (!added.isEmpty()) System.out.println("picked up existing solutions: " + added);
        saveState();

        switch (args[0]) {
            case "new" -> cmdNew(Integer.parseInt(args[1]));
            case "done" -> cmdDone(args);
            case "review" -> cmdReview(Integer.parseInt(args[1]));
            case "today" -> cmdToday();
            case "plan" -> cmdPlan(args);
            case "sync", "readme" -> rebuild();
            default -> System.out.println("unknown command: " + args[0]);
        }
    }

    // ---------- setup ----------

    static Path findRoot() {
        Path p = Path.of("").toAbsolutePath();
        while (p != null && !Files.exists(p.resolve(".git"))) p = p.getParent();
        if (p == null) throw new IllegalStateException("run this from inside the repository");
        return p;
    }

    static List<Problem> loadCatalog() throws IOException {
        List<Problem> list = new ArrayList<>();
        String topic = null, folder = null, pattern = null;
        int order = 0;
        for (String raw : Files.readAllLines(trackerDir.resolve("problems.txt"), StandardCharsets.UTF_8)) {
            String line = raw.strip();
            if (line.isEmpty() || line.startsWith("# ")) continue;
            if (line.startsWith("## ")) {
                String[] t = line.substring(3).split("\\|", 3);
                topic = t[0];
                folder = t[1];
                pattern = t[2];
                continue;
            }
            String[] f = line.split("\\|", 5);
            boolean premium = f[3].endsWith("$");
            String d = f[3].replace("$", "");
            String difficulty = switch (d) {
                case "E" -> "Easy";
                case "M" -> "Medium";
                default -> "Hard";
            };
            boolean core = !topic.equals("Bonus");
            list.add(new Problem(Integer.parseInt(f[0]), f[1], f[2], difficulty, premium, topic, folder,
                    pattern, f[4], core, core ? ++order : 0));
        }
        return list;
    }

    static void loadState() throws IOException {
        Path cfg = trackerDir.resolve("config.properties");
        if (Files.exists(cfg)) {
            Properties p = new Properties();
            try (var in = Files.newInputStream(cfg)) {
                p.load(in);
            }
            planStart = LocalDate.parse(p.getProperty("start", TODAY.toString()));
            perDay = Integer.parseInt(p.getProperty("perDay", "2"));
        }
        Path tsv = trackerDir.resolve("progress.tsv");
        if (Files.exists(tsv)) {
            for (String line : Files.readAllLines(tsv, StandardCharsets.UTF_8)) {
                if (line.isBlank()) continue;
                String[] f = line.split("\t", -1);
                List<LocalDate> reviews = new ArrayList<>();
                if (f.length > 2 && !f[2].isBlank()) for (String r : f[2].split(",")) reviews.add(LocalDate.parse(r));
                solved.put(Integer.parseInt(f[0]), new Solved(LocalDate.parse(f[1]), reviews));
            }
        }
    }

    static void saveState() throws IOException {
        Properties p = new Properties();
        p.setProperty("start", planStart.toString());
        p.setProperty("perDay", String.valueOf(perDay));
        try (var out = Files.newOutputStream(trackerDir.resolve("config.properties"))) {
            p.store(out, "plan settings");
        }
        String body = solved.entrySet().stream()
                .map(e -> e.getKey() + "\t" + e.getValue().date() + "\t"
                        + e.getValue().reviews().stream().map(LocalDate::toString).collect(Collectors.joining(",")))
                .collect(Collectors.joining("\n"));
        Files.writeString(trackerDir.resolve("progress.tsv"), body.isEmpty() ? "" : body + "\n", StandardCharsets.UTF_8);
    }

    // ---------- git and files ----------

    static String git(String... args) throws IOException, InterruptedException {
        List<String> cmd = new ArrayList<>(List.of("git"));
        cmd.addAll(List.of(args));
        Process proc = new ProcessBuilder(cmd).directory(root.toFile()).redirectErrorStream(true).start();
        String out = new String(proc.getInputStream().readAllBytes(), StandardCharsets.UTF_8).strip();
        if (proc.waitFor() != 0) throw new IOException("git " + String.join(" ", args) + " failed:\n" + out);
        return out;
    }

    static String repoUrl() {
        try {
            String url = git("remote", "get-url", "origin").replaceAll("\\.git$", "");
            return url.replace("git@github.com:", "https://github.com/");
        } catch (Exception e) {
            return "https://github.com/reddypranith80-max/leetcode-solutions";
        }
    }

    static List<Path> javaFiles() throws IOException {
        try (Stream<Path> s = Files.walk(root)) {
            return s.filter(p -> p.toString().endsWith(".java"))
                    .filter(p -> {
                        Path rel = root.relativize(p);
                        String first = rel.getName(0).toString();
                        return !first.equals(".git") && !first.equals("tracker") && !first.equals("system-design");
                    })
                    .sorted().collect(Collectors.toList());
        }
    }

    static Path findSolution(int num) throws IOException {
        for (Path p : javaFiles()) if (p.getFileName().toString().startsWith(num + ".")) return p;
        return null;
    }

    static LocalDate firstCommitDate(Path file) {
        try {
            String out = git("log", "--diff-filter=A", "--format=%as", "--", root.relativize(file).toString().replace('\\', '/'));
            if (out.isEmpty()) return TODAY;
            String[] lines = out.split("\n");
            return LocalDate.parse(lines[lines.length - 1].strip());
        } catch (Exception e) {
            return TODAY;
        }
    }

    static List<Integer> sync() throws IOException {
        Map<Integer, Problem> byNum = new LinkedHashMap<>();
        for (Problem p : problems) byNum.put(p.num(), p);
        Pattern numbered = Pattern.compile("^(\\d+)\\.");
        List<Integer> added = new ArrayList<>();
        for (Path f : javaFiles()) {
            Matcher m = numbered.matcher(f.getFileName().toString());
            if (!m.find()) continue;
            int n = Integer.parseInt(m.group(1));
            if (byNum.containsKey(n) && !solved.containsKey(n)) {
                solved.put(n, new Solved(firstCommitDate(f), new ArrayList<>()));
                added.add(n);
            }
        }
        return added;
    }

    // ---------- schedule ----------

    static LocalDate plannedDate(int order) {
        LocalDate day = planStart;
        for (int i = 0; i < (order - 1) / perDay; i++) {
            day = day.plusDays(1);
            if (day.getDayOfWeek() == DayOfWeek.SUNDAY) day = day.plusDays(1);
        }
        if (day.getDayOfWeek() == DayOfWeek.SUNDAY) day = day.plusDays(1);
        return day;
    }

    static List<LocalDate> reviewDates(Solved s) {
        List<LocalDate> out = new ArrayList<>();
        for (int g : REVIEW_GAPS) out.add(s.date().plusDays(g));
        return out;
    }

    static Problem byNum(int n) {
        return problems.stream().filter(p -> p.num() == n).findFirst().orElse(null);
    }

    // ---------- commands ----------

    static void cmdNew(int n) throws IOException {
        Problem p = byNum(n);
        if (p == null) {
            System.out.println("#" + n + " is not in the catalog");
            return;
        }
        Path existing = findSolution(n);
        if (existing != null) {
            System.out.println("already exists: " + root.relativize(existing));
            return;
        }
        String safe = p.title().replaceAll("[^A-Za-z0-9]+", "_").replaceAll("^_|_$", "");
        Path path = root.resolve(p.folder()).resolve(p.num() + "." + safe + ".java");
        Files.createDirectories(path.getParent());
        Files.writeString(path, """
                /*
                 * %d. %s  [%s]
                 * %s
                 *
                 * Task    : %s
                 * Pattern : %s
                 *
                 * Brute force:
                 *
                 * Better idea:
                 *
                 * Time: O(?)    Space: O(?)
                 */
                import java.util.*;

                class Solution {

                }
                """.formatted(p.num(), p.title(), p.difficulty(), p.link(), p.summary(), p.pattern()));
        System.out.println("created " + root.relativize(path));
        System.out.println("open the problem: " + p.link());
    }

    static void cmdDone(String[] args) throws Exception {
        int n = Integer.parseInt(args[1]);
        String note = null;
        boolean push = true;
        for (int i = 2; i < args.length; i++) {
            if (args[i].equals("-m") && i + 1 < args.length) note = args[++i];
            else if (args[i].equals("--no-push")) push = false;
        }
        Problem p = byNum(n);
        if (p == null || findSolution(n) == null) {
            System.out.println("no catalog entry or no solution file; run `new` first");
            return;
        }
        solved.put(n, new Solved(TODAY, new ArrayList<>()));
        saveState();
        rebuild();
        String msg = "Solve #" + p.num() + " " + p.title() + (note == null ? "" : " - " + note);
        git("add", "-A");
        git("commit", "-m", msg);
        System.out.println("committed: " + msg);
        if (push) {
            git("push");
            System.out.println("pushed");
        } else {
            System.out.println("skipped push (--no-push)");
        }
        System.out.println("re-solve from scratch on: " + reviewDates(solved.get(n)));
    }

    static void cmdReview(int n) throws IOException {
        Solved s = solved.get(n);
        if (s == null) {
            System.out.println("not solved yet");
            return;
        }
        s.reviews().add(TODAY);
        saveState();
        System.out.println("review logged for #" + n + " (" + s.reviews().size() + "/" + REVIEW_GAPS.length + ")");
    }

    static void cmdToday() {
        List<Problem> todo = problems.stream()
                .filter(p -> p.core() && !solved.containsKey(p.num()) && !plannedDate(p.order()).isAfter(TODAY))
                .collect(Collectors.toList());
        System.out.println("== Solve (" + todo.size() + " on or behind plan, showing 5) ==");
        todo.stream().limit(5).forEach(p ->
                System.out.printf("  #%-5d %s [%s]  %s%n", p.num(), p.title(), p.difficulty(), p.link()));
        System.out.println("== Re-solve from scratch ==");
        int due = 0;
        for (var e : solved.entrySet()) {
            List<LocalDate> dates = reviewDates(e.getValue());
            int done = e.getValue().reviews().size();
            if (done < dates.size() && !dates.get(done).isAfter(TODAY)) {
                System.out.printf("  #%-5d %s  (review %d/%d)%n", e.getKey(), byNum(e.getKey()).title(), done + 1, dates.size());
                due++;
            }
        }
        if (due == 0) System.out.println("  nothing due");
    }

    static void cmdPlan(String[] args) throws IOException {
        for (int i = 1; i < args.length - 1; i++) {
            if (args[i].equals("--start")) planStart = LocalDate.parse(args[i + 1]);
            if (args[i].equals("--per-day")) perDay = Integer.parseInt(args[i + 1]);
        }
        saveState();
        long core = problems.stream().filter(Problem::core).count();
        System.out.println(core + " problems, finish around " + plannedDate((int) core));
        rebuild();
    }

    // ---------- outputs ----------

    static void rebuild() throws IOException {
        writeNotionCsv();
        writeReadme();
    }

    static String bar(int done, int total, int width) {
        int filled = total == 0 ? 0 : Math.round((float) width * done / total);
        return "▰".repeat(filled) + "▱".repeat(width - filled);
    }

    static String csv(Object v) {
        String s = String.valueOf(v);
        return s.contains(",") || s.contains("\"") ? "\"" + s.replace("\"", "\"\"") + "\"" : s;
    }

    static void writeNotionCsv() throws IOException {
        String base = repoUrl() + "/blob/HEAD/";
        StringBuilder sb = new StringBuilder("Name,LC #,Difficulty,Topic,Pattern,Statement,Link,Premium,Status,Planned,"
                + "Solved,Review 1,Review 2,Review 3,Solution\n");
        for (Problem p : problems) {
            Solved s = solved.get(p.num());
            Path sol = findSolution(p.num());
            List<String> revs = new ArrayList<>(List.of("", "", ""));
            if (s != null) {
                revs.clear();
                reviewDates(s).forEach(d -> revs.add(d.toString()));
            }
            List<Object> row = new ArrayList<>(List.of(p.title(), p.num(), p.difficulty(), p.topic(), p.pattern(),
                    p.summary(), p.link(), p.premium() ? "Yes" : "", s != null ? "Solved" : "Todo",
                    p.core() ? plannedDate(p.order()).toString() : "", s != null ? s.date().toString() : ""));
            row.addAll(revs);
            row.add(sol != null ? base + root.relativize(sol).toString().replace('\\', '/') : "");
            sb.append(row.stream().map(Tracker::csv).collect(Collectors.joining(","))).append('\n');
        }
        Files.writeString(trackerDir.resolve("notion_import.csv"), sb.toString(), StandardCharsets.UTF_8);
    }

    static void writeReadme() throws IOException {
        List<Problem> core = problems.stream().filter(Problem::core).toList();
        int coreDone = (int) core.stream().filter(p -> solved.containsKey(p.num())).count();

        Map<String, int[]> diff = new LinkedHashMap<>();
        for (String d : List.of("Easy", "Medium", "Hard")) diff.put(d, new int[2]);
        for (Problem p : problems) {
            diff.get(p.difficulty())[1]++;
            if (solved.containsKey(p.num())) diff.get(p.difficulty())[0]++;
        }

        List<String> rows = new ArrayList<>();
        for (String topic : new LinkedHashSet<>(core.stream().map(Problem::topic).toList())) {
            List<Problem> ps = core.stream().filter(p -> p.topic().equals(topic)).toList();
            int d = (int) ps.stream().filter(p -> solved.containsKey(p.num())).count();
            String folder = ps.get(0).folder();
            String name = Files.isDirectory(root.resolve(folder)) ? "[" + topic + "](" + folder + "/)" : topic;
            rows.add("| " + name + " | " + bar(d, ps.size(), 12) + " | " + d + " / " + ps.size() + " |");
        }

        List<String> recent = new ArrayList<>();
        solved.entrySet().stream()
                .sorted((a, b) -> b.getValue().date().compareTo(a.getValue().date()))
                .limit(8)
                .forEach(e -> {
                    try {
                        Problem p = byNum(e.getKey());
                        Path sol = findSolution(p.num());
                        String title = sol == null ? p.title()
                                : "[" + p.title() + "](" + root.relativize(sol).toString().replace('\\', '/') + ")";
                        recent.add("| " + e.getValue().date() + " | " + p.num() + " | " + title + " | " + p.difficulty() + " |");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });

        String text = Files.readString(trackerDir.resolve("README.template.md"), StandardCharsets.UTF_8);
        Map<String, String> vars = new LinkedHashMap<>();
        vars.put("{{TOTAL}}", String.valueOf(solved.size()));
        vars.put("{{CORE_DONE}}", String.valueOf(coreDone));
        vars.put("{{CORE_TOTAL}}", String.valueOf(core.size()));
        vars.put("{{EASY}}", diff.get("Easy")[0] + "%2F" + diff.get("Easy")[1]);
        vars.put("{{MEDIUM}}", diff.get("Medium")[0] + "%2F" + diff.get("Medium")[1]);
        vars.put("{{HARD}}", diff.get("Hard")[0] + "%2F" + diff.get("Hard")[1]);
        vars.put("{{CORE_BAR}}", bar(coreDone, core.size(), 20));
        vars.put("{{TOPIC_ROWS}}", String.join("\n", rows));
        vars.put("{{RECENT_ROWS}}", recent.isEmpty() ? "| - | - | nothing yet | - |" : String.join("\n", recent));
        vars.put("{{UPDATED}}", TODAY.toString());
        for (var e : vars.entrySet()) text = text.replace(e.getKey(), e.getValue());
        Files.writeString(root.resolve("README.md"), text, StandardCharsets.UTF_8);
    }
}
