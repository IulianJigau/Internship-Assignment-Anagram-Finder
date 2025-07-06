# Internship Assignment - Anagram Finder

This is a technical task submitted for the Global Database internship application. Below is the technical explainer on how to run the code.

# How to Run This Project

There are two ways to run the Anagram Finder, depending on whether you prefer to use the precompiled version or the raw source code.

## Option 1: Running the Precompiled JAR

1. Download the Anagram-Finder.jar file and the words.txt file from the Releases section of this repository.
   (important: the JAR must be in the same location as words.txt)

3. Execute the JAR file directly:  
   Ensure that a valid Java Runtime Environment (JRE) is installed on your system and souble-click the Anagram-Finder.jar file.

4. Alternatively, execute the JAR file via terminal:

   ```bash
   java -jar Anagram-Finder.jar
   ```

---

## Option 2: Compiling and Running the Source Code

1. Install the latest version of the **Java Development Kit (JDK)**:  

2. Download the source code located in the `src/` directory.

3. Open a terminal window and navigate to the source code directory:

   ```bash
   cd path/to/src
   ```

4. Compile the source code:

   ```bash
   javac AnagramGrouper.java
   ```

5. Package the compiled classes into an executable JAR file:

   ```bash
   jar cfm Anagram-Finder.jar manifest.txt *.class
   ```

6. Run the application either by:
   - Double-clicking the generated `Anagram-Finder.jar` file
   - Executing the following terminal command:

   ```bash
   java -jar Anagram-Finder.jar
   ```

---

## Additional Information

- Both the `src/` directoriy and the release contain the `words.txt` file.
- This file includes a sample dataset of 10,000 words, each consisting of 5 to 15 characters and limited to the first 16 letters of the English alphabet. The file is provided for testing purposes.
- You may replace or modify the `words.txt` file to analyze custom word lists and generate corresponding anagram groups.

---
