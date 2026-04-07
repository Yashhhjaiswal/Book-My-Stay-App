/**
 * UseCase12DataPersistenceRecovery
 *
 * Demonstrates persistence and recovery of the Book My Stay App system state
 * using Java serialization to save and restore booking history and inventory.
 *
 * Author: YourName
 * Version: 12.0
 */

import java.io.*;
import java.util.*;

// ----------- Serializable Reservation Class -----------
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    String reservationId;
    String roomType;

    public Reservation(String reservationId, String roomType) {
        this.reservationId = reservationId;
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return reservationId + " (" + roomType + ")";
    }
}

// ----------- Serializable Inventory Service -----------
class InventoryService implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> roomInventory;

    public InventoryService() {
        roomInventory = new HashMap<>();
        roomInventory.put("Single Room", 5);
        roomInventory.put("Double Room", 3);
        roomInventory.put("Suite Room", 2);
    }

    public boolean bookRoom(String roomType) {
        int available = roomInventory.getOrDefault(roomType, 0);
        if (available > 0) {
            roomInventory.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public void releaseRoom(String roomType) {
        roomInventory.put(roomType, roomInventory.getOrDefault(roomType, 0) + 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : roomInventory.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue() + " available");
        }
    }
}

// ----------- Persistence Service -----------
class PersistenceService {

    private static final String FILE_PATH = "bookMyStayData.ser";

    public static void saveState(InventoryService inventory, List<Reservation> history) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(inventory);
            oos.writeObject(history);
            System.out.println("\n[INFO] System state saved successfully.");
        } catch (IOException e) {
            System.out.println("[ERROR] Failed to save system state: " + e.getMessage());
        }
    }

    public static Object[] restoreState() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("[INFO] No saved state found. Starting fresh.");
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            InventoryService inventory = (InventoryService) ois.readObject();
            List<Reservation> history = (List<Reservation>) ois.readObject();
            System.out.println("[INFO] System state restored successfully.");
            return new Object[]{inventory, history};
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[ERROR] Failed to restore system state: " + e.getMessage());
            return null;
        }
    }
}

// ----------- Main Class -----------
public class Book {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("   Book My Stay App - v12.0");
        System.out.println("  Data Persistence & Recovery Demo");
        System.out.println("======================================");

        // Restore previous state if available
        Object[] restored = PersistenceService.restoreState();
        InventoryService inventory;
        List<Reservation> bookingHistory;

        if (restored != null) {
            inventory = (InventoryService) restored[0];
            bookingHistory = (List<Reservation>) restored[1];
        } else {
            inventory = new InventoryService();
            bookingHistory = new ArrayList<>();
        }

        // Simulate bookings
        Reservation res1 = new Reservation("RES1201", "Single Room");
        if (inventory.bookRoom(res1.roomType)) bookingHistory.add(res1);

        Reservation res2 = new Reservation("RES1202", "Suite Room");
        if (inventory.bookRoom(res2.roomType)) bookingHistory.add(res2);

        Reservation res3 = new Reservation("RES1203", "Double Room");
        if (inventory.bookRoom(res3.roomType)) bookingHistory.add(res3);

        // Display current state
        inventory.displayInventory();
        System.out.println("\nBooking History:");
        for (Reservation res : bookingHistory) {
            System.out.println(res);
        }

        // Save state for next startup
        PersistenceService.saveState(inventory, bookingHistory);

        System.out.println("\nSystem ready for next session. Shutdown complete.");
    }
}