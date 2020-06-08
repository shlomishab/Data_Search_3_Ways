package com.company;

import org.apache.commons.io.FileUtils;
import java.io.*;
import java.util.*;

/**
 * ArrayMgt Class
 *
 * class responsible to read the text files into an Array,
 * QuickSort its indices,
 * and pick a number of randoms lines
 *
 * @author Shlomi_Shabtai
 *
 */

public class ArrayMgt {

    private String filesLocation;

    public ArrayMgt(String filesLocation) {
        this.filesLocation = filesLocation;
    }

    /**
     * filesLocation = dirName of the files to read
     * reads all the files into a String ArrayList
     * @return feelFine = Array contains all the data
     */
    public ArrayList<String> readToArray(){
        List<File> files = (List<File>) FileUtils.listFiles(new File(filesLocation), null, true);
        ArrayList<String> feelFine = new ArrayList<>();
        for (int i=0; i<files.size(); ++i) {
            try {
                boolean b_addAll = feelFine.addAll(FileUtils.readLines(files.get(i),(String) null));
            } catch (IOException e) {
                System.out.println("BOO! Something went wrong: " + e.getMessage());
            }
        }
        return feelFine;
    }

    /**
     * sortList and partition = QuickSort
     * @param listOfLines = ArrayList of all the data read
     * @param listOfIndex = ArrayList of Integers to sort
     * part = partitioning index
     * low, high = start point, end point
     * Recursively sorts elements before partition, and after partition
     * @return  listOfIndex = an ArrayList of sorted Integers, according to listToSort
     */
    public ArrayList<Integer> sortList (ArrayList<String> listOfLines, ArrayList<Integer> listOfIndex, int low, int high){
        if (low < high){
            int part = partition(listOfLines, listOfIndex, low, high);

            sortList(listOfLines, listOfIndex, low, part-1);
            sortList(listOfLines, listOfIndex, part+1, high);
        }
        return listOfIndex;
    }

    /**
     * @param listOfLines =ArrayList of all the data read
     * @param listOfIndex = ArrayList of Integers to sort
     * for loop: checks if current element is smaller than the pivot - swaps element i and element j
     * @return i = partitioning index
     */
    private static int partition(ArrayList<String> listOfLines, ArrayList<Integer> listOfIndex, int low, int high){
        int pivot = listOfIndex.get(high);
        int i = (low);   // index of smaller element
        for (int j=low; j<high; j++){
            if ( listOfLines.get(listOfIndex.get(j)).compareTo(listOfLines.get(pivot)) <= 0){
                int temp = listOfIndex.get(i) ;
                listOfIndex.set(i, listOfIndex.get(j));
                listOfIndex.set(j, temp);
                i++;
            }
        }
        // swaps element i and element at 'high' position (or pivot)
        int temp = listOfIndex.get(i);
        listOfIndex.set(i, listOfIndex.get(high));
        listOfIndex.set(high, temp);

        return i;
    }

    /**
     * @param sortedIndices = an Array of the sorted indices
     * @param numOfSearches = number if randoms to pick
     * method takes an ArrayList of Integers,
     * @return randomList = ArrayList with 'numOfSearches' random Integers
     */
    public ArrayList<Integer> pickRandom(ArrayList<Integer> sortedIndices, int numOfSearches){
        Random rand = new Random();
        ArrayList<Integer> randomList = new ArrayList<>();
        for(int i=0; i<numOfSearches; i++) {
            randomList.add(sortedIndices.get(rand.nextInt(sortedIndices.size()-1)));
        }
        return randomList;
    }
}