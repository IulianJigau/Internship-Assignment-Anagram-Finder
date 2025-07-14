# Design Decisions

## Task Overview

The goal of this project was to develop a Java program that identifies and groups anagrams from a given input file. The key priorities for the implementation were:

- **Maintainability**
- **Scalability**
- **Performance**

---

## Initial Considerations

Given the potentially large number of words in the input file, a naive solution such as sorting and comparing each word with every other word was immediately ruled out due to its unacceptable time complexity of **O(n² × k)**, where:

- **n** = number of words
- **k** = average length of each word

---

## Choosing a Hashing-Based Solution

To achieve the desired performance, a **hashing-based approach** was selected. Hashing is particularly suitable because it allows for:

- **Average case constant-time lookups:** O(1)
- **Efficient grouping of equivalent values** (in this case, anagrams)

<details>
<summary><strong>What is Hashing?</strong></summary>

<br>

Hashing is a technique that converts input data (a **key**) into a fixed-size numerical value (the **hash code**) using a deterministic algorithm. This hash code is then used to determine the bucket where the value is stored in a **HashMap** or similar data structure.

For a more detailed explanation, you can read [this article on GeeksforGeeks](https://www.geeksforgeeks.org/hashing-data-structure/).

In the context of this project:

- The **key** represents a unique signature of the word's character composition.
- The **value** is a list of words sharing that signature (i.e., words that are anagrams).

</details>

---

## Key Challenge

To leverage hashing for grouping anagrams, words that are anagrams of each other need to produce the **same hash key**. This required creating a **normalization function** that transforms anagrams into identical representations.

---

## Potential Solutions for Key Generation

To efficiently group anagrams using a hash-based approach, each word needed to be transformed into a **normalized key**—a value identical for all words that are anagrams of each other. Two main strategies were considered for generating this key.

---

### 1. Sorted Characters Approach

This approach transforms each word by sorting its characters alphabetically. Anagrams, when sorted, produce the same character sequence, which is used as the key for grouping.

Examples:
- `"listen"` → `"eilnst"`
- `"silent"` → `"eilnst"`

The method is simple, readable, and works well for shorter words. However, since sorting each word requires **O(k log k)** time (where **k** is the word’s length), it becomes less efficient when dealing with longer words or very large datasets.

### 2. Character Count Approach

The second approach involves counting the frequency of each letter in the word and constructing a unique key based on these counts. To build the key, the counts of all 26 letters are listed in order, separated by a delimiter (e.g., `#`), resulting in a fixed-length representation.

Examples:
- `"listen"` → Letter counts: {a:0, b:0, ..., e:1, i:1, l:1, n:1, s:1, t:1, ...}
- Key → `0#0#0#0#1#0#0#0#1#0#1#0#0#1#0#0#0#1#1#1#0#0#0#0#0#0`

This approach generates keys in **linear time** relative to the word length (**O(k)**) and produces a key string of **constant size** (approximately 51 characters: 26 counts + 25 delimiters).

---

#### Trade-offs

The character count approach offers better performance for extremely long words (around 50+ letters) since it avoids the sorting step entirely. However, for typical English words—where 90% are **10 letters or fewer**—this method introduces unnecessary overhead when constructing and hashing the key.

---

## Chosen Solution

Based on statistical analysis of English word lengths, the **Sorted Characters Approach** was selected as the primary method for key generation in this project since it provides a better balance between **performance**, **simplicity**, and **resource consumption** for the most common use case.

## Performance Results and Scalability Concerns

The selected approach allowed the program to achieve impressive processing speeds.

On a local machine (**Intel i7-12700H laptop**), the program processed **1 million words** (with word lengths ranging from 3 to 12 letters) in **under 500 milliseconds**.

The performance demonstrated a **linear time complexity** (**O(n)**), meaning the processing time scales proportionally with the number of words. Extrapolating this trend, processing **100 billion words** would require approximately **15 hours**, even on modest hardware.

However, while the algorithm’s time complexity was acceptable, a new critical limitation emerged: **memory consumption**.

---

## Memory Usage and HashMap Limitations

The core data structure of the solution—Java’s **HashMap**—presents specific memory challenges, particularly when handling very large datasets.

<details>
<summary><strong>Key Memory Components of a HashMap</strong></summary>

<br>

A Java `HashMap` consists of several key memory components:

- **Buckets (Array of Nodes):**  
  An internal array where each index corresponds to a bucket. Each bucket holds either a single key-value pair or a chain/tree of pairs in case of hash collisions.

- **Node Objects (Entries):**  
  Every key-value pair is stored as a separate node object containing:
  - The **key** (in this case, the normalized word string)
  - The **value** (a list of words that match the anagram key)
  - The **hash code**
  - A **reference to the next node** (for collision resolution)

</details>

---

## Memory Growth Analysis

Through empirical testing, it was determined that:

- For every **5 million unique words**, the memory used by the `HashMap` increased by approximately **1 GB of RAM**.

To process the theoretical target of **100 billion words**, the required memory would scale to an estimated **20 terabytes of RAM**.

Such memory requirements far exceed the capabilities of even high-end servers, making this approach infeasible for ultra-large datasets without additional optimization or architectural changes.

## Memory Optimization: Word Length Partitioning

To address the memory bottleneck described earlier, an additional optimization was implemented: **partitioning the words based on their lengths into separate files**.

### How It Works

Words are pre-sorted into different files, each containing words of the same or similar length (e.g., 3-letter words, 4-letter words, etc.). The main processing function then handles these smaller files independently, resulting in significantly smaller `HashMap` sizes at any given time.

### Benefits

This partitioning reduces peak memory usage by up to **10 times**, since the `HashMap` never needs to hold entries for the entire dataset simultaneously. While it does not entirely eliminate the memory growth issue for **massive datasets**, it provides a practical mitigation that enables the program to handle much larger inputs without requiring prohibitively large amounts of RAM.

The computational overhead introduced by this step is minimal, allowing the program to **preserve its high performance**.

### Conditional Activation

To avoid unnecessary file I/O and maintain maximum speed for small to medium datasets, partitioning is only activated when the input exceeds **10 million words**. This threshold ensures that:
- Small datasets remain fast and efficient.
- Large datasets benefit from reduced memory consumption.

---

## Additional Performance Tuning: HashMap Capacity Adjustment

<br>

Since the number of words is already counted during preprocessing, this information was used to further optimize the `HashMap` by setting an initial capacity close to the expected number of entries.

### Key Adjustments

The initial capacity of the `HashMap` is set based on the total number of words. Assuming that roughly **25%** of the words will form part of an anagram group this change would set the bucket-to-entries ratio to approximately **4:3** which is recommended to maintain the balance between **memory usage** and **lookup performance**. This estimate helps avoid costly internal resizing operations.

### Observed Impact

This fine-tuning led to a measurable improvement in both **speed** and **memory efficiency**, surpassing the earlier implementation that did not do any word counting.

## Future Scalability Considerations

For datasets reaching into the **billions or trillions of words**, memory usage would remain the primary limitation. To handle such cases, a more granular division strategy would likely be required—splitting words not only by **length** but also by their **alphabet composition**.

### How It Could Work

Words could be grouped based on specific letter patterns. For example:
- Words containing `'aa'` would be placed in one file.
- Words containing `'ab'` in another, and so on.

This strategy would limit the number of active anagram groups held in memory at any given time, keeping the size of the `HashMap` manageable.

### Trade-offs

While this method would make it technically possible to process **massive datasets**, it would come with a significant increase in **processing time** due to additional file I/O and multiple processing passes.

For this reason, such an approach would only be justified for **extreme-scale data** and could be implemented as an **optional feature**, triggering automatically when dataset size exceeds practical memory limits.



