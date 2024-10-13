package model;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ClinicManager {
    private List<Provider> providers; // List to hold all providers
    private List<Appointment> appointments; // List to hold all appointments
    private List<Technician> technicians; // Circular list for technicians

    public ClinicManager() {
        providers = new List<>();
        appointments = new List<>();
        technicians = new CircleList<>();
        loadProviders();
        sortProviders();
        displayProviders();
        System.out.println("Clinic Manager is running.");
    }

    // Method to run the command processor
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String commandLine = scanner.nextLine().trim();
            //Q-command: to terminate ClinicManager
            if (commandLine.equalsIgnoreCase("Q")) {
                System.out.println("Clinic Manager terminated.");
                break;
            }
            processCommand(commandLine);
        }
        scanner.close();
    }

    // Loads providers from the "providers.txt" file
    private void loadProviders() {
        try {
            Scanner fileScanner = new Scanner(new File("providers.txt"));
            while (fileScanner.hasNextLine()) {
                String[] tokens = fileScanner.nextLine().trim().split("\\s+");
                if (tokens.length > 0) {
                    switch(tokens[0]){
                    case "D": //Doctor
                        providers.add(new Doctor(tokens[1], tokens[2], tokens[3], tokens[4], tokens[5], tokens[6]));
                        break;
                    case "T": //Technician
                        technicians.add(new Technician(tokens[1], tokens[2], tokens[3], tokens[4]));
                        break;
                    }
                }
            }
            System.out.println("Providers loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("Error: providers.txt not found.");
        }
    }

    //Sorts providers by profile (default assumer Profile class implements Comparable)
    private void sortProviders(){
        Sort.provider(providers);//Calling Sort class
    }

    //Displays sorted providers
    private void displayProviders(){
        System.out.println("List of providers:");
        for(Provider provider: providers){
            System.out.println(provider);
        }
    }

    // Method to process user commands
    private void processCommand(String commandLine) {
        String[] tokens = commandLine.split(",");
        String command = tokens[0].toUpperCase();

        switch (command) {
            case "D":
                scheduleOfficeAppointment(tokens);
                break;
            case "T":
                scheduleImagingAppointment(tokens);
                break;
            case "C":
                cancelAppointment(tokens);
                break;
            case "R":
                rescheduleAppointment(tokens);
                break;
            case "PO":
                displayOfficeAppointments();
                break;
            case "PI":
                displayImagingAppointments();
                break;
            case "PC":
                displayCreditAmounts();
                break;
            default:
                System.out.println("Invalid command.");
                break;
        }
    }

    // Placeholder methods for various actions
    private void scheduleOfficeAppointment(String[] tokens) {
        // Implementation for scheduling an office appointment
        Technician nextTechnician = technicians.CircleList().getNext();
    }

    private void scheduleImagingAppointment(String[] tokens) {
        // Implementation for scheduling an imaging appointment
    }

    private void cancelAppointment(String[] tokens) {
        // Implementation for canceling an appointment
    }

    private void rescheduleAppointment(String[] tokens) {
        // Implementation for rescheduling an appointment
    }

    private void displayOfficeAppointments() {
        // Implementation to display office appointments
    }

    private void displayImagingAppointments() {
        // Implementation to display imaging appointments
    }

    private void displayCreditAmounts() {
        // Implementation to display provider credit amounts
    }
}
