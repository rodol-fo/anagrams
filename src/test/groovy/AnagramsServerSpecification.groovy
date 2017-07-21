import fo.rodol.anagrams.dictionary.AnagramsDictionary
import fo.rodol.anagrams.server.AnagramsServer
import spock.lang.Specification

import static fo.rodol.anagrams.server.AnagramsServer.PORT_NUMBER

class AnagramsServerSpecification extends Specification {

    def anagramsDictionary = Mock(AnagramsDictionary)

    def 'should start listening to connections on the configured port after start'() {

        when:
        new AnagramsServer(anagramsDictionary)

        then:
        def socket = new Socket('localhost', PORT_NUMBER)

        and:
        socket.isConnected()
    }
}