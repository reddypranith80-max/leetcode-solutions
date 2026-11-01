"""Static problem catalog: NeetCode 150 in learning order, plus bonus problems.

Line format:  number | title | leetcode-slug | E/M/H (+ "$" if LeetCode Premium) | summary
Summaries are written in plain words and are not LeetCode's statements; open the
link for the official wording, constraints and examples.
"""

# topic key -> (folder, pattern to look for)
TOPICS = {
    "Arrays & Hashing": ("Arrays", "Hash map / set, counting, prefix and suffix products"),
    "Two Pointers": ("Two_Pointers", "Converging or same-direction pointers on sorted or linear data"),
    "Sliding Window": ("Sliding_Window", "Expand right, shrink left while a window invariant breaks"),
    "Stack": ("Stack", "LIFO matching, monotonic stack for next-greater style queries"),
    "Binary Search": ("Binary_Search", "Halve a search space, including searching on the answer"),
    "Linked List": ("Linked_List", "Pointer rewiring, dummy head, fast and slow pointers"),
    "Trees": ("Trees", "DFS recursion, BFS by level, return values up the call stack"),
    "Tries": ("Tries", "Prefix tree where each node is one character"),
    "Heap / Priority Queue": ("Heap", "Keep the best k, or repeatedly take the min or max"),
    "Backtracking": ("Backtracking", "Choose, explore, un-choose over a decision tree"),
    "Graphs": ("Graphs", "BFS / DFS on grids and adjacency lists, topological order, union-find"),
    "Advanced Graphs": ("Advanced_Graphs", "Dijkstra, MST, Bellman-Ford, Euler paths"),
    "1-D DP": ("DP_1D", "State is one index; build from smaller prefixes"),
    "2-D DP": ("DP_2D", "State is two indices or index plus a budget"),
    "Greedy": ("Greedy", "Local best choice that provably stays optimal"),
    "Intervals": ("Intervals", "Sort by start or end, sweep and merge"),
    "Math & Geometry": ("Math_Geometry", "Matrix manipulation, number tricks, fast power"),
    "Bit Manipulation": ("Bit_Manipulation", "XOR, masks, shifts"),
    "Bonus": ("Arrays", "Extra practice outside the main list"),
}

