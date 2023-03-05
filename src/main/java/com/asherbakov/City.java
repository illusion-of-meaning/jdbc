package com.asherbakov;

import lombok.Data;

@Data
public class City {
    private Long id;
    private String name;

    public City(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
