package db.model.dao;

import db.model.entity.Department;
import db.model.entity.Seller;

import java.util.List;

public interface SellerDao {
    void insert (Seller seller);
    void update (Seller seller);
    void deleteById (Integer id);
    Seller findById (Integer id);
    List<Seller> findAll ();
    List<Seller> findByDepartment (Department department);
}
