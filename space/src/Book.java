/**
 * UseCase10BookingCancellation
 *
 * Demonstrates safe booking cancellations and inventory rollback
 * using a Stack for rollback tracking.
 *
 * Author: YourName
 * Version: 10.0
 */

import java.util.*;

// ----------- Reservation Class -----------

class Reservation {
    String reservationId;
    String roomType;

    public Reservation(String reservationId, String roomType) {
        this.reservationId = reservationId;
        this.roomType = roomType;
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

    public boolean isAvailable(String roomType) {
        return roomInventory.getOrDefault(roomType, 0) > 0;
    }

    public void bookRoom(String roomType) {
        int available = roomInventory.get(roomType);
        roomInventory.put(roomType, available - 1);
    }

    public void cancelRoom(String roomType) {
        int available = roomInventory.get(roomType);
        roomInventory.put(roomType, available + 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : roomInventory.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue() + " available");
        }
    }
}

// ----------- Cancellation Service -----------

class CancellationService {
    private Stack<String> cancelledReservations; // Stack for LIFO rollback
    private Map<String, Reservation> activeReservations;
    private InventoryService inventory;

    public CancellationService(InventoryService inventory) {
        this.inventory = inventory;
        cancelledReservations = new Stack<>();
        activeReservations = new HashMap<>();
    }

    // Add reservation
    public void addReservation(Reservation res) {
        activeReservations.put(res.reservationId, res);
    }

    // Cancel reservation
    public void cancelReservation(String reservationId) {
        if (!activeReservations.containsKey(reservationId)) {
            System.out.println("Cancellation failed: Reservation ID " + reservationId + " does not exist.");
            return;
        }

        Reservation res = activeReservations.remove(reservationId);
        cancelledReservations.push(res.reservationId); // Track rollback
        inventory.cancelRoom(res.roomType);
        System.out.println("Reservation " + reservationId + " for " + res.roomType + " cancelled successfully!");
    }

    public void displayCancelledStack() {
        System.out.println("\nRecently Cancelled Reservations (LIFO):");
        for (String resId : cancelledReservations) {
            System.out.println(resId);
        }
    }
}

// ----------- Main Class -----------

public class Book {
    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println("   Book My Stay App - v10.0");
        System.out.println(" Booking Cancellation & Rollback");
        System.out.println("======================================");

        InventoryService inventory = new InventoryService();
        CancellationService cancellationService = new CancellationService(inventory);

        // Sample reservations
        Reservation r1 = new Reservation("RES1001", "Single Room");
        Reservation r2 = new Reservation("RES1002", "Double Room");
        Reservation r3 = new Reservation("RES1003", "Suite Room");

        // Book rooms
        inventory.bookRoom(r1.roomType);
        inventory.bookRoom(r2.roomType);
        inventory.bookRoom(r3.roomType);

        // Add to active reservations
        cancellationService.addReservation(r1);
        cancellationService.addReservation(r2);
        cancellationService.addReservation(r3);

        inventory.displayInventory();

        // Cancel reservations
        System.out.println("\nCancelling reservations...");
        cancellationService.cancelReservation("RES1002"); // valid
        cancellationService.cancelReservation("RES9999"); // invalid
        cancellationService.cancelReservation("RES1003"); // valid

        inventory.displayInventory();
        cancellationService.displayCancelledStack();

        System.out.println("\nAll cancellations processed. System state restored safely.");
    }
}