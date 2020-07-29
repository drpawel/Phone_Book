package phonebook;

import java.sql.SQLOutput;
import java.util.Hashtable;
import java.util.List;

abstract public class SearchAlgorithm {
    abstract void search(List<Person> directory,List<Person> find);
    abstract void getInfo(long end,long start);
    String getTime(long end, long start){
        long time = end - start;
        long millis = time % 1000;
        long second = (time / 1000) % 60;
        long minute = (time / (1000 * 60)) % 60;
        return minute + " min."+second+" sec. "+millis+" ms.";
    }
    static void swap(List<Person> directory, int i, int j) {
        Person temp = directory.get(j);
        directory.set(j,directory.get(i));
        directory.set(i,temp);
    }
    static long swap_time = 50000;
}

class LinearSearch extends SearchAlgorithm{
    private int size = 0;
    private int counter = 0;

    public int getCounter() {
        return counter;
    }

    @Override
    public void search(List<Person> directory,List<Person> find){
        size = find.size();
        for(Person tmp_find : find){
            for(Person tmp_directory : directory){
                if(tmp_find.getName().equals(tmp_directory.getName())){
                    if(tmp_find.getSurname()!=null){
                        if(tmp_find.getSurname().equals(tmp_directory.getSurname())){
                            counter++;
                            break;
                        }
                    }else{
                        counter++;
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void getInfo(long end,long start){
        swap_time=end-start;
        System.out.println("Start searching (linear search)...");
        System.out.println("Found "+counter+" / "+size+" entries. Time taken: "+getTime(end,start));
    }
}

class JumpSearch extends SearchAlgorithm{
    private int size = 0;
    private int counter = 0;
    private String sort_time = null;
    private String search_time = null;

    private boolean stop = false;

    @Override
    public void search(List<Person> directory,List<Person> find){
        List<Person> directory_tmp = directory;
        long sort_start = System.currentTimeMillis();
        size = find.size();
        for (int i = 0; i < directory_tmp.size() - 1; i++) {
            for (int j = 0; j < directory_tmp.size() - i - 1; j++) {
                if (directory_tmp.get(j).getName().compareTo(directory_tmp.get(j+1).getName())>0) {
                    swap(directory_tmp,j,j+1);
                }
                if( System.currentTimeMillis()-sort_start > swap_time){
                    stop = true;
                    break;
                }
            }
        }
        long sort_end = System.currentTimeMillis();
        sort_time = getTime(sort_end,sort_start);

        long search_start = System.currentTimeMillis();
        if(stop){
            LinearSearch linearSearch = new LinearSearch();
            linearSearch.search(directory_tmp,find);
            counter = linearSearch.getCounter();
            //linear search

        }else{
            for(Person tmp_find : find){
                int currentRight = 0;
                int prevRight = 0;

                if (directory_tmp.size() == 0) {
                    break;
                }

                if (directory_tmp.get(currentRight) == tmp_find) {
                    counter++;
                }

                int jumpLength = (int) Math.sqrt(directory_tmp.size());

                while (currentRight < directory_tmp.size() - 1) {

                    /* Calculating the right border of the following block */
                    currentRight = Math.min(directory_tmp.size() - 1, currentRight + jumpLength);

                    if (directory_tmp.get(currentRight).getName().compareTo(tmp_find.getName()) > 0) {
                        break; // Found a block that may contain the target element
                    }

                    prevRight = currentRight; // update the previous right block border
                }

                /* If the last block is reached and it cannot contain the target value => not found */
                if ((currentRight == directory_tmp.size() - 1) && tmp_find.getName().compareTo(directory_tmp.get(currentRight).getName()) > 0) {
                    break;
                }

                for (int i = currentRight; i > prevRight; i--) {
                    if (directory_tmp.get(i).getName().compareTo(tmp_find.getName()) == 0) {
                        if(tmp_find.getSurname()!=null){
                            if(tmp_find.getSurname().equals(directory_tmp.get(i).getSurname())){
                                counter++;
                                break;
                            }
                        }else{
                            counter++;
                            break;
                        }
                    }
                }
            }
            //jump search
        }
        long search_end = System.currentTimeMillis();
        search_time = getTime(search_end,search_start);
    }

    @Override
    public void getInfo(long end,long start){
        System.out.println("Start searching (bubble sort + jump search)...");
        System.out.println("Found "+counter+" / "+size+" entries. Time taken: "+getTime(end, start));
        if(stop){
            System.out.println("Sorting time: "+sort_time+" - STOPPED, moved to linear search");
        }else{
            System.out.println("Sorting time: "+sort_time);
        }
        System.out.println("Searching time: "+search_time);
    }

}

class BinarySearch extends SearchAlgorithm{
    private int size = 0;
    private int counter = 0;
    private String sort_time = null;
    private String search_time = null;

    @Override
    public void search(List<Person> directory,List<Person> find){
        List<Person> directory_tmp = directory;
        long sort_start = System.currentTimeMillis();
        size = find.size();
        quickSort(directory_tmp,0,directory_tmp.size()-1);
        sort_time = getTime(System.currentTimeMillis(),sort_start);

        long search_start = System.currentTimeMillis();
        for(Person tmp_find : find){
            binary(directory_tmp,tmp_find,0,directory_tmp.size()-1);
        }
        search_time = getTime(System.currentTimeMillis(),search_start);


    }

    @Override
    public void getInfo(long end,long start){
        System.out.println("Start searching (quick sort + binary search)...");
        System.out.println("Found "+counter+" / "+size+" entries. Time taken: "+getTime(end, start));
        System.out.println("Sorting time: "+sort_time);
        System.out.println("Searching time: "+search_time);
    }

    private void binary(List<Person> directory, Person elem, int left, int right) {
        while (left <= right) {
            int mid = left + (right - left) / 2; // the index of the middle element

            if (directory.get(mid).getName().compareTo(elem.getName()) == 0) {
               counter++;
               break;
            } else if (directory.get(mid).getName().compareTo(elem.getName()) > 0) {
                right = mid - 1; // go to the left subarray
            } else {
                left = mid + 1;  // go the the right subarray
            }
        }
    }

    private static void quickSort(List<Person> directory, int left, int right) {
        if (left < right) {
            int pivotIndex = partition(directory, left, right); // the pivot is already on its place
            quickSort(directory, left, pivotIndex - 1);  // sort the left subarray
            quickSort(directory, pivotIndex + 1, right); // sort the right subarray
        }
    }

    private static int partition(List<Person> directory, int left, int right) {
        Person pivot = directory.get(right);  // choose the rightmost element as the pivot
        int partitionIndex = left; // the first element greater than the pivot

        /* move large values into the right side of the array */
        for (int i = left; i < right; i++) {
            if (pivot.getName().compareTo(directory.get(i).getName())> 0) { // may be used '<' as well
                swap(directory, i, partitionIndex);
                partitionIndex++;
            }
        }

        swap(directory, partitionIndex, right); // put the pivot on a suitable position

        return partitionIndex;
    }


}

class HashSearch extends SearchAlgorithm{
    private int size = 0;
    private int counter = 0;
    private String creating_time = null;
    private String search_time = null;
    private Hashtable<String,Person> hashtable;

    @Override
    void search(List<Person> directory, List<Person> find) {
        this.size = find.size();

        long creating_start = System.currentTimeMillis();
        hashtable = new Hashtable<>(directory.size());
        for (Person tmp : directory){
            hashtable.put(tmp.getName(),tmp);
        }
        creating_time = getTime(System.currentTimeMillis(),creating_start);

        long search_start = System.currentTimeMillis();
        for (Person tmp : find){
            if(tmp.getName().equals(hashtable.get(tmp.getName()).getName())){
                counter++;
            }
        }
        search_time = getTime(System.currentTimeMillis(),search_start);
    }

    @Override
    void getInfo(long end, long start) {
        System.out.println("Start searching (hash table)...");
        System.out.println("Found "+counter+" / "+size+" entries. Time taken: "+getTime(end, start));
        System.out.println("Creating time: "+creating_time);
        System.out.println("Searching time: "+search_time);
    }


}