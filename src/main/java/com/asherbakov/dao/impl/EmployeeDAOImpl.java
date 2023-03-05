package com.asherbakov.dao.impl;

import com.asherbakov.City;
import com.asherbakov.Employee;
import com.asherbakov.dao.EmployeeDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAOImpl implements EmployeeDAO {
    final Connection connection;

    public EmployeeDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Employee employee) {
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO employee(first_name, last_name, gender, age, city_id) VALUES((?),(?),(?),(?),(?))")) {
            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            preparedStatement.setString(3, employee.getGender());
            preparedStatement.setInt(4, employee.getAge());
            preparedStatement.setLong(5, employee.getCity().getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка добавления сотрудника: " + e);
        }
    }

    @Override
    public Employee getEmployeeById(Long id) {
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM employee e INNER JOIN city c ON e.city_id = c.city_id WHERE e.id = (?)")) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Employee(
                        resultSet.getLong("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("gender"),
                        resultSet.getInt("age"),
                        new City(resultSet.getLong("city_id"), resultSet.getString("city_name"))
                );
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения сотрудника: " + e);
        }
    }

    @Override
    public List<Employee> getAllEmployee() {
        List<Employee> employeeList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM employee e INNER JOIN city c ON e.city_id = c.city_id")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Employee employee = new Employee(
                        resultSet.getLong("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("gender"),
                        resultSet.getInt("age"),
                        new City(resultSet.getLong("city_id"), resultSet.getString("city_name"))
                );
                employeeList.add(employee);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения списка сотрудников: " + e);
        }
        return employeeList;
    }

    @Override
    public void changeEmployee(Long id, Employee employee) {
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("UPDATE employee SET first_name = (?), last_name = (?), gender = (?), age = (?), city_id = (?) WHERE id = (?)")) {
            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            preparedStatement.setString(3, employee.getGender());
            preparedStatement.setInt(4, employee.getAge());
            preparedStatement.setLong(5, employee.getCity().getId());
            preparedStatement.setLong(6, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка обновления данных сотрудника: " + e);
        }
    }

    @Override
    public void removeEmployee(Long id) {
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("DELETE FROM employee WHERE id = (?)")) {
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления сотрудника: " + e);
        }
    }
}
