package com.shop.project.domain;

import com.shop.project.base.domain.BaseEntity;
import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;


@Data
@Entity
@Table(name = Airline.TABLE_NAME)
public class Airline extends BaseEntity<Long> {

    public static final String TABLE_NAME = "airline";

    private String name;

    private String password;

    private String callSign;

    private String hub;

    private String logoFileName;

}
