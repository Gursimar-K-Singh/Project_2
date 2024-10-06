package util;
import java.util.Scanner;

public class ClinicManager {

    public ClinicManager() {
    }

    public void run() {
        System.out.println("Clinic Manager is running.");
        // loadProviders();
        // displayProviders();

        Scanner scanner = new Scanner(System.in);
        String command;
        while (true) {

            command = scanner.nextLine().trim(); // Trim leading/trailing whitespace

            // Skip empty commands
            if (command.isEmpty()) {
                continue;
            }

            // Split command into tokens
            String[] tokens = command.split(",");

            // Switch case to handle commands
            switch (tokens[0].toUpperCase()) {
                case "D":
                    // Handle scheduling a new office appointment
                    // Implement scheduleOfficeAppointment(tokens);
                    break;
                case "T":
                    // Handle scheduling a new imaging appointment
                    // Implement scheduleImagingAppointment(tokens);
                    break;
                case "C":
                    // Handle canceling an existing appointment
                    // Implement cancelAppointment(tokens);
                    break;
                case "R":
                    // Handle rescheduling an office appointment
                    // Implement rescheduleAppointment(tokens);
                    break;
                case "PO":
                    // Display office appointments
                    // Implement displayOfficeAppointments();
                    break;
                case "PI":
                    // Display imaging appointments
                    // Implement displayImagingAppointments();
                    break;
                case "PC":
                    // Display provider credits
                    // Implement displayProviderCredits();
                    break;
                case "Q":
                    // Terminate the program
                    System.out.println("Clinic Manager terminated.");
                    scanner.close(); // Close the scanner
                    return; // Exit the method
                default:
                    // Handle unrecognized commands
                    System.out.println("Invalid command: ");
            }
        }
    }




}
