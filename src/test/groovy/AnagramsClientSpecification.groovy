import fo.rodol.anagrams.client.AnagramsClient
import org.junit.Before
import spock.lang.Specification

class AnagramsClientSpecification extends Specification {

    def socket = Mock(Socket)
    def serverOut = Mock(PrintWriter)
    def serverInput = Mock(BufferedReader)
    def outContent = new ByteArrayOutputStream() // Allows us to capture the outuput of the application and verify it

    @Before
    def setOutput() {

        System.setOut(new PrintStream(outContent))
    }

    def 'should send request as P:word to fetch anagrams from server and receives a comma separated list of anagrams'() {

        given:
        def client = new AnagramsClient(socket, serverOut, serverInput, new ByteArrayInputStream("word".getBytes()))

        when:
        client.fetchAnagrams()

        then:
        serverOut.println("P:the")

        and:
        serverInput.readLine() >> "anagrams:eth,het"

        and:
        outContent.toString() ==    "> Type word and press enter...\n" +
                                    "> These anagrams have been found: \n" +
                                    "eth\n" +
                                    "het\n"
    }

    def 'should send request as P:word to fetch anagrams from server and receives word_not_found'() {

        given:
        def client = new AnagramsClient(socket, serverOut, serverInput, new ByteArrayInputStream("word".getBytes()))

        when:
        client.fetchAnagrams()

        then:
        serverOut.println("P:word")

        and:
        serverInput.readLine() >> "word_not_found"

        and:
        outContent.toString() ==    "> Type word and press enter...\n" +
                                    "The word has not been found\n"
    }

    def 'should send request as P:word to fetch anagrams from server and receives no_anagrams_found'() {

        given:
        def client = new AnagramsClient(socket, serverOut, serverInput, new ByteArrayInputStream("word".getBytes()))

        when:
        client.fetchAnagrams()

        then:
        1 * serverOut.println("P:word")

        and:
        serverInput.readLine() >> "no_anagrams_found"

        and:
        outContent.toString() ==    "> Type word and press enter...\n" +
                                    "No anagrams have been found\n"
    }

    def 'should send request as D:word to delete word from server and receives word_removed'() {

        given:
        def client = new AnagramsClient(socket, serverOut, serverInput, new ByteArrayInputStream("word".getBytes()))

        when:
        client.deleteWord()

        then:
        1 * serverOut.println("D:word")

        and:
        serverInput.readLine() >> "word_removed"

        and:
        outContent.toString() ==    "> Type word and press enter...\n" +
                                    "Word has been removed\n"
    }

    def 'should send request as A:word to add word from server and receives word_added'() {

        given:
        def client = new AnagramsClient(socket, serverOut, serverInput, new ByteArrayInputStream("word".getBytes()))

        when:
        client.addWord()

        then:
        1 * serverOut.println("A:word")

        and:
        serverInput.readLine() >> "word_added"

        and:
        outContent.toString() ==    "> Type word and press enter...\n" +
                                    "Word has been added\n"
    }
}