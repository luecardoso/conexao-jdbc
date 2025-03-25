package db.model.dao;

import db.model.entity.Department;

import java.util.List;

public interface DepartmentDao {
    void insert (Department department);
    void update (Department department);
    void deleteById (Integer id);
    Department findById (Integer id);
    List<Department> findAll ();

}
