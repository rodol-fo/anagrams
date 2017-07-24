# Anagrams

Reads a word from the user input and prints a list of its anagrams, based on a dictionary file included with the application.

# Usage

To build the program, simply execute in a BASH terminal:

```bash
./gradlew clean build
```
or in a Windows CMD:

```bash
gradlew.bat clean build
```

To run the Server:

```bash
java -cp build/libs/anagrams.jar fo.rodol.anagrams.server.AnagramsServer
```

To run the Client:

```bash
java -cp build/libs/anagrams.jar fo.rodol.anagrams.client.AnagramsClient
```
