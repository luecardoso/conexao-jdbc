package db.model.dao.impl;

import db.database.Db;
import db.exception.DbException;
import db.model.dao.DepartmentDao;
import db.model.entity.Department;
import db.model.entity.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {

    private Connection connection;

    public DepartmentDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Department department) {
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement("INSERT INTO department " +
                            "(Name)  " +
                            "VALUES  (?) ",
                    Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, department.getName());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    department.setId(id);
                }
                Db.closeResultSet(rs);
            }else {
                throw new DbException("Unexpected error! No rows affected!");
            }

        }catch (SQLException e) {
            throw new DbException(e.getMessage());
        }finally {
            Db.closeStatement(stmt);
        }
    }

    @Override
    public void update(Department department) {
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement("UPDATE department" +
                    "  SET Name = ? " +
                    "WHERE Id = ? ");
            stmt.setString(1, department.getName());
            stmt.setInt(2, department.getId());

            stmt.executeUpdate();

        }catch (SQLException e) {
            throw new DbException(e.getMessage());
        }finally {
            Db.closeStatement(stmt);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement("DELETE FROM department WHERE Id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }catch (SQLException e) {
            throw new DbException(e.getMessage());
        }finally {
            Db.closeStatement(stmt);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = connection.prepareStatement(
                    "SELECT * FROM department WHERE Id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                Department department = new Department();
                department.setId(rs.getInt("Id"));
                department.setName(rs.getString("Name"));
                return department;
            }
            return null;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            Db.closeStatement(st);
            Db.closeResultSet(rs);
        }
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = connection.prepareStatement(
                    "SELECT * FROM department ORDER BY Name");
            rs = st.executeQuery();

            List<Department> list = new ArrayList<>();

            while (rs.next()) {
                Department department = new Department();
                department.setId(rs.getInt("Id"));
                department.setName(rs.getString("Name"));
                list.add(department);
            }
            return list;
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            Db.closeStatement(st);
            Db.closeResultSet(rs);
        }
    }
}
