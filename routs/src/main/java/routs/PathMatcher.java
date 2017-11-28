package routs;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

// TODO Move to library
// TODO Interfacialize
// TODO Implemet Map interface
// TODO Support converter and validator
public class PathMatcher {
    @NotNull
    private final TrieNode root;
    @NotNull
    private final LruCache<String, TrieNode> cache;
    @NotNull
    public final Map<String, String> namedPath;

    public PathMatcher() {
        this(1);
    }

    public PathMatcher(int cacheSize) {
        this.root = new TrieNode();
        this.cache = new LruCache<>(cacheSize);
        this.namedPath = new HashMap<>();
    }

    public void add(@Nullable final String[] paths) {
        for (String path : paths) {
            add(path);
        }
    }

    public void add(@NotNull final String path) {
        add(path.split("/"), 0, root, null);
    }

    public void add(@NotNull final String path, @Nullable final String key) {
        add(path.split("/"), 0, root, key);
    }

    private static String substringQuote(String text, String left, String right) {
        int i = text.indexOf(left);
        int j = text.indexOf(right);
        //if (i < 0) i = 0;
        //if (j < 0) j = text.length() - 1;
        return text.substring(i+1, j);
    }

    private static String substringUntil(String text, String end) {
        String res = text;
        int i = text.indexOf(end);
        if (i > 0) res = text.substring(0, i);
        return res;
    }

    public void add(@NotNull final String[] names, int i, @Nullable TrieNode head, @Nullable final String key) {
        if (head == null) return;
        if (i >= names.length) {
            head.end = true;
            head.key = key;
            return;
        }

        String name = names[i];

        boolean isNamed = name.startsWith(":");
        boolean isRegex = name.contains("<") && name.endsWith(">");
        if (isNamed) {
            head.named = substringQuote(name,":", "<");
            if (!isRegex) name = "<.*>";
        }
        isRegex = name.contains("<") && name.endsWith(">");

        if (isRegex) {
            TrieNode next = head.regexNexts.getOrDefault(name, new TrieNode(name));
            head.regexNexts.put(name, next);

            // filter nodes into regexes
            Set<TrieNode> regexMatches = head.regexes.getOrDefault(name, new HashSet<TrieNode>());
            head.regexes.put(name, regexMatches);
            String regex = substringQuote(name, "<", ">");
            Pattern pattern = Pattern.compile(regex);
            head.patterns.put(name, pattern);
            for (Map.Entry<String, TrieNode> node : head.nexts.entrySet()) {
                //if (pattern.matcher(node.getKey()).matches()) {
                if (node.getKey().matches(regex)) {
                    regexMatches.add(node.getValue());
                    add(names, i+1, node.getValue(), key);
                }
            }

            add(names, i+1, next, key);
        } else {
            TrieNode next = head.nexts.getOrDefault(name, new TrieNode(name));
            head.nexts.put(name, next);

            for (Map.Entry<String, Set<TrieNode>> node : head.regexes.entrySet()) {
                //if (head.patterns.get(node.getKey()).matcher(name).matches()) {
                String regex = substringQuote(node.getKey(), "<", ">");
                if (name.matches(regex)) {
                    node.getValue().add(next);
                }
            }

            add(names, i+1, next, key);
        }
    }

    public void dump() {
        dump(root);
    }

    private void dump(@NotNull final TrieNode head) {
        System.out.println(head.name);
        if (head.end) {
            return;
        }
        for (TrieNode node : head.nexts.values()) {
            dump(node);
        }
    }

    @NotNull
    public TrieNode matchesNode(@NotNull final String path) {
        TrieNode matched = cache.get(path);
        if (matched != null) return matched;
        matched = matchesNode(path.split("/"), root, 0);
        if (matched == null) matched = new TrieNode();
        cache.put(path, matched);
        return matched;
    }

    public boolean matches(@NotNull final String path) {
        return matchesNode(path).end;
    }

    @Nullable
    private TrieNode matchesNode(@NotNull final String[] names, @Nullable final TrieNode head, int i) {
        //if (i == 0) namedPath.clear(); // FIXME: leaks last data if didn't clear
        if (head == null) return null;
        if (i >= names.length) {
            if (head.end) return head;
            return null;
        }

        final String name = names[i];
        TrieNode next = head.nexts.get(name);

        if (next == null && !head.regexes.isEmpty()) {
            for (Map.Entry<String, Set<TrieNode>> regexEntry : head.regexes.entrySet()) {
                if (head.patterns.get(regexEntry.getKey()).matcher(name).matches()) {
                    namedPath.put(head.named, name);
                    for (TrieNode node : regexEntry.getValue()) {
                        TrieNode matchesNode = matchesNode(names, node, i+1);
                        if (matchesNode != null) {
                            return matchesNode;
                        }
                    }
                    TrieNode matchesNode = matchesNode(names, head.regexNexts.get(regexEntry.getKey()), i+1);
                    if (matchesNode != null) {
                        return matchesNode;
                    }
                }
            }
        }
        if (next == null) return null;
        namedPath.put(head.named, name);
        return matchesNode(names, next, i+1);
    }

    public static class TrieNode {
        // TODO for reducing space complexity instead of HashMap
        @NotNull
        public final StringMatcher nameMatcher;
        // filter
        @NotNull
        public final Map<String, Set<TrieNode>> regexes;
        @NotNull
        public final Map<String, TrieNode> regexNexts;
        @NotNull
        public final Map<String, Pattern> patterns;
        @NotNull
        public final Map<String, TrieNode> nexts;
        // filter
        // TODO: intersection of filters
        @NotNull
        public final Map<Integer, TrieNode> depths;
        @NotNull
        public final String name;
        public boolean end;
        @Nullable
        public String key;
        @Nullable
        public String named;

        public TrieNode() {
            this("/");
        }

        public TrieNode(@NotNull final String name) {
            this.name = name;
            this.nexts = new HashMap<>();
            this.nameMatcher = new StringMatcher();
            this.regexes = new HashMap<>();
            this.regexNexts = new HashMap<>();
            this.patterns = new HashMap<>();
            this.depths = new HashMap<>();
        }
    }
}
