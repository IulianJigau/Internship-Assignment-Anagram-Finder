
import java.io.*;
import java.util.*;

public class AnagramGrouper {

    public static void splitWordsByLength(String inputFileName, String outputDir) {
        Map<Integer, BufferedWriter> writers = new HashMap<>();
        new File(outputDir).mkdirs();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
            String word;
            while ((word = reader.readLine()) != null) {
                word = word.trim();
                if (word.isEmpty()) {
                    continue;
                }

                int len = word.length();
                BufferedWriter writer = writers.computeIfAbsent(len, l -> {
                    try {
                        return new BufferedWriter(new FileWriter(outputDir + "/len_" + l + ".txt"));
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });

                writer.write(word);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            for (BufferedWriter w : writers.values()) {
                try {
                    w.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    public static String generateKey(String word) {
        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    public static long countLines(String fileName) {
        long lineCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            while (reader.readLine() != null) {
                lineCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lineCount;
    }

    public static Map<String, List<String>> groupAnagrams(String fileName, int initialCapacity) {
        float loadFactor = 0.75f;

        Map<String, List<String>> anagramMap = new HashMap<>(initialCapacity, loadFactor);

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String word;
            while ((word = reader.readLine()) != null) {
                word = word.trim().toLowerCase();
                if (word.isEmpty()) {
                    continue;
                }

                String key = generateKey(word);
                anagramMap.computeIfAbsent(key, k -> new ArrayList<>()).add(word);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return anagramMap;
    }

    public static void writeAnagramGroups(Map<String, List<String>> anagramMap, BufferedWriter writer) throws IOException {
        for (List<String> group : anagramMap.values()) {
            if (group.size() > 1) {
                writer.write(String.join(" ", group));
                writer.newLine();
            }
        }
    }

    public static void main(String[] args) {
        String inputFileName = "words.txt";
        String outputDir = "words";
        String combinedOutputFile = "anagram_groups.txt";

        long totalWords = countLines(inputFileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(combinedOutputFile))) {
            if (totalWords > 10_000_000) {
                splitWordsByLength(inputFileName, outputDir);

                File dir = new File(outputDir);

                for (File file : Objects.requireNonNull(dir.listFiles())) {
                    totalWords = countLines(file.getPath());
                    Map<String, List<String>> anagramMap = groupAnagrams(file.getPath(), (int) totalWords);
                    writeAnagramGroups(anagramMap, writer);
                }

            } else {
                Map<String, List<String>> anagramMap = groupAnagrams(inputFileName, (int) totalWords);
                writeAnagramGroups(anagramMap, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
