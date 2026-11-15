package com.shop.project.domain;

import com.shop.project.base.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = Passenger.TABLE_NAME)
public class Passenger extends BaseEntity<Long> {

    public static final String TABLE_NAME = "passenger";

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String username;

    private String password;

    private String phoneNumber;

    @Column(unique = true)
    private String email;
    
    private String ssn;

    private String profilePicFileName;

}
