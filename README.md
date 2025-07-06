# Internship Assignment - Anagram Finder

This is a technical task submitted for the Global Database internship application. Below is the technical explainer on how to run the code.

# How to Run This Project

This project provides two methods for executing the **Anagram Finder** application:

## Option 1: Running the Precompiled JAR (Recommended)

1. Download the compiled executable:  
   `dist/Anagram-Finder.jar`

2. Execute the JAR file directly:  
   Ensure that a valid Java Runtime Environment (JRE) is installed on your system.

3. Alternatively, execute the JAR file via terminal:

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

- Both the `src/` and `dist/` directories contain a `words.txt` file.
- This file includes a sample dataset of 10,000 words, each consisting of 5 to 15 characters and limited to the first 16 letters of the English alphabet. The file is provided for testing purposes.
- You may replace or modify the `words.txt` file to analyze custom word lists and generate corresponding anagram groups.

---
