# Code Overview

This is a brief explanation of the main components of the program and how they work together to process the input, group anagrams, and generate the output.

---

## Main Functionalities

### 1. Splitting Words by Length (`splitWordsByLength`)

**Purpose:**  
Reduces memory usage by dividing the input file into multiple smaller files, each containing words of the same length.

**How it works:**
- Reads words from the input file.
- Writes each word to a file named `len_X.txt`, where **X** is the word’s length.
- Skips empty lines.

This prevents the main memory structure (`HashMap`) from growing uncontrollably when dealing with very large datasets.

---

### 2. Generating Anagram Keys (`generateKey`)

**Purpose:**  
Produces a unique key for each word based on its **sorted characters**.

**Example:**
- `"listen"` → `"eilnst"`
- `"silent"` → `"eilnst"`

Words with the same key are considered anagrams of each other.

---

### 3. Counting Lines (`countLines`)

**Purpose:**  
Calculates the number of words (lines) in a given file.

**Why:**  
Used to estimate the initial capacity of the `HashMap` to minimize resizing and improve performance.

---

### 4. Grouping Anagrams (`groupAnagrams`)

**Purpose:**  
Reads a file and groups words into anagrams using a `HashMap` where:
- **Key:** Sorted version of the word.
- **Value:** List of original words matching that key.

The `initialCapacity` of the map is set based on the total word count to improve efficiency and reduce unnecessary resizing.

---

### 5. Writing Anagram Groups (`writeAnagramGroups`)

**Purpose:**  
Writes grouped anagrams to the output file, but includes only groups that contain **more than one word** (actual anagram sets).

---

## Program Flow (`main`)

### Input:
- Reads words from `words.txt`.

### Decision Based on File Size:
- If the word count exceeds **10 million**:
  - Splits words by length into separate files.
  - Processes each file individually to group anagrams.
  - Writes results to `anagram_groups.txt`.

- Otherwise:
  - Processes the entire file directly without splitting.

### Output:
- Writes all grouped anagrams to `anagram_groups.txt`.

