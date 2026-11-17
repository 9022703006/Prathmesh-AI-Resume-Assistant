package com.example.aichatbot.service;

import com.example.aichatbot.Enum.BookingStatus;
import com.example.aichatbot.Enum.TripType;
import com.example.aichatbot.RAG.RagService;
import com.example.aichatbot.entity.Booking;
import com.example.aichatbot.entity.Vehicle;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class ChatService {

    private final ChatClient chatClient;
    private final RagService ragService;
    private final VehicleService vehicleService;
    private final BookingService bookingService;

    private Booking currentBooking;

    public ChatService(
            ChatClient.Builder chatClientBuilder,
            RagService ragService) {
        this(chatClientBuilder, ragService, Optional.empty(), Optional.empty());
    }

    public ChatService(
            ChatClient.Builder chatClientBuilder,
            RagService ragService,
            VehicleService vehicleService,
            BookingService bookingService) {
        this(chatClientBuilder, ragService, Optional.ofNullable(vehicleService), Optional.ofNullable(bookingService));
    }

    @Autowired
    public ChatService(
            ChatClient.Builder chatClientBuilder,
            RagService ragService,
            Optional<VehicleService> vehicleService,
            Optional<BookingService> bookingService) {

        this.chatClient = chatClientBuilder.build();
        this.ragService = ragService;
        this.vehicleService = vehicleService.orElse(null);
        this.bookingService = bookingService.orElse(null);
    }

    public String chat(String message) {

        String userMessage = message.trim();

        // Start booking
        if (bookingService != null && vehicleService != null
                && userMessage.toLowerCase().contains("book")
                && currentBooking == null) {

            currentBooking = new Booking();

            return "Sure! Which vehicle would you like to book?";
        }

        // =========================
        // BOOKING FLOW
        // =========================
        if (currentBooking != null && bookingService != null && vehicleService != null) {

            // 1. Vehicle
            if (currentBooking.getVehicle() == null) {

                var vehicles = vehicleService.searchVehicles(userMessage);

                if (vehicles.isEmpty()) {
                    return "I could not find that vehicle. Please enter a valid vehicle name.";
                }

                Vehicle vehicle = vehicles.get(0);

                if (!vehicle.isAvailable()) {
                    return "Sorry, " + vehicle.getName()
                            + " is currently unavailable.";
                }

                currentBooking.setVehicle(vehicle);

                return "Great! You selected "
                        + vehicle.getName()
                        + ". What is your name?";
            }

            // 2. Name
            if (currentBooking.getName() == null) {

                currentBooking.setName(userMessage);

                return "Thank you. What is your email address?";
            }

            // 3. Email
            if (currentBooking.getEmail() == null) {

                currentBooking.setEmail(userMessage);

                return "What is your phone number?";
            }

            // 4. Phone
            if (currentBooking.getPhone() == null) {

                currentBooking.setPhone(userMessage);

                return "What is your address?";
            }

            // 5. Address
            if (currentBooking.getAddress() == null) {

                currentBooking.setAddress(userMessage);

                return "What is your pickup location?";
            }

            // 6. Pickup location
            if (currentBooking.getPickupLocation() == null) {

                currentBooking.setPickupLocation(userMessage);

                return "What is your drop location?";
            }

            // 7. Drop location
            if (currentBooking.getDropLocation() == null) {

                currentBooking.setDropLocation(userMessage);

                return "What is your pickup date? Please use YYYY-MM-DD.";
            }

            // 8. Pickup date
            if (currentBooking.getPickupDate() == null) {

                try {

                    currentBooking.setPickupDate(
                            LocalDate.parse(userMessage)
                    );

                    return "What is your pickup time? Please use HH:mm.";

                } catch (Exception e) {

                    return "Please enter the pickup date in YYYY-MM-DD format.";
                }
            }

            // 9. Pickup time
            if (currentBooking.getPickupTime() == null) {

                try {

                    currentBooking.setPickupTime(
                            LocalTime.parse(userMessage)
                    );

                    return "What is your return date? Please use YYYY-MM-DD.";

                } catch (Exception e) {

                    return "Please enter the pickup time in HH:mm format.";
                }
            }

            // 10. Return date
            if (currentBooking.getReturnDate() == null) {

                try {

                    currentBooking.setReturnDate(
                            LocalDate.parse(userMessage)
                    );

                    return "How many passengers?";

                } catch (Exception e) {

                    return "Please enter the return date in YYYY-MM-DD format.";
                }
            }

            // 11. Passengers
            if (currentBooking.getPassengers() == null) {

                try {

                    currentBooking.setPassengers(
                            Integer.parseInt(userMessage)
                    );

                    return "What is the trip type? "
                            + "For example: ONE_WAY or ROUND_TRIP.";

                } catch (Exception e) {

                    return "Please enter the number of passengers.";
                }
            }

            // 12. Trip type
            if (currentBooking.getTripType() == null) {

                try {

                    currentBooking.setTripType(
                            TripType.valueOf(
                                    userMessage.toUpperCase()
                            )
                    );

                    return "Please confirm your booking for "
                            + currentBooking.getVehicle().getName()
                            + ". Type YES to confirm.";

                } catch (Exception e) {

                    return "Please enter a valid trip type.";
                }
            }

            // 13. Confirmation
            if (userMessage.equalsIgnoreCase("yes")) {

                Vehicle vehicle = currentBooking.getVehicle();

                // Check live database availability again
                if (!vehicle.isAvailable()) {

                    currentBooking = null;

                    return "Sorry, this vehicle is no longer available.";
                }

                currentBooking.setStatus(BookingStatus.PENDING);

                Booking savedBooking =
                        bookingService.addBooking(currentBooking);

                vehicleService.bookvehicle(vehicle.getId());

                String response =
                        "Booking request confirmed successfully! "
                                + "Your booking ID is "
                                + savedBooking.getId()
                                + ".";

                currentBooking = null;

                return response;
            }

            return "Please type YES to confirm the booking.";
        }

        // =========================
        // VEHICLE DATABASE FIRST
        // =========================

        String vehicleContext = getVehicleContext(userMessage);

        if (!vehicleContext.isEmpty()) {

            return chatClient.prompt()
                    .system("""
                            You are a car rental assistant.

                            Answer using only the current vehicle
                            information provided from the database.

                            Vehicle information:
                            """ + vehicleContext)
                    .user(userMessage)
                    .call()
                    .content();
        }

        // =========================
        // RAG / KNOWLEDGE.TXT
        // =========================

        String context = ragService.retrieve(userMessage);

        if (!context.isEmpty()) {

            return chatClient.prompt()
                    .system("""
                            You are a helpful car rental assistant.

                            Answer only using the provided company information.

                            Company information:
                            """ + context)
                    .user(userMessage)
                    .call()
                    .content();
        }

        // =========================
        // FALLBACK
        // =========================

        return chatClient.prompt()
                .user(userMessage)
                .call()
                .content();
    }

    private String getVehicleContext(String message) {

        if (vehicleService == null) {
            return "";
        }

        String lowerMessage = message.toLowerCase();

        if (lowerMessage.contains("available")
                || lowerMessage.contains("availability")
                || lowerMessage.contains("car")
                || lowerMessage.contains("cars")
                || lowerMessage.contains("vehicle")
                || lowerMessage.contains("vehicles")
                || lowerMessage.contains("suv")
                || lowerMessage.contains("sedan")
                || lowerMessage.contains("hatchback")) {

            var vehicles = vehicleService.getvehicleavail();

            if (vehicles.isEmpty()) {
                return "No vehicles are currently available.";
            }   

            StringBuilder context = new StringBuilder();

            for (Vehicle vehicle : vehicles) {

                context.append("Vehicle: ")
                        .append(vehicle.getName())
                        .append(", Company: ")
                        .append(vehicle.getCompany())
                        .append(", Type: ")
                        .append(vehicle.getType())
                        .append(", Seats: ")
                        .append(vehicle.getSeats())
                        .append(", Available: ")
                        .append(vehicle.isAvailable())
                        .append("\n");
            }

            return context.toString();
        }

        return "";
    }
}