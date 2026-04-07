/**
 * UseCase6RoomAllocationService
 *
 * Demonstrates booking confirmation, room allocation,
 * uniqueness enforcement, and inventory synchronization.
 *
 * @author YourName
 * @version 6.0
 */

import java.util.*;

// ----------- Reservation (Booking Request) -----------

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// ----------- Inventory Service -----------

class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementRoom(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("\nUpdated Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
    }
}

// ----------- Booking Request Queue -----------

class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // FIFO removal
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// ----------- Booking Service (Core Logic) -----------

class BookingService {

    private RoomInventory inventory;

    // Track allocated room IDs
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Map room type → allocated IDs
    private Map<String, Set<String>> roomAllocations = new HashMap<>();

    private int roomCounter = 1;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void processBooking(Reservation reservation) {

        String roomType = reservation.getRoomType();

        System.out.println("\nProcessing request for " + reservation.getGuestName());

        // Check availability
        if (inventory.getAvailability(roomType) > 0) {

            // Generate unique room ID
            String roomId = roomType.replace(" ", "").toUpperCase() + "-" + roomCounter++;

            // Ensure uniqueness (Set check)
            if (!allocatedRoomIds.contains(roomId)) {

                allocatedRoomIds.add(roomId);

                // Store allocation
                roomAllocations
                        .computeIfAbsent(roomType, k -> new HashSet<>())
                        .add(roomId);

                // Update inventory immediately
                inventory.decrementRoom(roomType);

                // Confirm booking
                System.out.println("Booking Confirmed!");
                System.out.println("Guest: " + reservation.getGuestName());
                System.out.println("Room Type: " + roomType);
                System.out.println("Assigned Room ID: " + roomId);

            } else {
                System.out.println("Error: Duplicate room ID detected!");
            }

        } else {
            System.out.println("Booking Failed! No rooms available for " + roomType);
        }
    }

    public void displayAllocations() {
        System.out.println("\nRoom Allocations:");

        for (Map.Entry<String, Set<String>> entry : roomAllocations.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
    }
}

// ----------- Main Class -----------

public class Book {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Book My Stay App - v6.0");
        System.out.println(" Reservation & Room Allocation");
        System.out.println("=====================================");

        // Initialize components
        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue queue = new BookingRequestQueue();
        BookingService bookingService = new BookingService(inventory);

        // Add booking requests (FIFO)
        queue.addRequest(new Reservation("Arun", "Single Room"));
        queue.addRequest(new Reservation("Priya", "Single Room"));
        queue.addRequest(new Reservation("Rahul", "Single Room")); // should fail
        queue.addRequest(new Reservation("Anita", "Suite Room"));

        // Process queue
        while (!queue.isEmpty()) {
            Reservation r = queue.getNextRequest();
            bookingService.processBooking(r);
        }

        // Display results
        bookingService.displayAllocations();
        inventory.displayInventory();

        System.out.println("\nAll bookings processed successfully.");
    }
}