import fo.rodol.anagrams.dictionary.AnagramsDictionary
import fo.rodol.anagrams.server.AnagramsServer
import spock.lang.Specification

class AnagramsServerSpecification extends Specification {

    def anagramsDictionary = Mock(AnagramsDictionary)
    def serverSocket = Mock(ServerSocket)
    def inputReader = Mock(BufferedReader)
    def out = Mock(PrintWriter)

    def "should send word_not_found to client when the word is not found in the dictionary"() {

        given:
        def anagramsServer = new AnagramsServer(anagramsDictionary, serverSocket, inputReader, out)

        when:
        anagramsServer.processClientInput("P:word")

        then:
        anagramsDictionary.getAnagrams("word") >> null

        and:
        1 * out.println("word_not_found")
    }

    def "should send no_anagrams_found to client when the word is found and no other anagrams were found in the dictionary"() {

        given:
        def anagramsServer = new AnagramsServer(anagramsDictionary, serverSocket, inputReader, out)

        when:
        anagramsServer.processClientInput("P:word")

        then:
        anagramsDictionary.getAnagrams("word") >> ["word"]

        and:
        1 * out.println("no_anagrams_found")
    }

    def "should send anagrams:anagram1,anagram2,anagram3 to client when the word is found and three anagrams were found in the dictionary"() {

        given:
        def anagramsServer = new AnagramsServer(anagramsDictionary, serverSocket, inputReader, out)

        when:
        anagramsServer.processClientInput("P:word")

        then:
        anagramsDictionary.getAnagrams("word") >> [ "anagram1", "anagram2", "anagram3" ]

        and:
        1 * out.println("anagrams:anagram1,anagram2,anagram3")
    }

    def "should send word_added to client when the word is added to the dictionary"() {

        given:
        def anagramsServer = new AnagramsServer(anagramsDictionary, serverSocket, inputReader, out)

        when:
        anagramsServer.processClientInput("A:word")

        then:
        1 * anagramsDictionary.addWord("word")

        and:
        1 * out.println("word_added")
    }

    def "should send word_removed to client when the word is removed from the dictionary"() {

        given:
        def anagramsServer = new AnagramsServer(anagramsDictionary, serverSocket, inputReader, out)

        when:
        anagramsServer.processClientInput("D:word")

        then:
        1 * anagramsDictionary.removeWord("word")

        and:
        1 * out.println("word_removed")
    }
}