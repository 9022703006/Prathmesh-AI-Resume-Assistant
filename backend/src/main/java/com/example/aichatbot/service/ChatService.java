package com.example.aichatbot.service;

import com.example.aichatbot.Enum.BookingStatus;
import com.example.aichatbot.Enum.TripType;
import com.example.aichatbot.RAG.RagService;
import com.example.aichatbot.entity.Booking;
import com.example.aichatbot.entity.Vehicle;
import com.google.genai.errors.ApiException;
import com.google.genai.errors.ClientException;
import com.google.genai.errors.GenAiIOException;
import com.google.genai.errors.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.ai.retry.TransientAiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    private static final int MAX_RETRIES = 3;
    private static final long INITIAL_DELAY_MS = 1000L;
    private static final double BACKOFF_MULTIPLIER = 2.0;

    private int maxRetries = MAX_RETRIES;
    private long initialDelayMs = INITIAL_DELAY_MS;
    private double backoffMultiplier = BACKOFF_MULTIPLIER;

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

    ChatService(
            ChatClient chatClient,
            RagService ragService,
            VehicleService vehicleService,
            BookingService bookingService) {
        this.chatClient = chatClient;
        this.ragService = ragService;
        this.vehicleService = vehicleService;
        this.bookingService = bookingService;
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

            return executePromptWithRetry(() -> chatClient.prompt()
                    .system("""
                            You are a car rental assistant.

                            Answer using only the current vehicle
                            information provided from the database.

                            Vehicle information:
                            """ + vehicleContext)
                    .user(userMessage)
                    .call()
                    .content());
        }

        // =========================
        // RAG / KNOWLEDGE.TXT
        // =========================

        String context = ragService.retrieve(userMessage);

        if (!context.isEmpty()) {

            return executePromptWithRetry(() -> chatClient.prompt()
                    .system("""
                            You are a helpful car rental assistant.

                            Answer only using the provided company information.

                            Company information:
                            """ + context)
                    .user(userMessage)
                    .call()
                    .content());
        }

        // =========================
        // FALLBACK
        // =========================

        return executePromptWithRetry(() -> chatClient.prompt()
                .user(userMessage)
                .call()
                .content());
    }

    String executePromptWithRetry(Supplier<String> promptAction) {
        int retryCount = 0;
        long currentDelay = initialDelayMs;

        while (true) {
            try {
                String response = promptAction.get();
                return response != null ? response : "";
            } catch (Throwable t) {
                if (isQuotaExceededError(t)) {
                    logger.error("Gemini API quota exceeded (HTTP 429): {}", t.getMessage(), t);
                    return getClearErrorMessage(t);
                }

                if (!isRetryable(t)) {
                    logger.error("Gemini API call failed with non-retryable error: {}", t.getMessage(), t);
                    return getClearErrorMessage(t);
                }

                if (retryCount >= maxRetries) {
                    logger.error("Gemini API call failed after {} retries: {}", maxRetries, t.getMessage(), t);
                    return getClearErrorMessage(t);
                }

                retryCount++;
                logger.warn("Temporary Gemini API error (attempt {} of {}). Retrying in {} ms. Error: {}",
                        retryCount, maxRetries, currentDelay, t.getMessage());

                try {
                    Thread.sleep(currentDelay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    logger.error("Gemini retry backoff interrupted: {}", ie.getMessage(), ie);
                    return "The request was interrupted while waiting to retry. Please try again.";
                }

                currentDelay = (long) (currentDelay * backoffMultiplier);
            }
        }
    }

    private boolean isQuotaExceededError(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            if (current instanceof ApiException apiEx) {
                if (apiEx.code() == 429 || "RESOURCE_EXHAUSTED".equalsIgnoreCase(apiEx.status())) {
                    return true;
                }
            }

            if (current instanceof HttpStatusCodeException httpEx) {
                if (httpEx.getStatusCode().value() == 429) {
                    return true;
                }
            }

            String msg = current.getMessage();
            if (msg != null) {
                String lower = msg.toLowerCase();
                if (lower.contains("429")
                        || lower.contains("quota exceeded")
                        || lower.contains("quota_exceeded")
                        || lower.contains("quota")
                        || lower.contains("rate limit")
                        || lower.contains("rate_limit")
                        || lower.contains("ratelimit")
                        || lower.contains("resource_exhausted")
                        || lower.contains("resource exhausted")
                        || lower.contains("too many requests")) {
                    return true;
                }
            }

            current = current.getCause();
        }
        return false;
    }

    private boolean isNonRetryable(Throwable throwable) {
        if (isQuotaExceededError(throwable)) {
            return true;
        }

        Throwable current = throwable;
        while (current != null) {
            // Google GenAI 4xx client errors (400, 401, 403, 404, etc.)
            if (current instanceof ClientException) {
                return true;
            }

            if (current instanceof ApiException apiEx) {
                int code = apiEx.code();
                if (code >= 400 && code < 500) {
                    return true;
                }
            }

            // Spring AI non-transient error
            if (current instanceof NonTransientAiException) {
                return true;
            }

            // Spring Web 4xx client error
            if (current instanceof HttpStatusCodeException httpEx) {
                int code = httpEx.getStatusCode().value();
                if (code >= 400 && code < 500) {
                    return true;
                }
            }

            // Message-based checks for invalid API keys, authentication, or invalid model names
            String msg = current.getMessage();
            if (msg != null) {
                String lower = msg.toLowerCase();
                if (lower.contains("api key")
                        || lower.contains("api_key")
                        || lower.contains("unauthenticated")
                        || lower.contains("permission_denied")
                        || lower.contains("permission denied")
                        || lower.contains("unauthorized")
                        || lower.contains("forbidden")
                        || lower.contains("invalid model")
                        || lower.contains("model not found")
                        || lower.contains("models/")
                        || lower.contains("is not found")
                        || lower.contains("not_found")
                        || lower.contains("invalid_argument")
                        || lower.contains("invalid argument")
                        || lower.contains("401")
                        || lower.contains("403")
                        || lower.contains("404")) {
                    return true;
                }
            }

            current = current.getCause();
        }
        return false;
    }

    private boolean isRetryable(Throwable throwable) {
        if (isQuotaExceededError(throwable)) {
            return false;
        }

        if (isNonRetryable(throwable)) {
            return false;
        }

        Throwable current = throwable;
        while (current != null) {
            // Google GenAI 5xx server errors (500, 502, 503, 504, etc.)
            if (current instanceof ServerException) {
                return true;
            }

            if (current instanceof ApiException apiEx) {
                int code = apiEx.code();
                if (code == 503 || (code >= 500 && code < 600)) {
                    return true;
                }
            }

            // Spring AI transient errors
            if (current instanceof TransientAiException) {
                return true;
            }

            // Spring Web 5xx errors
            if (current instanceof HttpStatusCodeException httpEx) {
                int code = httpEx.getStatusCode().value();
                if (code == 503 || (code >= 500 && code < 600)) {
                    return true;
                }
            }

            // Network / I/O transient errors
            if (current instanceof SocketTimeoutException
                    || current instanceof ConnectException
                    || current instanceof ResourceAccessException
                    || current instanceof GenAiIOException) {
                return true;
            }

            // Message-based checks for temporary service errors (503 / 5xx)
            String msg = current.getMessage();
            if (msg != null) {
                String lower = msg.toLowerCase();
                if (lower.contains("503")
                        || lower.contains("service unavailable")
                        || lower.contains("unavailable")
                        || lower.contains("502")
                        || lower.contains("bad gateway")
                        || lower.contains("504")
                        || lower.contains("gateway timeout")
                        || lower.contains("500")
                        || lower.contains("internal server error")
                        || lower.contains("transient")) {
                    return true;
                }
            }

            current = current.getCause();
        }
        return false;
    }

    private String getClearErrorMessage(Throwable throwable) {
        if (isQuotaExceededError(throwable)) {
            return "Gemini API quota exceeded. Please try again later after the quota resets.";
        }
        if (isAuthOrApiKeyError(throwable)) {
            return "Unable to process request: Gemini API key or authentication error. Please verify your API key configuration.";
        }
        if (isModelNotFoundError(throwable)) {
            return "Unable to process request: The configured Gemini model was not found or is invalid. Please verify model configuration.";
        }
        if (isClientError(throwable)) {
            return "Unable to process request: Invalid request to Gemini API. Please check your query or configuration.";
        }
        if (isRetryable(throwable)) {
            return "The AI service is temporarily unavailable due to high demand or service maintenance. Please try again in a few moments.";
        }
        return "The AI service encountered an error while processing your request. Please try again later.";
    }

    private boolean isAuthOrApiKeyError(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            if (current instanceof ApiException apiEx) {
                int code = apiEx.code();
                if (code == 401 || code == 403) {
                    return true;
                }
            }
            String msg = current.getMessage();
            if (msg != null) {
                String lower = msg.toLowerCase();
                if (lower.contains("api key")
                        || lower.contains("api_key")
                        || lower.contains("unauthenticated")
                        || lower.contains("permission_denied")
                        || lower.contains("permission denied")
                        || lower.contains("unauthorized")
                        || lower.contains("forbidden")
                        || lower.contains("401")
                        || lower.contains("403")) {
                    return true;
                }
            }
            current = current.getCause();
        }
        return false;
    }

    private boolean isModelNotFoundError(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            if (current instanceof ApiException apiEx) {
                int code = apiEx.code();
                if (code == 404) {
                    return true;
                }
            }
            String msg = current.getMessage();
            if (msg != null) {
                String lower = msg.toLowerCase();
                if (lower.contains("invalid model")
                        || lower.contains("model not found")
                        || lower.contains("models/")
                        || lower.contains("404")) {
                    return true;
                }
            }
            current = current.getCause();
        }
        return false;
    }

    private boolean isClientError(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            if (current instanceof ClientException) {
                return true;
            }
            if (current instanceof ApiException apiEx) {
                int code = apiEx.code();
                if (code >= 400 && code < 500) {
                    return true;
                }
            }
            if (current instanceof NonTransientAiException) {
                return true;
            }
            if (current instanceof HttpStatusCodeException httpEx) {
                int code = httpEx.getStatusCode().value();
                if (code >= 400 && code < 500) {
                    return true;
                }
            }
            current = current.getCause();
        }
        return false;
    }

    void setRetryConfig(int maxRetries, long initialDelayMs, double backoffMultiplier) {
        this.maxRetries = maxRetries;
        this.initialDelayMs = initialDelayMs;
        this.backoffMultiplier = backoffMultiplier;
    }

    boolean isRetryableError(Throwable t) {
        return isRetryable(t);
    }

    boolean isNonRetryableError(Throwable t) {
        return isNonRetryable(t);
    }

    boolean isQuotaError(Throwable t) {
        return isQuotaExceededError(t);
    }

    String resolveClearErrorMessage(Throwable t) {
        return getClearErrorMessage(t);
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