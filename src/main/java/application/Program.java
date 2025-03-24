package application;

import db.database.Db;

import java.sql.Connection;

public class Program {
    public static void main(String[] args) {
        Connection connection = Db.getConnection();
        Db.closeConnection();
    }
}
