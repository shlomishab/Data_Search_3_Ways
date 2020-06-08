package com.company;

import java.sql.*;
import java.util.*;

/**
 * Database Class
 *
 * class creates Database table for all unique lines
 * searches random line, and modified lines
 * presents the total search time for each case
 *
 * @author Shlomi_Shabtai
 */
public class DataBase {

    /**
     * CONNECTION_STRING is the location in which the DataBase is located.
     * In order to run the program on a different machine - please update CONNECTION_STRING as necessary
     * constants definition
     */
    private static final String DB_NAME = "We_Feel_Fine_DB.db";
    private static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\Shlomi.Shab\\Desktop\\Data_Search_Project\\" + DB_NAME;
    private static final String MAIN_TABLE = "Feelings_Main_Table";
    private static final String COLUMN_INDEX = "Line_ID";
    private static final String COLUMN_LINE = "Line";
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + MAIN_TABLE
                                             + " (" + COLUMN_INDEX + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                             + COLUMN_LINE + " TEXT UNIQUE NOT NULL" + ")";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + MAIN_TABLE;
    private static final String TABLE_COUNT = "SELECT COUNT(*) AS count FROM " + MAIN_TABLE;
    private static final String INSERT_PREP = "INSERT INTO " + MAIN_TABLE
                                             + " (" +  COLUMN_LINE + " )"
                                             + "VALUES( ? )";
    private static final String SEARCH_PREP = "SELECT * FROM " + MAIN_TABLE +
                                             " WHERE " + COLUMN_LINE + " = ? ";

    private ArrayList<String> feelFineDB;

    private Connection conn;

    public DataBase(ArrayList<String> feelFineDB) {
        this.feelFineDB = feelFineDB;
    }

    /**
     * opens a Connection using the 'CONNECTION_STRING'
     * @return boolean indicator
     */
    public boolean open(){
        try{
            conn = DriverManager.getConnection(CONNECTION_STRING);
            return true;
        }
        catch (SQLException e){
            System.out.println("OH NO! Couldn't connect to DataBase: " + e.getMessage());
            return false;
        }
    }

    /**
     * closes a not-null Connection
     * @return boolean indicator
     */
    public boolean close(){
        try{
            if(conn != null){
                conn.close();
                conn = null;
            }
            return true;
        }
        catch (SQLException e){
            System.out.println("DARN! Couldn't close connection: " + e.getMessage());
            conn = null;
            return false;
        }
    }

    /**
     * deletes the table and creates a new one
     * calls the insertLines method if the table's size = 0
     * prints the number of unique lines
     * @return boolean indicator
     */
    public boolean createDB(){
        if(conn == null){
            System.out.println("No connection for createDB! Try to open new connection ");
            return false;
        }
        try{
            Statement statement = conn.createStatement();
            statement.execute(DROP_TABLE);     // delete table
            statement.execute(CREATE_TABLE);   //create a table

            ResultSet result = statement.executeQuery(TABLE_COUNT);
            PreparedStatement prepInsert = conn.prepareStatement(INSERT_PREP);
            if(result.getInt("count") == 0) {
                conn.setAutoCommit(false);

                for (String str : feelFineDB) {
                    boolean lineIsIn = insertLines(prepInsert, str);
                }
                conn.commit();
                prepInsert.close();
                conn.setAutoCommit(true);
                result = statement.executeQuery(TABLE_COUNT);
                int uniqueSize = result.getInt("count");
                System.out.println("\nDB was created! ");
                System.out.format("Data size = %,d, Unique lines in table = %,d \n" , feelFineDB.size(), uniqueSize);
            }
            statement.close();
            return true;
        }
        catch (SQLException ex){
            System.out.println("OOPS! Something went wrong: " + ex.getMessage());
            return false;
        }
    }

    /**
     * inserts lines into table using a Prepared Statement
     * recreates the Prepared Statement if needed
     * @param prepInsert = Prepared insert Statement
     * @param lineNow = String at index location
     * @return boolean indicator
     */
    private boolean insertLines(PreparedStatement prepInsert, String lineNow){
        if(conn == null){
            System.out.println("No connection for insertLines! Try to open new connection ");
            return false;
        }
        try {
            prepInsert.setString( 1, lineNow);
            prepInsert.executeUpdate();
            return true;
        }
        catch (SQLException e){
            try {
                conn.prepareStatement(INSERT_PREP);
            }
            catch (SQLException ex){
                System.out.println("YIKES! Couldn't create prepared statement: " + ex.getMessage());
                return false;
            }
            return false;
        }
    }

    /**
     * performs a search in the DataBase
     * print the total search time, and the number of found lines
     * @param lines = lines to search
     * @return boolean indicator
     */
    public boolean searchInDB(ArrayList<String> lines){
        if(conn == null){
            System.out.println("No connection for searchInDB! Try to open new connection ");
            return false;
        }
        int count = 0;
        long totalSearchEndTime;
        try {
            PreparedStatement prepSearch = conn.prepareStatement(SEARCH_PREP);
            long totalSearchStartTime = System.nanoTime();
            for (int i = 0; i < lines.size(); i++) {
                prepSearch.setString(1, lines.get(i));
                ResultSet results = prepSearch.executeQuery();
                while (results.next()) {
//                    System.out.println("Found line: " + lines.get(i));    // prints found lines
                    count++;
                }
            }
            totalSearchEndTime = System.nanoTime();
            System.out.println("\nThe total search time is: " + (double)(totalSearchEndTime - totalSearchStartTime)/1.0E9 + " seconds");
            prepSearch.close();
        } catch (SQLException e) {
            System.out.println("RUN FOR YOUR LIFE! Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        System.out.format("%,d items found\n", count);
        return true;
    }
}
