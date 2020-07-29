package phonebook;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileReader {
    private String path;

    public FileReader(String path) {
        this.path = path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Person> readFile (List<Person> data, boolean mode){
        File file_in = new File(path);
        try (Scanner scanner = new Scanner(file_in)) {
            while (scanner.hasNext()) {
                String person = scanner.nextLine();
                String[] tmp_person = person.split(" ");
                if(mode){
                    if(tmp_person.length==3){
                        data.add(new Person(tmp_person[0],tmp_person[1],tmp_person[2]));
                    }else if(tmp_person.length==2){
                        data.add(new Person(tmp_person[0],tmp_person[1],null));
                    }
                }else{
                    if(tmp_person.length==2){
                        data.add(new Person(null,tmp_person[0],tmp_person[1]));
                    }else if(tmp_person.length==1){
                        data.add(new Person(null,tmp_person[0],null));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No file found");
        }
        return data;
    }
}
