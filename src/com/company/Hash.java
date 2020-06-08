package com.company;

import org.apache.commons.codec.digest.MurmurHash3;
import java.util.*;
import java.lang.*;
import java.math.BigInteger;

/**
 * Hash Class
 *
 * class builds a hash table for the lines,
 * searches the random lines' hash in the table
 *
 * @author Shlomi_Shabtai
 *
 */

public class Hash {

    private ArrayList<Integer>[] hashTable;
    private ArrayList<String> lines;
    private BigInteger bigSize;

    /**
     * @param hashLong = a long array of length 2
     * @return index as hashLong % hashSize
     */
    private int longA2index(long[] hashLong) {
        BigInteger lHash = BigInteger.valueOf(hashLong[0]).shiftLeft(64).add(BigInteger.valueOf(hashLong[1]));
        return lHash.mod(bigSize).intValue();
    }

    /**
     *hashtable - array of an arrayList with the size of hashSize
     *bigSize - the size of the hash array, as a BigInteger
     */
    public Hash(ArrayList<String> lines_) {
        lines = lines_;
        int hashSize = (int) (((double) lines.size()) * 1.1);
        System.out.format("\nData size = %,d, Hash size = %,d\n", lines.size(), hashSize);
        if (hashSize > 0) {
            hashTable = new ArrayList[hashSize];
            bigSize = BigInteger.valueOf((long) hashSize);
        }
        buildHash();
    }

    /**
     * adds an element to the hash table
     * @param index = the place in the table
     * @param posit = the value to add
     * @return boolean indicator
     */
    private boolean addTo(int index, int posit) {
        if (hashTable[index] == null) {
            hashTable[index] = new ArrayList<>(8);
        }
        // the array was created
        hashTable[index].add(posit);
        return true;
    }

    /**
     * @param index = index to add
     * @param key = string at index location
     * Calculates the hash value, and the index in the table (hIndex)
     * @return the index in the hash table
     */
    private int addKey(int index, String key) {
        long[] hashLong = MurmurHash3.hash128(key);
        int hIndex = longA2index(hashLong);
        if (addTo(hIndex, index)) {
            return hIndex;
        }
        else {
            return -1;
        }
    }

    private int addKey(int id) {
        return addKey(id, lines.get(id));
    }

    /**
     * builds hash table
     * calls the 'addKey' method
     */
    private boolean buildHash() {
        for (int i=0; i<lines.size(); ++i) {
            addKey(i);
        }
        return true;
    }

    /**
     * Calculates the hash value of key, and the hIndex
     * puts the hash in long array
     * searches in the hash table
     * @return int value of the index for the first key found
     */
    public int search(String key) {
        long[] hashLong = MurmurHash3.hash128(key);
        int hIndex = longA2index(hashLong);
        if (hashTable[hIndex] == null){
            return -1;
        }
        for (int i = 0; i < hashTable[hIndex].size(); ++i) {
            if (key.equals(lines.get(hashTable[hIndex].get(i)))) {
                return hashTable[hIndex].get(i);
            }
        }
        return -1;
    }

    /**
     * @param numOfSearches = number of lines to search
     * searches for entries of keys in Hash table
     * prints total search time, and numbers of lines found
     */
    public void lookUpInHshTable(int numOfSearches, String [] keys){
        int count = 0;
        long startTime = System.nanoTime();
        for(int i=0; i < numOfSearches; ++i){
            int ikey = search(keys[i]);
            if (ikey > -1){
                count++;
//                System.out.println("Found at location [" + ikey + "]....." + keys[i] ); // + weFeelFine.get(ikey));   // prints found lines
            }
            else{
//                System.out.println(keys[i] + "     --- Was not found!");    // prints lines that could not be found
            }
        }
        long endTime = System.nanoTime();
        System.out.println("Total search time is: " + (double) (endTime-startTime)/1.0E6 + " milliseconds");
        System.out.format( "%,d Items found\n" , count);
    }

    /**
     * @param feelFine = Array of all the data
     * @param numOfSearches = number of line to search for
     * @returns keys = an Array of line to search
     */
    public  String[] makeHashArray2search (ArrayList<String> feelFine , int numOfSearches){
        Random rand = new Random();
        String[] keys = new String[numOfSearches];
        for(int i=0; i < numOfSearches ; ++i){
            keys[i] = feelFine.get(rand.nextInt(feelFine.size()));
        }
        return keys;
    }
}
