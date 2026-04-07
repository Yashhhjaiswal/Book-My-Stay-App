import java.util.HashMap;
import java.util.Map;

// Inventory Class (Centralized Management)
class RoomInventory {

    private Map<String, Integer> inventory;

    // Constructor - Initialize inventory
    public RoomInventory() {
        inventory = new HashMap<>();

        // Initial room availability
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Get availability
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability
    public void updateAvailability(String roomType, int newCount) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, newCount);
        } else {
            System.out.println("Room type not found!");
        }
    }

    // Display full inventory
    public void displayInventory() {
        System.out.println("Current Room Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " → Available: " + entry.getValue());
        }
    }
}

// Main Class
public class Book {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Book My Stay App - v3.0");
        System.out.println(" Centralized Room Inventory");
        System.out.println("=====================================\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Display initial inventory
        inventory.displayInventory();
        System.out.println();

        // Example: Check availability
        System.out.println("Checking availability for Single Room:");
        System.out.println("Available: " + inventory.getAvailability("Single Room"));
        System.out.println();

        // Example: Update availability
        System.out.println("Updating Single Room availability to 4...\n");
        inventory.updateAvailability("Single Room", 4);

        // Display updated inventory
        inventory.displayInventory();

        System.out.println("\nApplication terminated successfully.");
    }
}