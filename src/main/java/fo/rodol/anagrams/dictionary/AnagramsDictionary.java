package fo.rodol.anagrams.dictionary;

import java.util.List;

public interface AnagramsDictionary {

    List<String> getAnagrams(String word);

    void init();

    void addWord(String word);

    boolean removeWord(String word);
}
