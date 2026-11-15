package com.shop.project.repository.impl;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.shop.project.domain.Airline;
import com.shop.project.domain.City;
import com.shop.project.domain.Flight;
import com.shop.project.repository.FlightRepositoryExtension;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class FlightRepositoryExtensionImpl implements FlightRepositoryExtension{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Flight> groupAllByOrigin(String order) {
        return entityManager.createQuery("select f from Flight f order by f.origin ".concat(order), Flight.class)
                .getResultList();
    }

    @Override
    public List<Flight> groupAllByDestination(String order) {
        return entityManager.createQuery("select f from Flight f order by f.destination ".concat(order), Flight.class)
                .getResultList();
    }

    @Override
    public List<Flight> groupAllByAirline(String order) {
        return entityManager.createQuery("select f from Flight f order by f.airline.name ".concat(order), Flight.class)
                .getResultList();
    }

    @Override
    public List<Flight> groupAllByPrice(String order) {
        return entityManager.createQuery("select f from Flight f order by f.price ".concat(order), Flight.class)
                .getResultList();
    }

    @Override
    public List<Flight> advancedSearch(City origin, City destination, Airline airline, LocalDateTime startDate, LocalDateTime endDate) {
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Flight> query = cb.createQuery(Flight.class);
        Root<Flight> flight = query.from(Flight.class);

        List<Predicate> predicates = new ArrayList<>();

        if (origin != null) {
            predicates.add(cb.equal(flight.get("origin"), origin));
        }
        if (destination != null) {
            predicates.add(cb.equal(flight.get("destination"), destination));
        }
        if (airline != null) {
            predicates.add(cb.equal(flight.get("airline"), airline));
        }
        if (startDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(flight.get("departureTime"), startDate));
        }
        if (endDate != null) {
            predicates.add(cb.lessThanOrEqualTo(flight.get("departureTime"), endDate));
        }

        query.select(flight).where(cb.and(predicates.toArray(new Predicate[0])));
        
        return entityManager.createQuery(query).getResultList();
    }
    
}
