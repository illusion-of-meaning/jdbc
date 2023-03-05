package com.asherbakov.dao.impl;

import com.asherbakov.City;
import com.asherbakov.dao.CityDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CityDAOImpl implements CityDAO {
    final Connection connection;

    public CityDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public City getCityById(Long id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM city WHERE city_id = (?)")) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new City(resultSet.getLong("city_id"), resultSet.getString("city_name"));
            } else {
                throw new IllegalArgumentException("Не найден объект с id = " + id);
            }
        }
    }
}
