package application;

import db.database.Db;
import db.exception.DbIntegrityException;

import java.sql.*;
import java.text.SimpleDateFormat;


public class Program {
    public static void main(String[] args) {
        
//        select();
//        insert();
//        update();
//        delete();
        transaction();
    }


    public static void select(){
        Connection conn =null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = Db.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM department");
            while (rs.next()) {
                System.out.println(rs.getInt("Id") + " " + rs.getString("Name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Db.closeResultSet(rs);
            Db.closeStatement(stmt);
            Db.closeConnection();
        }
    }

    public static void insert() {
        Connection conn =null;
        PreparedStatement stmt = null;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            conn = Db.getConnection();
            stmt = conn.prepareStatement("INSERT INTO seller (Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                    "VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS); // para retornar o id gerado
            stmt.setString(1, "Lucas");
            stmt.setString(2, "lucas@com");
            stmt.setDate(3, new java.sql.Date(sdf.parse("21/03/1998").getTime()));
            stmt.setDouble(4, 3000.0);
            stmt.setInt(5, 4);
            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                while (rs.next()) {
                    int id = rs.getInt(1);
                    System.out.println("Done! Id = " + id);
                }
            }else{
                System.out.println("No rows affected");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Db.closeStatement(stmt);
            Db.closeConnection();
        }
    }

    public static void update() {
        Connection conn =null;
        PreparedStatement stmt = null;
        try{
            conn = Db.getConnection();
            stmt = conn.prepareStatement("UPDATE seller " +
                    "SET BaseSalary = BaseSalary + ? " +
                    "WHERE (DepartmentId = ?)");
            stmt.setDouble(1, 200.0);
            stmt.setInt(2, 2);
            int rows = stmt.executeUpdate();
            System.out.println("Done! Rows = " + rows);
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            Db.closeStatement(stmt);
            Db.closeConnection();
        }
    }

    public static void delete() {
        Connection conn =null;
        PreparedStatement stmt = null;
        try{
            conn = Db.getConnection();
            stmt = conn.prepareStatement("DELETE FROM seller WHERE Id = ?");
            stmt.setInt(1, 3);
            int rows = stmt.executeUpdate();
            System.out.println("Done! Rows = " + rows);
        }catch (SQLException e) {
            throw new DbIntegrityException(e.getMessage());
        }finally {
            Db.closeStatement(stmt);
            Db.closeConnection();
        }
    }

    public static void transaction() {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = Db.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            int rows1 = stmt.executeUpdate("UPDATE seller " +
                    "SET BaseSalary = 2090 " +
                    "WHERE (DepartmentId = 1)");

            int rows2 = stmt.executeUpdate("UPDATE seller " +
                    "SET BaseSalary = 3090 " +
                    "WHERE (DepartmentId = 2)");
            conn.commit();
            System.out.println("Rows1 = " + rows1 + " Rows2 = " + rows2);


        } catch (SQLException e) {
            try {
                conn.rollback();
                throw new DbIntegrityException("Transaction rolled back! Caused by: " + e.getMessage());
            } catch (SQLException ex) {
                throw new DbIntegrityException("Error trying to rollback! Caused by: " + ex.getMessage());
            }
        }finally {
            Db.closeStatement(stmt);
            Db.closeConnection();
        }
        }
    }