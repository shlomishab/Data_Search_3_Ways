package com.company;

import java.util.*;

/**
 *  Data_Search_Project
 *  Searching data in 3 different ways:
 *  Binary search, Hash search, SQLite query
 *  and comparing total search times
 *
 * @author Shlomi_Shabtai
 * @ID = 304895402
 * @data Jan. 2020
 *
 *
 *
 * DataProject Class
 *
 * holds arrays, creates objects, calls methods
 * that are needed for the project
 */
public class DataProject {

    public static void main(String[] args) {
        /**
         *      filesLocation is the location in which the data is located
         *      In order to run the program on a different machine - please update filesLocation as necessary
         */
        String filesLocation = "C:\\Users\\Shlomi.Shab\\Desktop\\Data_Search_Project\\data\\";

        ArrayMgt arr = new ArrayMgt(filesLocation);
        ArrayList<String> weFeelFine = arr.readToArray();
        System.out.format("\nRead total of %,d lines.\n" , weFeelFine.size());

        int numOfSearches = 100000;

        // creates the serial number array
        Items items = new Items(weFeelFine);

        /**
         * BINARY
         */
        System.out.println("\n****Binary****");

        // sorts, and creates the array of findable lines, then searches for the array
        ArrayList<Integer> sortedIndexList = arr.sortList(weFeelFine, items.getSerialNumber(), 0 , weFeelFine.size()-1);
        ArrayList<Integer> randomIndexFeel = arr.pickRandom(sortedIndexList, numOfSearches);
        BinSearch binSearch = new BinSearch(sortedIndexList, weFeelFine);

        ArrayList<String> randomLines = new ArrayList<>();

        for (int num : randomIndexFeel) {
            randomLines.add(weFeelFine.get(num));
        }

        System.out.println("\nBinary Search for the random list:");
        binSearch.lookUpForItems(randomLines);

        // modifies the lines, and searches again
        ArrayList<String> modifiedLines = binSearch.modifyRandomList(randomIndexFeel);

        binSearch.lookUpForItems(modifiedLines);

        /**
         * HASH
         */
        System.out.println("\n****Hash****");

        // creates the hash table
        Hash hTable = new Hash(weFeelFine);

        // creates the set of findable, and searches for the set
        String[] keys = hTable.makeHashArray2search(weFeelFine, numOfSearches);
        System.out.println("\nHash search for the findable list:\n");
        hTable.lookUpInHshTable(numOfSearches, keys);

        // modifies the lines, and searches again
        for(int i=0; i < numOfSearches; ++i){
            keys[i] = keys[i] + "jk";
        }
        System.out.println("\nThe list was modified.");
        System.out.println("\nHash search for modified list:\n");
        hTable.lookUpInHshTable(numOfSearches, keys);

        /**
         * DATABASE
         */
        System.out.println("\n****Database****");

        // opens a connection and builds a table
        DataBase db = new DataBase(weFeelFine);
        if(!db.open()){
            System.out.println("FOR CRYING OUT LOUD! Can't open database!");
            return;
        }
        if(!db.createDB()){
            System.out.println("Error while creating DB: ");
            return;
        }

        // search the randoms in DB
        System.out.println("\nSearching random lines in DataBase:");
        if(!db.searchInDB(randomLines)){
            System.out.println("Error while searching DB: ");
            return;
        }

        //uses the modifiedLines Array to search again in DB
        System.out.println("\nThe list was modified.");
        System.out.println("\nSearching modified lines in DataBase:");
        if(!db.searchInDB(modifiedLines)){
            System.out.println("Error while searching DB: ");
            return;
        }
        db.close();

    }
}
