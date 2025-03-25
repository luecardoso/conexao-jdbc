package db.model.dao;

import db.database.Db;
import db.model.dao.impl.DepartmentDaoJDBC;
import db.model.dao.impl.SellerDaoJDBC;

public class DaoFactory {

    public static SellerDao createSellerDao() {
        return new SellerDaoJDBC(Db.getConnection());
    }

    public static DepartmentDao createDepartmentDao() {
        return new DepartmentDaoJDBC(Db.getConnection());
    }
}
