package com.shop.project.service.impl;

import com.shop.project.base.service.impl.BaseServiceImpl;
import com.shop.project.domain.Passenger;
import com.shop.project.repository.PassengerRepository;
import com.shop.project.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PassengerServiceImpl extends BaseServiceImpl<Passenger, Long, PassengerRepository> implements PassengerService {

    @Autowired
    public PassengerServiceImpl(PassengerRepository repository) {
        super(repository);
    }

    @Override
    @Transactional
    public Passenger findByUsername(String username) {

        if (username != null) {

            try {

                return repository.findByUsername(username);

            } catch (Exception e) {

                return null;

            }

        } else
            return null;

    }

    @Override
    @Transactional
    public Passenger createPassenger(String newUsername, String newPassword, String newFirstname, String newLastname, String newEmail, String newPhoneNumber, String newSsn, String newProfilePicFileName){
        
        if (newProfilePicFileName == null || newProfilePicFileName.isEmpty()){
            newProfilePicFileName = "default_profile.jpg";
        }
        
        Passenger newPassenger = null;

        if (findByUsername(newUsername) == null && findByEmail(newEmail) == null) {

            newPassenger = new Passenger();

            newPassenger.setUsername(newUsername);

            newPassenger.setPassword(newPassword);

            newPassenger.setFirstName(newFirstname);

            newPassenger.setLastName(newLastname);

            newPassenger.setEmail(newEmail);

            newPassenger.setPhoneNumber(newPhoneNumber);

            newPassenger.setSsn(newSsn);
            
            newPassenger.setProfilePicFileName(newProfilePicFileName);

            newPassenger = save(newPassenger);
        }

        return newPassenger;
    }

    public Passenger findByEmail(String email){
        return repository.findByEmail(email);
    }


}
