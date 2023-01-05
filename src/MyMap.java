import java.util.*;


public class MyMap{
    public LinkedList<Double> keyList;
    public Map<Double, CharNode> myMap = new HashMap<>();

    MyMap(){ keyList = new LinkedList<>(); }

    public void put(double low, CharNode charNode){
        int index = binarySearch(low) + 1;
        keyList.add(index, low);
        myMap.put(low, charNode);
    }
    public CharNode get(double key){
        int index = binarySearch(key);
        key = keyList.get(index);
        return myMap.get(key);
    }

    public Collection values(){ return myMap.values(); }
    public Collection keySet(){ return new ArrayList(keyList); }


    //search for the valid place to insert the element
    //it finds the index of the closest element to the key and returns it's right next index
    private int binarySearch(double key){
        int medium, maxIndex = -1;
        int low = 0, high = keyList.size() - 1;

        //binary search for the closest element less than or equal the key
        while(low <= high){
            medium = (low+high)/2;
            double current = keyList.get(medium);

            if(current == key) return medium;
            //else find the index of the closest element less than the key;
            else if(current < key){
                maxIndex = medium;
                low = medium + 1;
            }
            else high = medium - 1;
        }
        //return the index of the closest element found
        return maxIndex;
    }

}
