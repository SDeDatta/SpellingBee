//SpellingBee by Surya De Datta
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }
    // Generates all possible permutations of the string of given letters
    public void generate() {
        // Calls a recursive method
        // Empty string is the substring to add too and letters is the letters left
        generateHelper("",letters);

    }

    public void generateHelper(String word, String letters)
    {
        // Ends when there are no letters left to add to substring in the current iteration
        if(letters.isEmpty())
        {
            return;
        }
        for(int i = 0; i < letters.length(); i++)
        {
            // Adds letters one at a time
            String otherWord = word + letters.charAt(i);
            words.add(otherWord);
            generateHelper(otherWord, letters.substring(0,i) + letters.substring(i+1));
        }

    }

    public void sort()
    {
        words = mergeSort(words, 0, words.size()-1);
    }

    // Uses mergeSort to sort all the words in alphabetical order
    public ArrayList<String> mergeSort(ArrayList<String> arr, int low, int high)
    {
        // Divides the strings
        // Stops recursion when the sublist contains only one element
        if (low >= high) {
            ArrayList<String> newArr = new ArrayList<String>(1);
            newArr.add(arr.get(low));
            return newArr;
        } else {
            int mid = (high + low) / 2;
            // Sorts the two halves of the array
            ArrayList<String> arr1 = mergeSort(arr, low, mid);
            ArrayList<String> arr2 = mergeSort(arr, mid + 1, high);
            // Calls merge on two individual elements
            return merge(arr1, arr2);

        }
    }

    public ArrayList<String> merge(ArrayList<String> arr1, ArrayList<String> arr2)
    {
        // Merges the strings
        ArrayList<String> sol = new ArrayList<String>();

        while (!arr1.isEmpty() && !arr2.isEmpty())
        {
            if(arr2.get(0).compareTo(arr1.get(0)) > 0)
            {
                sol.add(arr1.get(0));
                arr1.remove(0);
            } else {
                sol.add(arr2.get(0));
                arr2.remove(0);
            }
        }

        // Copy over any remaining elements
        while (!arr1.isEmpty()) {
            sol.add(arr1.get(0));
            arr1.remove(0);
        }

        while (!arr2.isEmpty()) {
            sol.add(arr2.get(0));
            arr2.remove(0);
        }

        return sol;

    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // Ensures words are in the dictionary
    public void checkWords() {
        for(int i = 0; i < words.size(); i++)
        {
            if(!binarySearch(words.get(i),0,DICTIONARY_SIZE-1))
            {
                words.remove(i);
                i--;
            }
        }
    }

    // Uses binary search to ensure the target is in the dictionary
    public boolean binarySearch(String target, int left, int right)
    {
        if(left > right)
        {
            return false;
        }
        int mid = left + (right - left)/2;
        if(DICTIONARY[mid].equals(target))
        {
            return true;
        }
        if(DICTIONARY[mid].compareTo(target) > 0)
        {
            return binarySearch(target,left, mid-1);
        }
        else
        {
            return binarySearch(target, mid+1, right);
        }
    }
    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
