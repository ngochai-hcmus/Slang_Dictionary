import java.io.*;
import java.util.*;

public class SlangWord {
    private TreeMap<String, Set<String>> slangWord;

    public SlangWord() {
        this.slangWord = new TreeMap<String, Set<String>>();
    }

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
                (this.slangWord).put(content[0].trim(), def);
            }
            bufferedReader.close();
        } else {
            System.out.println("File not found!");
        }
    }

    public Set<String> searchBySlangWord(String word) {
        return this.slangWord.get(word);
    }

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

    public static void main(String[] args) throws IOException {
        SlangWord word = new SlangWord();
        word.readSlangWord("src/slang.txt");
    }
}