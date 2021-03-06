package fo.rodol.anagrams.dictionary;

import java.util.*;

/**
 * HashMap based implementation of an anagrams dictionary
 *
 * Provides lookups in constant time (O(1)) and inits in linear time (O(n))
 */
public class HashMapAnagramsDictionary implements AnagramsDictionary {

    private Map<String, List<String>> instance;

    /**
     * Traverses the file line by line.
     * For each line, puts an entry in a Map, with the key as the word sorted alphabetically
     * ans the value is a list, with all the
     * <p>
     * Example:
     * <p>
     * {
     * "ahmoops"   :     ["shampoo", "oompahs"]
     * "acehorst"  :     ["orchestra", "cathorse"]
     * "doow"      :     ["wood"]
     * }
     * <p>
     * This structure allows for efficient lookup and storage
     */
    @Override
    public void init() {

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Scanner scanner = new Scanner(contextClassLoader.getResourceAsStream("words.txt"));

        instance = new HashMap<>();

        while (scanner.hasNext()) {

            String word = scanner.next();
            addWord(word);
        }
    }

    @Override
    public void addWord(String word) {

        String key = sortString(word);

        if (instance.containsKey(key)) {

            if (!instance.get(key).contains(word)) {
                instance.get(key).add(word);
            }

        } else {
            List<String> words = new ArrayList<>();
            words.add(word);
            instance.put(key, words);
        }
    }

    @Override
    public boolean removeWord(String word) {

        String key = sortString(word);
        boolean removed = false;

        if (instance.containsKey(key)) {
            removed = instance.get(key).remove(word);

            if (removed && instance.get(key).isEmpty()) {

                instance.remove(key);
            }
        }
        return removed;
    }

    /**
     * Sorts the word alphabetically to compute the key in the map, then gets its value.
     */
    @Override
    public List<String> getAnagrams(String word) {

        return instance.get(sortString(word));
    }

    /**
     * Sorts the characters of a string alphabetically
     */
    private static String sortString(String word) {

        char[] chars = word.toCharArray();
        Arrays.sort(chars);

        return new String(chars);
    }
}
