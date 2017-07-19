package fo.rodol.anagrams;

import java.util.*;

public class Anagrams {

    public static void main(String[] args) {

        Map<String, List<String>> anagramLookupMap = loadDictionaryFile();
        String userInput = readUserInput();
        outputValidAnagrams(userInput, anagramLookupMap);
    }

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
    private static Map<String, List<String>> loadDictionaryFile() {

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Scanner scanner = new Scanner(contextClassLoader.getResourceAsStream("words.txt"));

        Map<String, List<String>> anagramLookupMap = new HashMap<>();

        while (scanner.hasNext()) {

            String line = scanner.next();
            String key = sortString(line);

            if (anagramLookupMap.containsKey(key)) {
                anagramLookupMap.get(key).add(line);

            } else {
                List<String> words = new ArrayList<>();
                words.add(line);
                anagramLookupMap.put(key, words);
            }
        }
        return anagramLookupMap;
    }

    /**
     * Returns a new String with the characters from 'word' sorted.
     * Example: sortString("mobile) => "beilmo"
     */
    private static String sortString(String word) {

        char[] chars = word.toCharArray();
        Arrays.sort(chars);

        return new String(chars);
    }

    private static String readUserInput() {

        System.out.println("> Please enter a word:");
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }

    private static void outputValidAnagrams(String userInput, Map<String, List<String>> anagramLookupMap) {

        String key = sortString(userInput);
        List<String> anagrams = anagramLookupMap.get(key);

        if (anagrams == null ) {

            System.out.println("> Word not found");

        } else if (anagrams.size() == 1) {

            System.out.println("> No anagrams have been found for the word: " + userInput);

        } else {

            System.out.println("> These anagrams have been found for the word: " + userInput);
            anagrams.stream()
                    .filter(s -> !userInput.equals(s))
                    .forEach(System.out::println);
        }
    }
}
