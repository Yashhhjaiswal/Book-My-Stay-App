/**
 * UseCase7AddOnServiceSelection
 *
 * Demonstrates attaching optional services to reservations
 * using Map and List (one-to-many relationship).
 *
 * Core booking and inventory remain unchanged.
 *
 * @author YourName
 * @version 7.0
 */

import java.util.*;

// ----------- Add-On Service -----------

class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    public void displayService() {
        System.out.println(serviceName + " → ₹" + cost);
    }
}

// ----------- Add-On Service Manager -----------

class AddOnServiceManager {

    // Map<ReservationID, List of Services>
    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    // Add services to a reservation
    public void addService(String reservationId, AddOnService service) {

        serviceMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println("Added service: " + service.getServiceName() +
                " to Reservation ID: " + reservationId);
    }

    // Display services for a reservation
    public void displayServices(String reservationId) {

        System.out.println("\nServices for Reservation ID: " + reservationId);

        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No services added.");
            return;
        }

        for (AddOnService service : services) {
            service.displayService();
        }
    }

    // Calculate total add-on cost
    public double calculateTotalCost(String reservationId) {

        List<AddOnService> services = serviceMap.get(reservationId);

        double total = 0;

        if (services != null) {
            for (AddOnService s : services) {
                total += s.getCost();
            }
        }

        return total;
    }
}

// ----------- Main Class -----------

public class Book {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Book My Stay App - v7.0");
        System.out.println(" Add-On Service Selection");
        System.out.println("=====================================\n");

        // Simulated reservation IDs (from Use Case 6)
        String res1 = "SINGLEROOM-1";
        String res2 = "SUITEROOM-2";

        // Initialize service manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Create services
        AddOnService breakfast = new AddOnService("Breakfast", 500);
        AddOnService wifi = new AddOnService("WiFi", 200);
        AddOnService spa = new AddOnService("Spa Access", 1500);

        // Add services to reservations
        manager.addService(res1, breakfast);
        manager.addService(res1, wifi);
        manager.addService(res2, spa);

        // Display services
        manager.displayServices(res1);
        manager.displayServices(res2);

        // Calculate total cost
        System.out.println("\nTotal Add-On Cost for " + res1 + ": ₹" +
                manager.calculateTotalCost(res1));

        System.out.println("Total Add-On Cost for " + res2 + ": ₹" +
                manager.calculateTotalCost(res2));

        System.out.println("\nAdd-on services processed successfully.");
        System.out.println("Core booking and inventory remain unchanged.");
    }
}