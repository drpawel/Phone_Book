package phonebook;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SearchEngine {
    private SearchAlgorithm searchAlgorithm;

    public void setSearchAlgorithm(String algorithmName){
        if(algorithmName.equals("Linear")){
            this.searchAlgorithm = new LinearSearch();
        }else if(algorithmName.equals("Jump")){
            this.searchAlgorithm = new JumpSearch();
        }else if(algorithmName.equals("Binary")){
            this.searchAlgorithm = new BinarySearch();
        }else if(algorithmName.equals("Hash")){
            this.searchAlgorithm = new HashSearch();
        }
    }

    public void search(List<Person> directory, List<Person> find){
        this.searchAlgorithm.search(directory, find);
    }

    public void getInfo(long end,long start){
        this.searchAlgorithm.getInfo(end,start);
    }

}
