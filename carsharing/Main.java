package carsharing;

import carsharing.db.DataBase;
import carsharing.db.Menu;

import java.util.Scanner;

public class Main {

    private static DataBase db;

    public static void main(String[] args) {
        String dbName = "Test";
        for (int i = 0; i < args.length; i++) {
            if ("-databaseFileName".equals(args[i])) {
                dbName = args[i + 1];
                break;
            }
        }

        db = new DataBase(dbName);
        db.createCompanyTable();
        db.createCarTable();
        db.createCostumerTable();
        new Menu(db);
        db.closeDB();
    }
}