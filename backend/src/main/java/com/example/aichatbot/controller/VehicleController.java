package com.example.aichatbot.controller;

import com.example.aichatbot.Enum.VehicleType;
import com.example.aichatbot.entity.Vehicle;
import com.example.aichatbot.service.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "http://localhost:4200")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public Vehicle addVehicle(@RequestBody Vehicle vehicle) {
        return vehicleService.addvehicle(vehicle);
    }

    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getallvehicle();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id) {

        Optional<Vehicle> vehicle = vehicleService.getvehicleid(id);

        return vehicle
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/available")
    public List<Vehicle> getAvailableVehicles() {
        return vehicleService.getallvehicle();
    }

    @GetMapping("/{id}/available")
    public boolean isVehicleAvailable(@PathVariable Long id) {
        return vehicleService.isVehicleavilable(id);
    }

    @GetMapping("/name/{name}")
    public List<Vehicle> getByName(@PathVariable String name) {
        return vehicleService.getname(name);
    }


    @GetMapping("/company/{company}")
    public List<Vehicle> getByCompany(@PathVariable String company) {
        return vehicleService.getcompnay(company);
    }


    @GetMapping("/type/{type}")
    public List<Vehicle> getByType(@PathVariable VehicleType type) {
        return vehicleService.getType(type);
    }

    @GetMapping("/seats/{seats}")
    public List<Vehicle> getBySeats(@PathVariable Integer seats) {
        return vehicleService.getSeats(seats);
    }

    // Book vehicle
    @PutMapping("/{id}/book")
    public ResponseEntity<Vehicle> bookVehicle(@PathVariable Long id) {

        try {
            Vehicle vehicle = vehicleService.bookvehicle(id);
            return ResponseEntity.ok(vehicle);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(
            @PathVariable Long id,
            @RequestBody Vehicle vehicleInfo) {

        try {
            Vehicle vehicle = vehicleService.updateVehicle(id, vehicleInfo);
            return ResponseEntity.ok(vehicle);

        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {

        try {
            vehicleService.deletedVehicle(id);
            return ResponseEntity.noContent().build();

        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}