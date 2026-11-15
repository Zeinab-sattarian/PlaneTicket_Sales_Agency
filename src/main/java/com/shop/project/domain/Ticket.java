package com.shop.project.domain;

import com.shop.project.base.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = Ticket.TABLE_NAME)
public class Ticket extends BaseEntity<Long> {

    public static final String TABLE_NAME = "ticket";

    @ManyToOne
    private Passenger passenger;

    @ManyToOne
    private Flight flight;

    @Override
    public String toString() {

        return passenger.toString()
                + "   "
                + flight.toString();

    }
}
