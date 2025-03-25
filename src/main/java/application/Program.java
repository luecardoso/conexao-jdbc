package application;

import db.database.Db;
import db.exception.DbIntegrityException;
import db.model.dao.DaoFactory;
import db.model.dao.DepartmentDao;
import db.model.dao.SellerDao;
import db.model.entity.Department;
import db.model.entity.Seller;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.List;


public class Program {
    public static void main(String[] args) {

        System.out.println("_____________________________________");
        SellerDao sellerDao = DaoFactory.createSellerDao();
        Seller seller = sellerDao.findById(2);
        System.out.println(seller);

        System.out.println("_____________________________________");
        Department department = new Department(4, null);
        List<Seller> listSeller = sellerDao.findByDepartment(department);
        listSeller.forEach(System.out::println);

        System.out.println("_____________________________________");
        List<Seller> listSellers = sellerDao.findAll();
        listSellers.forEach(System.out::println);

        System.out.println("_____________________________________");
        Seller newSeller = new Seller(4000.0,new java.util.Date(), "felipe@com", null, "Felipe",department);
        System.out.println(newSeller);
        sellerDao.insert(newSeller);
        System.out.println("Inserted! New id = " + newSeller.getId());

        System.out.println("_____________________________________");
        seller = sellerDao.findById(10);
        seller.setName("Felipe R");
        sellerDao.update(seller);
        System.out.println("Update completed = "+ seller);

        System.out.println("_____________________________________");
        sellerDao.deleteById(11);
        System.out.println("Deleted");

        System.out.println("_____________________________________");
        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
        Department dep = departmentDao.findById(1);
        System.out.println(dep);

        System.out.println("_____________________________________");
        List<Department> listDepartment = departmentDao.findAll();
        listDepartment.forEach(System.out::println);

        System.out.println("_____________________________________");
        Department newDep = new Department(null, "Movies");
        departmentDao.insert(newDep);
        System.out.println("Inserted! New id = " + newDep.getId());

        System.out.println("_____________________________________");
        dep = departmentDao.findById(3);
        dep.setName("Music");
        departmentDao.update(dep);
        System.out.println("Update completed = "+ dep);

        System.out.println("_____________________________________");
        departmentDao.deleteById(5);
        System.out.println("Deleted");
    }

    }