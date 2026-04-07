/**
 * UseCase11ConcurrentBookingSimulation
 *
 * Demonstrates concurrent booking requests with thread safety
 * using synchronized blocks to protect shared inventory.
 *
 * Author: YourName
 * Version: 11.0
 */

import java.util.*;
import java.util.concurrent.*;

// ----------- Reservation Class -----------

class Reservation {
    String reservationId;
    String roomType;

    public Reservation(String reservationId, String roomType) {
        this.reservationId = reservationId;
        this.roomType = roomType;
    }
}

// ----------- Inventory Service with Thread Safety -----------

class InventoryService {
    private Map<String, Integer> roomInventory;

    public InventoryService() {
        roomInventory = new HashMap<>();
        roomInventory.put("Single Room", 2);
        roomInventory.put("Double Room", 2);
        roomInventory.put("Suite Room", 1);
    }

    // Thread-safe booking
    public synchronized boolean bookRoom(String roomType) {
        int available = roomInventory.getOrDefault(roomType, 0);
        if (available > 0) {
            roomInventory.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public synchronized void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : roomInventory.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue() + " available");
        }
    }
}

// ----------- Booking Processor (Runnable) -----------

class BookingProcessor implements Runnable {
    private Reservation reservation;
    private InventoryService inventory;

    public BookingProcessor(Reservation reservation, InventoryService inventory) {
        this.reservation = reservation;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        System.out.println("Processing " + reservation.reservationId + " for " + reservation.roomType + " by " + Thread.currentThread().getName());
        boolean booked = inventory.bookRoom(reservation.roomType);
        if (booked) {
            System.out.println("Booking confirmed: " + reservation.reservationId + " (" + reservation.roomType + ")");
        } else {
            System.out.println("Booking failed (sold out): " + reservation.reservationId + " (" + reservation.roomType + ")");
        }
    }
}

// ----------- Main Class -----------

public class Book {
    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println("   Book My Stay App - v11.0");
        System.out.println(" Concurrent Booking Simulation");
        System.out.println("======================================");

        InventoryService inventory = new InventoryService();

        // Sample reservations
        List<Reservation> reservations = Arrays.asList(
                new Reservation("RES1101", "Single Room"),
                new Reservation("RES1102", "Single Room"),
                new Reservation("RES1103", "Single Room"), // should fail
                new Reservation("RES1104", "Double Room"),
                new Reservation("RES1105", "Double Room"),
                new Reservation("RES1106", "Double Room"), // should fail
                new Reservation("RES1107", "Suite Room"),
                new Reservation("RES1108", "Suite Room")   // should fail
        );

        // ExecutorService to simulate concurrent booking
        ExecutorService executor = Executors.newFixedThreadPool(4);

        for (Reservation res : reservations) {
            executor.execute(new BookingProcessor(res, inventory));
        }

        // Shutdown executor and wait for all tasks to finish
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        inventory.displayInventory();
        System.out.println("\nConcurrent booking simulation completed successfully.");
    }
}