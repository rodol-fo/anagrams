package fo.rodol.anagrams.server;

import fo.rodol.anagrams.dictionary.AnagramsDictionary;
import fo.rodol.anagrams.dictionary.HashMapAnagramsDictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AnagramsServer {

    public static final int PORT_NUMBER = 4444;
    private final ServerSocket serverSocket;
    private final BufferedReader in;
    private final PrintWriter out;
    private AnagramsDictionary anagramsDictionary;

    public AnagramsServer(AnagramsDictionary anagramsDictionary, ServerSocket serverSocket, BufferedReader in, PrintWriter out) {

        this.anagramsDictionary = anagramsDictionary;
        this.serverSocket = serverSocket;
        this.in = in;
        this.out = out;
    }

    public static void main(String[] args) throws IOException {

        HashMapAnagramsDictionary anagramsDictionary = new HashMapAnagramsDictionary();
        anagramsDictionary.init();

        try (
                ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
                Socket clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {

            AnagramsServer anagramsServer = new AnagramsServer(anagramsDictionary, serverSocket, in, out);
            anagramsServer.run();
        }
    }

    public void run() throws IOException {

        System.out.println("Connection established");
        String inputLine, outputLine;
        while ((inputLine = in.readLine()) != null) {

            processClientInput(inputLine);
        }
    }

    public void processClientInput(String inputLine) {

        String[] split = inputLine.split(":");
        switch (split[0]) {

            case "P":

                printValidAnagrams(split[1]);
                break;

            case "A":

                addWord(split[1]);
                break;

            case "D":

                removeWord(split[1]);
                break;

        }
    }

    private void addWord(String word) {

        anagramsDictionary.addWord(word);
        out.println("word_added");
    }

    private void removeWord(String word) {

        anagramsDictionary.removeWord(word);
        out.println("word_removed");
    }

    private void printValidAnagrams(String word) {

        List<String> anagrams = anagramsDictionary.getAnagrams(word);

        if (anagrams == null) {

            out.println("word_not_found");

        } else if (anagrams.size() == 1) {

            out.println("no_anagrams_found");

        } else {

            String anagramsResponse = anagrams.stream()
                    .filter(s -> !word.equals(s))
                    .collect(Collectors.joining(","));

            out.println("anagrams:" + anagramsResponse);
        }
    }
}
