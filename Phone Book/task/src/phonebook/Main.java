package phonebook;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        List<Person> directory = new ArrayList<Person>();
        List<Person> find = new ArrayList<Person>();

        FileReader fileReader = new FileReader("C:\\Users\\drewe\\Downloads\\directory.txt");
        directory = fileReader.readFile(directory,true);

        fileReader.setPath("C:\\Users\\drewe\\Downloads\\find.txt");
        find = fileReader.readFile(find,false);

        SearchEngine searchEngine = new SearchEngine();

        searchEngine.setSearchAlgorithm("Linear");
        searchEngine.search(directory,find);
        searchEngine.getInfo(System.currentTimeMillis(),start);

        System.out.println();

        start = System.currentTimeMillis();
        searchEngine.setSearchAlgorithm("Jump");
        searchEngine.search(directory,find);
        searchEngine.getInfo(System.currentTimeMillis(),start);

        System.out.println();

        start = System.currentTimeMillis();
        searchEngine.setSearchAlgorithm("Binary");
        searchEngine.search(directory,find);
        searchEngine.getInfo(System.currentTimeMillis(),start);

        System.out.println();

        start = System.currentTimeMillis();
        searchEngine.setSearchAlgorithm("Hash");
        searchEngine.search(directory,find);
        searchEngine.getInfo(System.currentTimeMillis(),start);
    }
}
