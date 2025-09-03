package src;
import java.sql.*;
import java.util.Scanner;

public class RailwayReservationCRUD {
    static final String URL = "jdbc:mysql://localhost:3306/railway";
    static final String USER = "kevin";   // your DB username
    static final String PASS = "pass123";   // your DB password

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/railway", "kevin", "pass123")) {
            while (true) {
                System.out.println("\n--- Railway Reservation System ---");
                System.out.println("1. Book Ticket (INSERT)");
                System.out.println("2. View Tickets (SELECT)");
                System.out.println("3. Update Ticket (UPDATE)");
                System.out.println("4. Cancel Ticket (DELETE)");
                System.out.println("5. Exit");
                System.out.print("Choose option: ");
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1 -> bookTicket(con, sc);
                    case 2 -> viewTickets(con);
                    case 3 -> updateTicket(con, sc);
                    case 4 -> cancelTicket(con, sc);
                    case 5 -> {
                        System.out.println("Exiting... Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid choice, try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Insert
    private static void bookTicket(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter Passenger Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Train No: ");
        int trainNo = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Source: ");
        String source = sc.nextLine();
        System.out.print("Enter Destination: ");
        String dest = sc.nextLine();

        String sql = "INSERT INTO reservation (passenger_name, train_no, source, destination) VALUES (?,?,?,?)";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, name);
            pst.setInt(2, trainNo);
            pst.setString(3, source);
            pst.setString(4, dest);
            pst.executeUpdate();
            System.out.println("Ticket booked successfully!");
        }
    }

    // Select
    private static void viewTickets(Connection con) throws SQLException {
        String sql = "SELECT * FROM reservation";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            System.out.println("\n--- Ticket Records ---");
            while (rs.next()) {
                System.out.printf("ID: %d | Name: %s | Train: %d | %s -> %s%n",
                        rs.getInt("id"), rs.getString("passenger_name"),
                        rs.getInt("train_no"), rs.getString("source"),
                        rs.getString("destination"));
            }
        }
    }

    // Update
    private static void updateTicket(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter Ticket ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter new Destination: ");
        String newDest = sc.nextLine();

        String sql = "UPDATE reservation SET destination=? WHERE id=?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, newDest);
            pst.setInt(2, id);
            int rows = pst.executeUpdate();
            if (rows > 0) System.out.println("Ticket updated successfully!");
            else System.out.println("Ticket ID not found.");
        }
    }

    // Delete
    private static void cancelTicket(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter Ticket ID to cancel: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM reservation WHERE id=?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, id);
            int rows = pst.executeUpdate();
            if (rows > 0) System.out.println("Ticket cancelled successfully!");
            else System.out.println("Ticket ID not found.");
        }
    }
}
