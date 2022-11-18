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

    public static void main(String[] args) throws IOException {
        SlangWord word = new SlangWord();
        word.readSlangWord("src/slang.txt");
    }
}