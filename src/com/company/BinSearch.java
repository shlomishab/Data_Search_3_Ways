package com.company;

import java.util.ArrayList;

/**
 * BinSearch Class
 *
 * class executes a Binary Search algorithm,
 * and modifies the randomly chosen lines
 *
 * @author Shlomi_Shabtai
 *
 */

public class BinSearch {

    private ArrayList<Integer> sortedIndex;
    private ArrayList<String> feelFine;

    public BinSearch(ArrayList<Integer> sortedIndex, ArrayList<String> feelFine) {
        this.sortedIndex = sortedIndex;
        this.feelFine = feelFine;
    }

    public ArrayList<Integer> getSortedIndex() {
        return sortedIndex;
    }

    public ArrayList<String> getFeelFine() {
        return feelFine;
    }

    /**
     * @param linesToSearch = array of 100,000 Strings
     * performs a binary search for the random lines
     * print the total search time, and the number of found lines
     */
    public void lookUpForItems(ArrayList<String> linesToSearch) {
        int count = 0;
        long totalSearchEndTime;
        long totalSearchStartTime = System.nanoTime();
        for (int i = 0; i < linesToSearch.size(); i++) {
            int low = 0;
            int high = this.sortedIndex.size() - 1;
            boolean flag = false;

            //binary search:
            while (low <= high) {
                int mid = (low + high) / 2;
                String midStr = this.feelFine.get(this.sortedIndex.get(mid));
                int comp = midStr.compareTo(linesToSearch.get(i));
                if (comp < 0) {
                    low = mid + 1;
                } else if (comp > 0) {
                    high = mid - 1;
                } else {
//                    System.out.println("Found at location [" + this.sortedIndex.get(mid) + "]....." + linesToSearch.get(i));   // prints found linesToSearch
                    count++;
                    flag = true;
                    break;
                }
            }
            if(!flag){
//                System.out.println(linesToSearch.get(i)  + "     --- Was not found");   // prints linesToSearch that could not be found
            }
        }
        totalSearchEndTime = System.nanoTime();
        System.out.println("\nThe total search time is: " + (double)(totalSearchEndTime - totalSearchStartTime)/1.0E6 + " milliseconds");
        System.out.format( "%,d Items found\n" , count);
    }

    /**
     * @param randomFeelFine = the random lines
     * modifies the random array into a new array
     * @return modifiedFeelFine = an Array with the modified line
     */
    public ArrayList<String> modifyRandomList (ArrayList<Integer> randomFeelFine){
        ArrayList<String> modifiedFeelFine = new ArrayList<>();
        for(int i = 0; i < randomFeelFine.size(); i++){
            modifiedFeelFine.add(this.feelFine.get(randomFeelFine.get(i)) + "jk");
        }
        System.out.println(" ");
        System.out.println("The list was modified.");
        System.out.println("\nBinary search for modified list:");

        return modifiedFeelFine;
    }
}
