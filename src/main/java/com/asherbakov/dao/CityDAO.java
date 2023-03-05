package com.asherbakov.dao;

import com.asherbakov.City;

import java.sql.SQLException;

public interface CityDAO {
    City getCityById(Long id) throws SQLException;
}
