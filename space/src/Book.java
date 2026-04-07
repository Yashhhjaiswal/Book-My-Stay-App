/**
 * UseCase9ErrorHandlingValidation
 *
 * Demonstrates error handling and validation for booking requests.
 * Validates room types, inventory counts, and prevents invalid state.
 *
 * @author YourName
 * @version 9.0
 */

import java.util.*;

// ----------- Custom Exception -----------

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// ----------- Inventory Service -----------

class InventoryService {
    private Map<String, Integer> roomInventory;

    public InventoryService() {
        roomInventory = new HashMap<>();
        roomInventory.put("Single Room", 2);
        roomInventory.put("Double Room", 2);
        roomInventory.put("Suite Room", 1);
    }

    // Validate room type and availability
    public void validateBooking(String roomType, int quantity) throws InvalidBookingException {
        if (!roomInventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
        int available = roomInventory.get(roomType);
        if (quantity <= 0) {
            throw new InvalidBookingException("Quantity must be at least 1.");
        }
        if (quantity > available) {
            throw new InvalidBookingException(
                    "Not enough rooms available for " + roomType + ". Requested: " + quantity + ", Available: " + available
            );
        }
    }

    // Update inventory after successful booking
    public void bookRoom(String roomType, int quantity) {
        int available = roomInventory.get(roomType);
        roomInventory.put(roomType, available - quantity);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : roomInventory.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue() + " available");
        }
    }
}

// ----------- Main Class -----------

public class Book {

    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println("   Book My Stay App - v9.0");
        System.out.println(" Error Handling & Validation");
        System.out.println("======================================");

        InventoryService inventory = new InventoryService();
        Scanner scanner = new Scanner(System.in);

        try {
            // Example 1: valid booking
            System.out.println("\nAttempting valid booking: Single Room, 1 unit");
            inventory.validateBooking("Single Room", 1);
            inventory.bookRoom("Single Room", 1);
            System.out.println("Booking confirmed successfully!");

            // Example 2: invalid room type
            System.out.println("\nAttempting invalid booking: Deluxe Room, 1 unit");
            inventory.validateBooking("Deluxe Room", 1);
            inventory.bookRoom("Deluxe Room", 1); // This will not run

        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        }

        try {
            // Example 3: overbooking
            System.out.println("\nAttempting overbooking: Suite Room, 2 units");
            inventory.validateBooking("Suite Room", 2);
            inventory.bookRoom("Suite Room", 2); // This will not run

        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        }

        // Display inventory after bookings
        inventory.displayInventory();

        System.out.println("\nAll validations completed. System remains stable.");
        scanner.close();
    }
}