/**
 * UseCase8BookingHistoryReport
 *
 * Demonstrates booking history storage and reporting using List.
 * Maintains insertion order and supports admin reporting.
 *
 * @author YourName
 * @version 8.0
 */

import java.util.*;

// ----------- Reservation (Confirmed Booking) -----------

class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room: " + roomType);
    }
}

// ----------- Booking History -----------

class BookingHistory {

    private List<Reservation> history = new ArrayList<>();

    // Add confirmed booking
    public void addReservation(Reservation reservation) {
        history.add(reservation);
    }

    // Get all bookings (read-only usage)
    public List<Reservation> getAllReservations() {
        return history;
    }
}

// ----------- Reporting Service -----------

class BookingReportService {

    // Display all bookings
    public void displayAllBookings(List<Reservation> reservations) {

        System.out.println("\n--- Booking History ---");

        if (reservations.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : reservations) {
            r.display();
        }
    }

    // Generate summary report
    public void generateSummary(List<Reservation> reservations) {

        System.out.println("\n--- Booking Summary Report ---");

        Map<String, Integer> roomCount = new HashMap<>();

        for (Reservation r : reservations) {
            roomCount.put(
                    r.getRoomType(),
                    roomCount.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        for (Map.Entry<String, Integer> entry : roomCount.entrySet()) {
            System.out.println(entry.getKey() + " → Bookings: " + entry.getValue());
        }

        System.out.println("Total Bookings: " + reservations.size());
    }
}

// ----------- Main Class -----------

public class Book {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Book My Stay App - v8.0");
        System.out.println(" Booking History & Reporting");
        System.out.println("=====================================");

        // Initialize history
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings (from Use Case 6)
        history.addReservation(new Reservation("SINGLEROOM-1", "Arun", "Single Room"));
        history.addReservation(new Reservation("DOUBLEROOM-2", "Priya", "Double Room"));
        history.addReservation(new Reservation("SUITEROOM-3", "Rahul", "Suite Room"));
        history.addReservation(new Reservation("SINGLEROOM-4", "Anita", "Single Room"));

        // Reporting service
        BookingReportService reportService = new BookingReportService();

        // Display all bookings
        reportService.displayAllBookings(history.getAllReservations());

        // Generate summary
        reportService.generateSummary(history.getAllReservations());

        System.out.println("\nReporting completed successfully.");
        System.out.println("No changes made to booking history.");
    }
}