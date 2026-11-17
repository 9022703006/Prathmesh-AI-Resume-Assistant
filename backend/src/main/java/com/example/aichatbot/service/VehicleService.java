package com.example.aichatbot.service;

import com.example.aichatbot.Enum.VehicleType;
import com.example.aichatbot.entity.Vehicle;
import com.example.aichatbot.repository.VehicleRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Profile("!gemini-only")
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle addvehicle(Vehicle vehicle){
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getallvehicle(){
        return vehicleRepository.findAll();
    }

    public Optional<Vehicle> getvehicleid(Long id){
        return vehicleRepository.findById(id);
    }

    public List<Vehicle> getvehicleavail(){
        return vehicleRepository.findByAvailableTrue();
    }

    public List<Vehicle> getname(String name){
        return  vehicleRepository.findByNameIgnoreCase(name);
    }

    public List<Vehicle> getcompnay(String company){
        return vehicleRepository.findByCompanyIgnoreCase(company);
    }

    public List<Vehicle> getType(VehicleType vehicleType){
        return vehicleRepository.findByType(vehicleType);
    }

    public List<Vehicle> getSeats(Integer seats){
        return vehicleRepository.findBySeats(seats);
    }


    // check Vehicle is available or not
    public boolean isVehicleavilable(Long id){
        return vehicleRepository.findById(id).map(Vehicle::isAvailable).orElse(false);
    }

    // book vehicle
    public Vehicle bookvehicle(Long id){
        Vehicle vehicleid = vehicleRepository.findById(id).orElseThrow(()-> new RuntimeException("Vehicle id is not found"));

        if(!vehicleid.isAvailable()){
            throw  new RuntimeException("Vehicle is currently unavailable");
        }

        vehicleid.setAvailable(false);
        return vehicleRepository.save(vehicleid);
    }

    public Vehicle updateVehicle(Long id, Vehicle vehicleinfo){
        Vehicle vehicles = vehicleRepository.findById(id).orElseThrow(()-> new RuntimeException("id is not found"));
        vehicles.setName(vehicleinfo.getName());
        vehicles.setCompany(vehicleinfo.getCompany());
        vehicles.setType(vehicleinfo.getType());
        vehicles.setSeats(vehicleinfo.getSeats());
        vehicles.setAvailable(vehicleinfo.isAvailable());
        return vehicleRepository.save(vehicles);
    }

    public void deletedVehicle(Long id){
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(()-> new RuntimeException("id is not found"));
         vehicleRepository.delete(vehicle);
    }

    public List<Vehicle> searchVehicles(String keyword) {

        String search = keyword.toLowerCase().trim();

        return vehicleRepository.findAll()
                .stream()
                .filter(vehicle ->
                        vehicle.getName().toLowerCase().contains(search)
                                || vehicle.getCompany().toLowerCase().contains(search)
                                || vehicle.getType().name().toLowerCase().contains(search)
                )
                .toList();
    }

}
