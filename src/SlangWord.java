import java.io.*;
import java.util.*;

public class SlangWord {
    private String FilePath = new String();
    private final TreeMap<String, Set<String>> slangWord  = new TreeMap<String, Set<String>>();
    private final List<String> historyWord = new ArrayList<String>();
    private final TreeMap<String, Set<String>> deletedWord = new TreeMap<String, Set<String>>();

    public SlangWord(String path){
        this.FilePath = path;
    }

    public void addSlangWord(String word, String definition){
        Set<String> setOfDefinition = new HashSet<String>();
        if(!this.slangWord.containsKey(word)){
            setOfDefinition.add(definition);
        }
        else {
            setOfDefinition = this.slangWord.get(word);
            setOfDefinition.add(definition) ;
        }
        this.slangWord.put(word, setOfDefinition);
    }

    // Read file to slangWord
    public void readSlangWord() throws IOException {
        File f = new File(this.FilePath);
        if (f.exists()) {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(this.FilePath));
            this.slangWord.clear();
            this.historyWord.clear();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] content = line.split("`"); // Split a line to <Slang> and <Definitions>
                if (content.length != 2)
                    continue;
                String[] syn = content[1].split("\\|"); // Split <Definitions> if they have many meaning.
                for (String s : syn) {
                    addSlangWord(content[0], s.trim());
                }

//                Set<String> def = new HashSet<String>(Arrays.asList(syn)); // Append processed data to treemap
//                (this.slangWord).put(content[0], def);
            }
            bufferedReader.close();
        } else {
            System.out.println("File not found!");
        }
    }
    public void readSlangWord(String filePath) throws IOException {
        this.FilePath = filePath;
        readSlangWord();
    }

    public void print() {
        System.out.println(this.slangWord);
    }


    // 1. Search function by slang word
    public List<String> searchBySlangWord(String word) {
        this.historyWord.add(word);

        // Handle the input word
        String lowWord = word.toLowerCase();
        String upWord = word.toUpperCase();

        List<String> result = new ArrayList<String>();
        Set<Map.Entry<String, Set<String>>> entrySet = this.slangWord.entrySet(); // Get list of <Slang, Set<Definitions> pairs
        for(Map.Entry<String,Set<String>> entry: entrySet){
            String slangWord = entry.getKey();
            if (slangWord.startsWith(lowWord) || slangWord.startsWith(upWord)) {
                result.add(slangWord);
            }
        }
        return result;
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
    public List<String> displayHistory() {
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
            Scanner scanner = new Scanner(System.in);
            int choice = 0;
            do {
                System.out.println("(1) Delete or (2) Cancel: ");
                choice = Integer.parseInt(scanner.nextLine()); // Choose choice 1: Overwrite, choice 2: Duplicate
            } while (choice != 1 && choice != 2);
            if (choice == 1) {
                this.deletedWord.put(word, this.slangWord.get(word));
                this.slangWord.remove(word);
            }
        }
        else {
            System.out.println("Error: none find slang word.");
        }
    }

    // 7. Function to reset the original list of slang words
    public void resetOriginalSlangWord() throws IOException {
        readSlangWord();
    }

    // 8. Function random 1 slang word (On this day slang word)
    public String randomSlangWord() {
        Object[] slangWords = this.slangWord.keySet().toArray();
        int index = new Random().nextInt(slangWords.length);
        return (String) slangWords[index];
    }

    // 9. 10. Quiz function
    public Map<String, Set<String>> createQuiz() {
        Map<String, Set<String>> quiz = new TreeMap<String, Set<String>>();
        String randomWord = new String();
        for (int i = 0; i < 4; i++) {
            do {
                randomWord = randomSlangWord();
            }while (quiz.containsKey(randomWord));
            quiz.put(randomWord,this.slangWord.get(randomWord));
        }
        return quiz;
    }

    public static void main(String[] args) throws IOException {
        SlangWord word = new SlangWord("src/SlangTest.txt");
        word.readSlangWord();
        Set<String> definition = new HashSet<String>();
        definition.add("hi");
        definition.add("haha");
        word.addSlangWord("HB", definition);
        //word.editSlangWord("HBz", definition);
        word.print();

        word.resetOriginalSlangWord();

        word.print();
    }
}