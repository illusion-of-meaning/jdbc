package com.asherbakov;

import com.asherbakov.dao.CityDAO;
import com.asherbakov.dao.EmployeeDAO;
import com.asherbakov.dao.impl.CityDAOImpl;
import com.asherbakov.dao.impl.EmployeeDAOImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.List;
import java.util.Optional;

public class Application {
    final static String user = "postgres";
    final static String password = "adminpos";
    final static String url = "jdbc:postgresql://localhost:5432/skypro";

    public static void main(String[] args) {
        // task 1
        requestToDatabaseHW1();

        // task 2
        requestToDatabaseHW2();
    }

    public static void requestToDatabaseHW1() {
        try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT e.first_name, e.last_name, e.gender, e.age, c.city_name FROM employee e INNER JOIN city c ON e.id = c.city_id WHERE e.id = (?)"
             )) {
            statement.setInt(1, 1);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println("Имя: " + resultSet.getString("first_name"));
                System.out.println("Фамилия: " + resultSet.getString("last_name"));
                System.out.println("Пол: " + resultSet.getString("gender"));
                System.out.println("Возраст: " + resultSet.getString("age"));
                System.out.println("Город: " + resultSet.getString("city_name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void requestToDatabaseHW2() {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            EmployeeDAO employeeDAO = new EmployeeDAOImpl(connection);
            CityDAO cityDAO = new CityDAOImpl(connection);
            // 1. Создание(добавление) сущности Employee в таблицу
            City city = cityDAO.getCityById(4L);
            employeeDAO.create(new Employee("Anthon", "Schvetsov", "Male", 21, city));

            // 2. Получение конкретного объекта Employee по id
            System.out.println("\nДля получения объекта базы данных, введите его id: ");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            Long employeeId = Long.valueOf(reader.readLine());
            Optional<Employee> employee = Optional.ofNullable(employeeDAO.getEmployeeById(employeeId));
            if (employee.isPresent()) {
                System.out.println(employee.get());
            } else {
                System.out.println("Объект не найден.");
            }

            // 3. Получение списка всех объектов Employee из базы
            System.out.println("\nПолучение списка всех объектов Employee из базы:");
            List<Employee> employeeList = employeeDAO.getAllEmployee();
            if (!employeeList.isEmpty()) {
                employeeList.forEach(System.out::println);
            } else {
                System.out.println("Список пуст.");
            }

            // 4. Изменение конкретного объекта Employee в базе по id
            Employee employeeToChange = new Employee("Danil", "Schvetsov", "Male", 26, city);
            employeeDAO.changeEmployee(1L, employeeToChange);

            // 5. Удаление конкретного объекта Employee из базы по id
            employeeDAO.removeEmployee(3L);

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