_RAW = """
## Arrays & Hashing
217|Contains Duplicate|contains-duplicate|E|Say whether any value appears more than once in an array.
242|Valid Anagram|valid-anagram|E|Check if one string is a rearrangement of another.
1|Two Sum|two-sum|E|Find two indices whose values add up to a target.
49|Group Anagrams|group-anagrams|M|Bucket words that are rearrangements of each other.
347|Top K Frequent Elements|top-k-frequent-elements|M|Return the k values that occur most often.
238|Product of Array Except Self|product-of-array-except-self|M|For each position, the product of all other elements, without division.
36|Valid Sudoku|valid-sudoku|M|Check a partly filled 9x9 board for row, column and box repeats.
271|Encode and Decode Strings|encode-and-decode-strings|M$|Design a format that turns a list of strings into one string and back.
128|Longest Consecutive Sequence|longest-consecutive-sequence|M|Longest run of consecutive integers in an unsorted array, in O(n).
## Two Pointers
125|Valid Palindrome|valid-palindrome|E|Palindrome check that ignores case and non-alphanumerics.
167|Two Sum II - Input Array Is Sorted|two-sum-ii-input-array-is-sorted|M|Two Sum on a sorted array using only constant extra space.
15|3Sum|3sum|M|All unique triplets that add up to zero.
11|Container With Most Water|container-with-most-water|M|Pick two vertical lines that hold the most water between them.
42|Trapping Rain Water|trapping-rain-water|H|Total water trapped between bars of a height map.
## Sliding Window
121|Best Time to Buy and Sell Stock|best-time-to-buy-and-sell-stock|E|Max profit from one buy followed by one later sell.
3|Longest Substring Without Repeating Characters|longest-substring-without-repeating-characters|M|Length of the longest substring with all distinct characters.
424|Longest Repeating Character Replacement|longest-repeating-character-replacement|M|Longest uniform-letter substring after at most k character changes.
567|Permutation in String|permutation-in-string|M|Does s2 contain some rearrangement of s1 as a substring.
76|Minimum Window Substring|minimum-window-substring|H|Smallest window of s that covers every character of t.
239|Sliding Window Maximum|sliding-window-maximum|H|Maximum of every length-k window as it slides across the array.
## Stack
20|Valid Parentheses|valid-parentheses|E|Check that brackets of three kinds are closed in the right order.
155|Min Stack|min-stack|M|Stack that also returns its minimum in constant time.
150|Evaluate Reverse Polish Notation|evaluate-reverse-polish-notation|M|Evaluate an arithmetic expression written in postfix form.
22|Generate Parentheses|generate-parentheses|M|List every valid arrangement of n pairs of brackets.
739|Daily Temperatures|daily-temperatures|M|For each day, how many days until a warmer one.
853|Car Fleet|car-fleet|M|Count the groups of cars that end up travelling together to a destination.
84|Largest Rectangle in Histogram|largest-rectangle-in-histogram|H|Area of the biggest rectangle that fits inside a histogram.
## Binary Search
704|Binary Search|binary-search|E|Find a target in a sorted array in O(log n).
74|Search a 2D Matrix|search-a-2d-matrix|M|Search a row-sorted matrix whose rows also chain in order.
875|Koko Eating Bananas|koko-eating-bananas|M|Slowest eating speed that still finishes all piles within h hours.
153|Find Minimum in Rotated Sorted Array|find-minimum-in-rotated-sorted-array|M|Smallest value in a sorted array that was rotated.
33|Search in Rotated Sorted Array|search-in-rotated-sorted-array|M|Find a target in a rotated sorted array.
981|Time Based Key-Value Store|time-based-key-value-store|M|Key-value store that answers "value of key at or before time t".
4|Median of Two Sorted Arrays|median-of-two-sorted-arrays|H|Median of two sorted arrays in O(log(m+n)).
## Linked List
206|Reverse Linked List|reverse-linked-list|E|Reverse a singly linked list.
21|Merge Two Sorted Lists|merge-two-sorted-lists|E|Splice two sorted lists into one sorted list.
143|Reorder List|reorder-list|M|Interleave the list as first, last, second, second-last and so on.
19|Remove Nth Node From End of List|remove-nth-node-from-end-of-list|M|Delete the nth node counted from the tail in one pass.
138|Copy List with Random Pointer|copy-list-with-random-pointer|M|Deep copy a list where each node has an extra random pointer.
2|Add Two Numbers|add-two-numbers|M|Add two numbers stored as reversed digit lists.
141|Linked List Cycle|linked-list-cycle|E|Detect whether a list loops back on itself.
287|Find the Duplicate Number|find-the-duplicate-number|M|Find the repeated value in n+1 numbers from 1..n without changing the array.
146|LRU Cache|lru-cache|M|Design a fixed-size cache that evicts the least recently used key.
23|Merge k Sorted Lists|merge-k-sorted-lists|H|Merge k sorted lists into one.
25|Reverse Nodes in k-Group|reverse-nodes-in-k-group|H|Reverse the list in blocks of k nodes.
## Trees
226|Invert Binary Tree|invert-binary-tree|E|Mirror a binary tree left to right.
104|Maximum Depth of Binary Tree|maximum-depth-of-binary-tree|E|Number of levels in a binary tree.
543|Diameter of Binary Tree|diameter-of-binary-tree|E|Longest path between any two nodes, counted in edges.
110|Balanced Binary Tree|balanced-binary-tree|E|Check that subtree heights never differ by more than one.
100|Same Tree|same-tree|E|Check whether two trees have identical shape and values.
572|Subtree of Another Tree|subtree-of-another-tree|E|Does one tree appear as a complete subtree inside another.
235|Lowest Common Ancestor of a Binary Search Tree|lowest-common-ancestor-of-a-binary-search-tree|M|Lowest node that has both given nodes below it in a BST.
102|Binary Tree Level Order Traversal|binary-tree-level-order-traversal|M|Visit a tree level by level, left to right.
199|Binary Tree Right Side View|binary-tree-right-side-view|M|The nodes you would see looking at the tree from the right.
1448|Count Good Nodes in Binary Tree|count-good-nodes-in-binary-tree|M|Count nodes not smaller than any ancestor on their root path.
98|Validate Binary Search Tree|validate-binary-search-tree|M|Check the BST rule holds for the whole tree, not just parents.
230|Kth Smallest Element in a BST|kth-smallest-element-in-a-bst|M|The kth smallest value stored in a BST.
105|Construct Binary Tree from Preorder and Inorder Traversal|construct-binary-tree-from-preorder-and-inorder-traversal|M|Rebuild a tree from its preorder and inorder lists.
124|Binary Tree Maximum Path Sum|binary-tree-maximum-path-sum|H|Largest sum over any path, which can bend at any node.
297|Serialize and Deserialize Binary Tree|serialize-and-deserialize-binary-tree|H|Convert a tree to a string and rebuild it exactly.
## Tries
208|Implement Trie (Prefix Tree)|implement-trie-prefix-tree|M|Build a trie with insert, search and prefix-search.
211|Design Add and Search Words Data Structure|design-add-and-search-words-data-structure|M|Word dictionary where searches may contain "." wildcards.
212|Word Search II|word-search-ii|H|Find which words from a list can be traced in a letter grid.
## Heap / Priority Queue
703|Kth Largest Element in a Stream|kth-largest-element-in-a-stream|E|Keep reporting the kth largest as new numbers arrive.
1046|Last Stone Weight|last-stone-weight|E|Repeatedly smash the two heaviest stones and see what remains.
973|K Closest Points to Origin|k-closest-points-to-origin|M|The k points nearest to (0, 0).
215|Kth Largest Element in an Array|kth-largest-element-in-an-array|M|The kth largest value in an unsorted array.
621|Task Scheduler|task-scheduler|M|Least time to run tasks when equal tasks need a cooldown gap.
355|Design Twitter|design-twitter|M|Post, follow, unfollow and fetch the 10 latest items from a feed.
295|Find Median from Data Stream|find-median-from-data-stream|H|Maintain a running median as numbers are added.
## Backtracking
78|Subsets|subsets|M|Every subset of a list of distinct numbers.
39|Combination Sum|combination-sum|M|Combinations that reach a target, reusing numbers freely.
46|Permutations|permutations|M|Every ordering of a list of distinct numbers.
90|Subsets II|subsets-ii|M|Every subset when the input may contain duplicates, without repeats.
40|Combination Sum II|combination-sum-ii|M|Combinations reaching a target, each number used at most once.
79|Word Search|word-search|M|Can a word be traced through adjacent cells of a grid.
131|Palindrome Partitioning|palindrome-partitioning|M|Every way to cut a string into palindromic pieces.
17|Letter Combinations of a Phone Number|letter-combinations-of-a-phone-number|M|All letter strings a digit string could spell on a phone keypad.
51|N-Queens|n-queens|H|Place n queens on an n x n board so none attack each other.
## Graphs
200|Number of Islands|number-of-islands|M|Count connected land regions in a grid.
133|Clone Graph|clone-graph|M|Deep copy an undirected graph.
695|Max Area of Island|max-area-of-island|M|Size of the biggest connected land region.
417|Pacific Atlantic Water Flow|pacific-atlantic-water-flow|M|Cells whose water can reach both oceans.
130|Surrounded Regions|surrounded-regions|M|Flip enclosed O regions to X, leaving border-connected ones.
994|Rotting Oranges|rotting-oranges|M|Minutes until all fresh oranges rot, spreading one step per minute.
286|Walls and Gates|walls-and-gates|M$|Fill each room with its distance to the nearest gate.
207|Course Schedule|course-schedule|M|Can all courses be finished given prerequisite pairs.
210|Course Schedule II|course-schedule-ii|M|A valid order to take all courses, if one exists.
684|Redundant Connection|redundant-connection|M|The extra edge that turns a tree into a graph with a cycle.
323|Number of Connected Components in an Undirected Graph|number-of-connected-components-in-an-undirected-graph|M$|Count separate connected groups in a graph.
261|Graph Valid Tree|graph-valid-tree|M$|Do these edges form a single tree with no cycles.
127|Word Ladder|word-ladder|H|Shortest chain of one-letter changes from one word to another.
## Advanced Graphs
332|Reconstruct Itinerary|reconstruct-itinerary|H|Use every flight ticket once, in smallest lexical order.
1584|Min Cost to Connect All Points|min-cost-to-connect-all-points|M|Cheapest way to link all points by Manhattan distance.
743|Network Delay Time|network-delay-time|M|Time for a signal to reach every node from a start node.
778|Swim in Rising Water|swim-in-rising-water|H|Least time to cross a grid where each cell opens at its own water level.
269|Alien Dictionary|alien-dictionary|H$|Recover letter order of an unknown alphabet from sorted words.
787|Cheapest Flights Within K Stops|cheapest-flights-within-k-stops|M|Cheapest route that uses at most k layovers.
## 1-D DP
70|Climbing Stairs|climbing-stairs|E|Ways to climb n steps taking 1 or 2 at a time.
746|Min Cost Climbing Stairs|min-cost-climbing-stairs|E|Cheapest way to reach the top when each step has a cost.
198|House Robber|house-robber|M|Max loot from a row of houses without hitting two neighbours.
213|House Robber II|house-robber-ii|M|House Robber when the houses form a circle.
5|Longest Palindromic Substring|longest-palindromic-substring|M|Longest substring that reads the same both ways.
647|Palindromic Substrings|palindromic-substrings|M|Count all palindromic substrings of a string.
91|Decode Ways|decode-ways|M|Number of ways to read a digit string as letters A to Z.
322|Coin Change|coin-change|M|Fewest coins needed to make an amount.
152|Maximum Product Subarray|maximum-product-subarray|M|Largest product of a contiguous subarray.
139|Word Break|word-break|M|Can a string be split into words from a dictionary.
300|Longest Increasing Subsequence|longest-increasing-subsequence|M|Length of the longest strictly increasing subsequence.
416|Partition Equal Subset Sum|partition-equal-subset-sum|M|Can the array be split into two subsets with equal sums.
## 2-D DP
62|Unique Paths|unique-paths|M|Paths from top-left to bottom-right moving only right or down.
1143|Longest Common Subsequence|longest-common-subsequence|M|Length of the longest subsequence shared by two strings.
309|Best Time to Buy and Sell Stock with Cooldown|best-time-to-buy-and-sell-stock-with-cooldown|M|Stock trading with unlimited trades but a one-day cooldown after selling.
518|Coin Change II|coin-change-ii|M|Number of coin combinations that make an amount.
494|Target Sum|target-sum|M|Ways to assign + or - to each number to hit a target.
97|Interleaving String|interleaving-string|M|Is s3 a weave of s1 and s2 that keeps each one's order.
329|Longest Increasing Path in a Matrix|longest-increasing-path-in-a-matrix|H|Longest strictly increasing path through a grid.
115|Distinct Subsequences|distinct-subsequences|H|How many times t appears as a subsequence of s.
72|Edit Distance|edit-distance|M|Fewest inserts, deletes or replacements to turn one word into another.
312|Burst Balloons|burst-balloons|H|Maximum coins from bursting all balloons in the best order.
10|Regular Expression Matching|regular-expression-matching|H|Match a string against a pattern with "." and "*".
## Greedy
53|Maximum Subarray|maximum-subarray|M|Largest sum of a contiguous subarray.
55|Jump Game|jump-game|M|Can you reach the last index given max jump lengths.
45|Jump Game II|jump-game-ii|M|Fewest jumps needed to reach the last index.
134|Gas Station|gas-station|M|Find the start station that lets you finish a circular route.
846|Hand of Straights|hand-of-straights|M|Can cards be grouped into runs of consecutive values of a set size.
1899|Merge Triplets to Form Target Triplet|merge-triplets-to-form-target-triplet|M|Can max-merging chosen triplets produce the target triplet.
763|Partition Labels|partition-labels|M|Cut a string so each letter appears in only one part, as many parts as possible.
678|Valid Parenthesis String|valid-parenthesis-string|M|Bracket check where "*" can be "(", ")" or empty.
## Intervals
57|Insert Interval|insert-interval|M|Insert a new interval into a sorted list and merge overlaps.
56|Merge Intervals|merge-intervals|M|Merge all overlapping intervals.
435|Non-overlapping Intervals|non-overlapping-intervals|M|Fewest intervals to remove so the rest do not overlap.
252|Meeting Rooms|meeting-rooms|E$|Can one person attend every meeting.
253|Meeting Rooms II|meeting-rooms-ii|M$|Minimum number of rooms needed for all meetings.
1851|Minimum Interval to Include Each Query|minimum-interval-to-include-each-query|H|For each query point, the size of the smallest interval covering it.
## Math & Geometry
48|Rotate Image|rotate-image|M|Rotate a square matrix 90 degrees in place.
54|Spiral Matrix|spiral-matrix|M|Return matrix elements in spiral order.
73|Set Matrix Zeroes|set-matrix-zeroes|M|If a cell is zero, zero its whole row and column in place.
202|Happy Number|happy-number|E|Repeated digit-square sums: do they reach 1 or loop forever.
66|Plus One|plus-one|E|Add one to a number stored as a digit array.
50|Pow(x, n)|powx-n|M|Compute x to the power n efficiently.
43|Multiply Strings|multiply-strings|M|Multiply two huge numbers given as strings.
2013|Detect Squares|detect-squares|M|Count axis-aligned squares that a new point could complete.
## Bit Manipulation
136|Single Number|single-number|E|The one value that appears once while others appear twice.
191|Number of 1 Bits|number-of-1-bits|E|Count set bits in an integer.
338|Counting Bits|counting-bits|E|Set-bit count for every number from 0 to n.
190|Reverse Bits|reverse-bits|E|Reverse the bits of a 32-bit integer.
268|Missing Number|missing-number|E|The missing value in 0..n.
371|Sum of Two Integers|sum-of-two-integers|M|Add two integers without using + or -.
7|Reverse Integer|reverse-integer|M|Reverse an integer's digits while guarding against 32-bit overflow.
## Bonus
26|Remove Duplicates from Sorted Array|remove-duplicates-from-sorted-array|E|Deduplicate a sorted array in place and return the new length.
27|Remove Element|remove-element|E|Remove all copies of a value in place.
75|Sort Colors|sort-colors|M|Sort an array of 0s, 1s and 2s in one pass.
80|Remove Duplicates from Sorted Array II|remove-duplicates-from-sorted-array-ii|M|Allow each value at most twice, in place.
88|Merge Sorted Array|merge-sorted-array|E|Merge two sorted arrays into the first one.
977|Squares of a Sorted Array|squares-of-a-sorted-array|E|Squares of a sorted array, returned sorted.
16|3Sum Closest|3sum-closest|M|Triplet sum closest to a target.
1846|Maximum Element After Decreasing and Rearranging|maximum-element-after-decreasing-and-rearranging|M|Largest possible final value under adjacent-difference limits.
219|Contains Duplicate II|contains-duplicate-ii|E|Duplicate values within k positions of each other.
"""


def load():
    problems = []
    topic = None
    order = 0
    for line in _RAW.strip().splitlines():
        line = line.strip()
        if not line:
            continue
        if line.startswith("## "):
            topic = line[3:]
            continue
        num, title, slug, diff, summary = line.split("|", 4)
        premium = diff.endswith("$")
        problems.append({
            "num": int(num),
            "title": title,
            "slug": slug,
            "difficulty": {"E": "Easy", "M": "Medium", "H": "Hard"}[diff.rstrip("$")],
            "premium": premium,
            "topic": topic,
            "folder": TOPICS[topic][0],
            "pattern": TOPICS[topic][1],
            "summary": summary,
            "link": f"https://leetcode.com/problems/{slug}/",
            "core": topic != "Bonus",
        })
        if topic != "Bonus":
            order += 1
            problems[-1]["order"] = order
    return problems
