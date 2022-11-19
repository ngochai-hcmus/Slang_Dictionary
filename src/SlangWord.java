import java.io.*;
import java.util.*;

public class SlangWord {
    private final TreeMap<String, Set<String>> slangWord;
    private TreeMap<String, Set<String>> historyWord;
    private TreeMap<String, Set<String>> deletedWord;

    public SlangWord() {
        this.slangWord = new TreeMap<String, Set<String>>();
    }

    // Read file to slangWord
    public void readSlangWord(String fileName) throws IOException {
        File f = new File(fileName);
        if (f.exists()) {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            this.slangWord.clear();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] content = line.split("(`)"); // Split a line to <Slang> and <Definitions>
                if (content.length != 2)
                    continue;
                String[] syn = content[1].split("\\|"); // Split <Definitions> if they have many meaning.
                for (int i = 0; i < syn.length; i++) {
                    syn[i] = syn[i].trim();
                }

                Set<String> def = new HashSet<String>(Arrays.asList(syn)); // Append processed data to treemap
                (this.slangWord).put(content[0], def);
                System.out.println(content[0]);
                System.out.println(def);
            }
            bufferedReader.close();
        } else {
            System.out.println("File not found!");
        }
    }

    // 1. Search function by slang word
    public Set<String> searchBySlangWord(String word) {
        this.historyWord.put(word, this.slangWord.get(word));
        return this.slangWord.get(word);
    }

    // 2. Search function by definition, displays all the slang words that in the definition contain the keyword typed.
    public List<String> searchByDefinition(String word){
        // Handle the input word
        String lowWord = word.toLowerCase();
        String upWord = word.toUpperCase();
        String capitalizedWord = lowWord.substring(0, 1).toUpperCase() + lowWord.substring(1);

        List<String> result = new ArrayList<String>();
        Set<Map.Entry<String, Set<String>>> entrySet = this.slangWord.entrySet(); // Get list of <Slang, Set<Definitions> pairs
        for(Map.Entry<String,Set<String>> entry: entrySet){
            Set<String> definite = entry.getValue(); // Get set <Definitions> of each slang word
            for (String def : definite){
                if (def.contains(lowWord) || def.contains(upWord) || def.contains(word) || def.contains(capitalizedWord)) // Check the cases of the input word
                    result.add(entry.getKey());
            }
        }
        return result;
    }

    // 3. Function to display history, list of searched slang words.
    public TreeMap<String, Set<String>> displayHistory() {
        return this.historyWord;
    }

    // 4. Add 1 new slang word function. If the slang word matches, notify the user and confirm whether to overwrite or duplicate a new slang word.
    public void addSlangWord(String word, Set<String> definition) {
        if (this.slangWord.containsKey(word)) { // Check if the slang word matches
            System.out.println("Slang word matches!!");
            Scanner scanner = new Scanner(System.in);
            int choice = 0;
            do {
                System.out.println("(1) Overwrite or (2) Duplicate: ");
                choice = Integer.parseInt(scanner.nextLine()); // Choose choice 1: Overwrite, choice 2: Duplicate
            } while (choice != 1 && choice != 2);
            if (choice == 1) { // Overwrite slang word
                this.slangWord.put(word, definition);
            }
            if (choice == 2) { // Duplicate slang word
                Set<String> definitionOld = this.slangWord.get(word); // Get old set <Definition> of slang word
                definitionOld.addAll(definition); // Add definition to old set <Definition> => new set<Definition>
                this.slangWord.remove(word);
                this.slangWord.put(word, definitionOld);
            }
        }
        else {
            this.slangWord.put(word, definition);
        }
    }

    // 5. Function to edit 1 slang word
    public void editSlangWord(String word, Set<String> definition) {
        if (this.slangWord.containsKey(word)) { // Check if the slang word find
            this.slangWord.remove(word);
            this.slangWord.put(word, definition);
        }
        else {
            System.out.println("Error: none find slang word.");
        }
    }

    // 6. Function delete 1 slang word. Confirm before deleting.
    public void deleteSlangWord(String word) {
        if (this.slangWord.containsKey(word)) { // Check if the slang word find
            System.out.println("(1) Delete (2) Cancel: ");
            Scanner scanner = new Scanner(System.in);
            int choice = 0;
            do {
                System.out.println("(1) Delete or (2) Cancel: ");
                choice = Integer.parseInt(scanner.nextLine()); // Choose choice 1: Overwrite, choice 2: Duplicate
            } while (choice != 1 && choice != 2);
            if (choice == 1) {
                this.historyWord.put(word,this.slangWord.get(word));
                this.slangWord.remove(word);
            }
        }
        else {
            System.out.println("Error: none find slang word.");
        }
    }

    public static void main(String[] args) throws IOException {
        SlangWord word = new SlangWord();
        word.readSlangWord("src/slang.txt");
    }
}