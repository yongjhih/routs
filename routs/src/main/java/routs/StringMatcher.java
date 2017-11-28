package routs;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

// TODO Move to library
public class StringMatcher {
    @NotNull
    private final TrieNode root;

    public StringMatcher() {
        this.root = new TrieNode('\0');
    }

    public void add(@NotNull final String word) {
        TrieNode head = root;
        for (char c : word.toCharArray()) {
            TrieNode next = head.nexts.getOrDefault(c, new TrieNode(c));
            head.nexts.put(c, next);
            head = next;
        }
        head.end = true;
    }

    public boolean matches(@NotNull final String word) {
        return matches(word, root, 0);
    }

    public boolean matches(@NotNull final String word, @Nullable final TrieNode head, int i) {
        if (head == null) return false;
        if (word.length() == i) return head.end;
        char c = word.charAt(i);
        if (c == '.') {
            for (TrieNode next : head.nexts.values()) {
                if (matches(word, next, i+1)) return true;
            }
            return false;
        }
        TrieNode next = head.nexts.get(c);
        if (next == null) return false;
        return matches(word, next, i+1);
    }

    public static class TrieNode {
        @NotNull
        public final Map<Character, TrieNode> nexts;
        public char c;
        public boolean end;
        public TrieNode(char c) {
            this.c = c;
            this.nexts = new HashMap<>();
        }
    }
}
