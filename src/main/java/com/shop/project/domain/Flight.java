package com.shop.project.domain;

import com.shop.project.base.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = Flight.TABLE_NAME)
public class Flight extends BaseEntity<Long> {

    public static final String TABLE_NAME = "flight";

    @ManyToOne
    private City origin;

    @ManyToOne
    private City destination;

    @ManyToOne
    private Airline airline;

    private LocalDateTime departureTime;

    private int availableSeats;

    private int price;

    public void fillOneSeat() {
        if (availableSeats>0) {
            availableSeats--;
        }
    }

    public void addOneSeat() {
            availableSeats++;
    }

}
