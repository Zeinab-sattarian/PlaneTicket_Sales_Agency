package com.shop.project.service;


import com.shop.project.base.service.BaseService;
import com.shop.project.domain.Passenger;

public interface PassengerService extends BaseService<Passenger,Long>{

    Passenger findByUsername(String username);

    Passenger createPassenger(String newUsername, String newPassword, String newFirstname, String newLastname, String newEmail, String newPhoneNumber, String newSsn, String newProfilePicFileName);

    Passenger findByEmail(String email);
}
