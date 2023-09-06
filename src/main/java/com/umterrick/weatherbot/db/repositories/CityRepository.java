package com.umterrick.weatherbot.db.repositories;

import com.umterrick.weatherbot.db.models.telegram.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    City findByName(String name);

}
