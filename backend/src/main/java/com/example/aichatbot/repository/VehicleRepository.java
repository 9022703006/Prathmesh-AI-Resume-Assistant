package com.example.aichatbot.repository;

import com.example.aichatbot.Enum.VehicleType;
import com.example.aichatbot.entity.Vehicle;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("!gemini-only")
public interface VehicleRepository extends JpaRepository<Vehicle,Long>{
    List<Vehicle> findByAvailableTrue();
    Optional<Vehicle> findById(Long id);
    List<Vehicle> findByNameIgnoreCase(String name);
    List<Vehicle> findByCompanyIgnoreCase(String company);
    List<Vehicle> findByType(VehicleType type);
    List<Vehicle> findBySeats(Integer seats);

}
