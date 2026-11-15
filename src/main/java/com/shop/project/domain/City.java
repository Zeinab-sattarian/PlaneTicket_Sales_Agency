package com.shop.project.domain;

import com.shop.project.base.domain.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = City.TABLE_NAME)
public class City extends BaseEntity<Long>{

    public static final String TABLE_NAME = "city";
    
    private String name;
}
