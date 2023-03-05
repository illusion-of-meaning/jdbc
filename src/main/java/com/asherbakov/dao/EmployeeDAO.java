package com.asherbakov.dao;

import com.asherbakov.Employee;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeDAO {
    void create(Employee employee) throws SQLException;

    Employee getEmployeeById(Long id) throws SQLException;

    List<Employee> getAllEmployee() throws SQLException;

    void changeEmployee(Long id, Employee employee) throws SQLException;

    void removeEmployee(Long id) throws SQLException;
}
