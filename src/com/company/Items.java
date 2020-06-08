package com.company;

import java.util.*;

/**
 * Items Class
 *
 * class assigns a serial number to each line in the Array
 *
 * @author Shlomi_Shabtai
 *
 */

public class Items {

    private ArrayList<String> readLines;
    private  ArrayList<Integer> serialNumber = new ArrayList<>();

    /**
     * generates a serial number for each line
     */
    public Items(ArrayList<String> arrayTests) {
        this.readLines = arrayTests;
        for (int i = 0; i < this.readLines.size(); i++) {
            this.serialNumber.add(i);
        }
    }

    public ArrayList<String> getReadLines() {
        return readLines;
    }

    public ArrayList<Integer> getSerialNumber() {
        return serialNumber;
    }
}
