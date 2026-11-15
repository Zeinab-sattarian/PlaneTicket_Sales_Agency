package com.shop.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.project.domain.Passenger;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    Passenger findByUsername(String username);

    Passenger findByEmail(String email);


}
