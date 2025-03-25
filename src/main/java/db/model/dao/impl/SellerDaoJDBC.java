package db.model.dao.impl;

import db.database.Db;
import db.exception.DbException;
import db.model.dao.SellerDao;
import db.model.entity.Department;
import db.model.entity.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private Connection connection;

    public SellerDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Seller seller) {
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement("INSERT INTO seller " +
                    "(Name, Email, BirthDate, BaseSalary, DepartmentId)  " +
                    "VALUES  (?, ?, ?, ?, ?) ",
                    Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, seller.getName());
            stmt.setString(2, seller.getEmail());
            stmt.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
            stmt.setDouble(4, seller.getBaseSalary());
            stmt.setInt(5, seller.getDepartment().getId());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    seller.setId(id);
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
    public void update(Seller seller) {
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement("UPDATE seller" +
                            "  SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ?  " +
                            "WHERE Id = ? ");
            stmt.setString(1, seller.getName());
            stmt.setString(2, seller.getEmail());
            stmt.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
            stmt.setDouble(4, seller.getBaseSalary());
            stmt.setInt(5, seller.getDepartment().getId());
            stmt.setInt(6, seller.getId());

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
            stmt = connection.prepareStatement("DELETE FROM seller WHERE Id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }catch (SQLException e) {
            throw new DbException(e.getMessage());
        }finally {
            Db.closeStatement(stmt);
        }
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.prepareStatement("SELECT seller.*,department.Name as DepName  " +
                    "FROM seller " +
                    "INNER JOIN department  " +
                    "ON seller.DepartmentId = department.Id  " +
                    "WHERE seller.Id = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if(rs.next()){
                Department dep = instantiateDepartment(rs);
                Seller seller = instantiateSeller(rs, dep);
                return seller;
            }
            return null;
        }catch (SQLException e) {
            throw new DbException(e.getMessage());
        }finally {
            Db.closeResultSet(rs);
            Db.closeStatement(stmt);
        }
    }

    private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
        Seller seller = new Seller();
        seller.setId(rs.getInt("Id"));
        seller.setName(rs.getString("Name"));
        seller.setEmail(rs.getString("Email"));
        seller.setBaseSalary(rs.getDouble("BaseSalary"));
        seller.setBirthDate(rs.getDate("BirthDate"));
        seller.setDepartment(dep);
        return seller;
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.prepareStatement("SELECT seller.*,department.Name as DepName  " +
                    "FROM seller " +
                    "INNER JOIN department  " +
                    "ON seller.DepartmentId = department.Id " +
                    "ORDER BY Name ");
            rs = stmt.executeQuery();

            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while(rs.next()){
                Department dep = map.get(rs.getInt("DepartmentId"));
                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }

                Seller seller = instantiateSeller(rs, dep);
                list.add(seller);
            }
            return list;
        }catch (SQLException e) {
            throw new DbException(e.getMessage());
        }finally {
            Db.closeResultSet(rs);
            Db.closeStatement(stmt);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.prepareStatement("SELECT seller.*,department.Name as DepName  " +
                    "FROM seller " +
                    "INNER JOIN department  " +
                    "ON seller.DepartmentId = department.Id " +
                    "WHERE DepartmentId = ? ORDER BY Name ");
            stmt.setInt(1, department.getId());
            rs = stmt.executeQuery();

            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while(rs.next()){
                Department dep = map.get(rs.getInt("DepartmentId"));
                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }

                Seller seller = instantiateSeller(rs, dep);
                list.add(seller);
            }
            return list;
        }catch (SQLException e) {
            throw new DbException(e.getMessage());
        }finally {
            Db.closeResultSet(rs);
            Db.closeStatement(stmt);
        }
    }


}
