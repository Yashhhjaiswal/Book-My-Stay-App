import java.util.*;

// ----------- Reservation Class (Represents a booking request) -----------

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

    public void displayReservation() {
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
    }
}

// ----------- Booking Request Queue -----------

class BookingRequestQueue {

    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add request to queue (FIFO)
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added for " + reservation.getGuestName());
    }

    // View all requests
    public void displayQueue() {
        System.out.println("\nCurrent Booking Requests Queue:\n");

        if (queue.isEmpty()) {
            System.out.println("No booking requests.");
            return;
        }

        for (Reservation r : queue) {
            r.displayReservation();
        }
    }

    // Peek next request (without removing)
    public void peekNextRequest() {
        System.out.println("\nNext Request to Process:");

        Reservation r = queue.peek();
        if (r != null) {
            r.displayReservation();
        } else {
            System.out.println("Queue is empty.");
        }
    }
}

// ----------- Main Class -----------

public class Book {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Book My Stay App - v5.0");
        System.out.println(" Booking Request Queue (FIFO)");
        System.out.println("=====================================\n");

        // Initialize queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Simulate incoming booking requests
        bookingQueue.addRequest(new Reservation("Arun", "Single Room"));
        bookingQueue.addRequest(new Reservation("Priya", "Double Room"));
        bookingQueue.addRequest(new Reservation("Rahul", "Suite Room"));

        // Display queue
        bookingQueue.displayQueue();

        // Peek next request (FIFO behavior)
        bookingQueue.peekNextRequest();

        System.out.println("\nAll requests stored in arrival order.");
        System.out.println("No inventory changes made at this stage.");
    }
}