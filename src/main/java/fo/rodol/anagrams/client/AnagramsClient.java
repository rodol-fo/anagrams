package fo.rodol.anagrams.client;

import fo.rodol.anagrams.server.AnagramsServer;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class AnagramsClient {

    private Socket socket;

    private PrintWriter out;

    private BufferedReader in;

    private Scanner scanner;

    public AnagramsClient(Socket socket, PrintWriter out, BufferedReader in, InputStream userInput) {

        this.socket = socket;
        this.out = out;
        this.in = in;
        this.scanner = new Scanner(userInput);
    }

    public static void main(String[] args) throws IOException {

        try (
                Socket socket = new Socket("localhost", AnagramsServer.PORT_NUMBER);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {

            AnagramsClient anagramsClient = new AnagramsClient(socket, out, in, System.in);
            anagramsClient.showApplicationMenu();
        }
    }

    public void showApplicationMenu() throws IOException {

        String userInput = null;

        while (true) {

            System.out.println("> Please one of this keys and press enter to select an option:");
            System.out.println("[A] Add a word");
            System.out.println("[D] Delete a word");
            System.out.println("[P] Print anagrams");
            System.out.println("[Q] quit");

            userInput = scanner.next();
            if (userInput.matches("[adpaADPQ]")) {

                switch (userInput) {
                    case "A":
                    case "a":
                        addWord();
                        break;
                    case "D":
                    case "d":
                        deleteWord();
                        break;
                    case "P":
                    case "p":
                        fetchAnagrams();
                        break;
                    case "Q":
                    case "q":
                        System.out.println("Bye");
                        System.exit(0);
                }
            }
        }
    }

    public void fetchAnagrams() throws IOException {

        System.out.println("> Type word and press enter...");
        String word = scanner.next();

        out.println("P:" + word);

        String serverResponse;
        while ((serverResponse = in.readLine()) != null) {

            processServerResponse(serverResponse);
            break;
        }
    }

    private void processServerResponse(String serverResponse) {

        switch (serverResponse) {
            case "word_not_found":
                System.out.println("The word has not been found");
                break;
            case "no_anagrams_found":
                System.out.println("No anagrams have been found");
                break;
            case "word_removed":
                System.out.println("Word has been removed");
                break;
            case "word_added":
                System.out.println("Word has been added");
                break;
            default:
                String[] split = serverResponse.split(":");
                if (!"anagrams".equals(split[0])) {

                    throw new IllegalStateException("Invalid server response");
                }
                printAnagramsAndExit(split[1]);
        }
    }

    private void printAnagramsAndExit(String anagramsSeparatedByComma) {

        System.out.println("> These anagrams have been found: ");
        for (String anagram : anagramsSeparatedByComma.split(",")) {
            System.out.println(anagram);
        }
    }

    public void deleteWord() throws IOException {

        System.out.println("> Type word and press enter...");
        String word = scanner.next();

        out.println("D:" + word);

        String serverResponse;
        while ((serverResponse = in.readLine()) != null) {

            processServerResponse(serverResponse);
            break;
        }

    }

    public void addWord() throws IOException {

        System.out.println("> Type word and press enter...");
        String word = scanner.next();

        out.println("A:" + word);

        String serverResponse;
        while ((serverResponse = in.readLine()) != null) {

            processServerResponse(serverResponse);
            break;
        }
    }
}
