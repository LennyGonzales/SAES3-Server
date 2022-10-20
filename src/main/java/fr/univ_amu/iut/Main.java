package fr.univ_amu.iut;

import fr.univ_amu.iut.database.Database;

public class Main {
    public static Database database;
    public static void main(String[] args) {
        database = new Database();
        System.out.println(database.getConnection());
        database.closeConnection();
    }
}
