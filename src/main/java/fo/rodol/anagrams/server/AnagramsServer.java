package fo.rodol.anagrams.server;

import fo.rodol.anagrams.dictionary.AnagramsDictionary;
import fo.rodol.anagrams.dictionary.HashMapAnagramsDictionary;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class AnagramsServer implements Runnable {

    public static final int PORT_NUMBER = 4444;

    private ServerSocket serverSocket;

    private AnagramsDictionary anagramsDictionary;

    public AnagramsServer(AnagramsDictionary anagramsDictionary) throws IOException {

        this.anagramsDictionary = anagramsDictionary;
        this.serverSocket = new ServerSocket(PORT_NUMBER);
    }

    public static void main(String[] args) throws IOException {

        HashMapAnagramsDictionary anagramsDictionary = new HashMapAnagramsDictionary();
        anagramsDictionary.init();

        AnagramsServer anagramsServer = new AnagramsServer(anagramsDictionary);
        anagramsServer.run();
        System.out.println("AnagramsServer started listening on port " + PORT_NUMBER);

        String userInput = readUserInput();
        outputValidAnagrams(userInput, anagramsDictionary);
    }

    @Override
    public void run() {

        try {
            Socket socker = serverSocket.accept();
            System.out.println("Connection accepted");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readUserInput() {

        System.out.println("> Please enter a word:");
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }

    private static void outputValidAnagrams(String word, HashMapAnagramsDictionary anagramsDictionary) {

        List<String> anagrams = anagramsDictionary.getAnagrams(word);

        if (anagrams == null ) {

            System.out.println("> Word not found");

        } else if (anagrams.size() == 1) {

            System.out.println("> No anagrams have been found for the word: " + word);

        } else {

            System.out.println("> These anagrams have been found for the word: " + word);
            anagrams.stream()
                    .filter(s -> !word.equals(s))
                    .forEach(System.out::println);
        }
    }
}
