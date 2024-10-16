package model;
import util.Date;
import util.sort;
import java.util.Scanner;
import util.List; // Import the new List class
import util.CircleList;
import java.io.File;
import java.io.FileNotFoundException;

public class ClinicManager {

    private  List<Provider> providerList;
    private List<Appointment> appointmentList;
    private CircleList<Technician> technicianList;

    // Constants for calculating a date within 6 months
    private static final int TOTAL_MONTHS_IN_YEAR = 12;
    private static final int HALF_YEAR_IN_MONTHS = 6;
    //public static final Timeslot[] TIME_SLOTS = generateTimeslots();

    public ClinicManager() {
        providerList = new List<>();
        appointmentList = new List<>();
        this.technicianList = new CircleList<>();

        loadProviderList(); //load + sort providers + instantiate technician rotation
        initializeTechnicianRotation(); //load technician rotation circular list (before sorting providers)
        sort.provider(providerList); //Sort providers
        displayProviders(); //print providers
        printTechnicianRotation(); //print technician rotation circular list
    }

    /**
     * Reads provider details from the "providers.txt" file and adds each provider
     * to the providerList. Displays an error message if the file cannot be found.
     */
    private void loadProviderList() {
        try {
            File providerFile = new File("providers.txt");
            Scanner fileScanner = new Scanner(providerFile);

            while (fileScanner.hasNextLine()) {
                String providerData = fileScanner.nextLine();
                Provider provider = convertToProvider(providerData);
                providerList.add(provider);
            }

            fileScanner.close();
            System.out.println("Providers successfully loaded to the list.");

        } catch (FileNotFoundException e) {
            System.out.println("Error: 'providers.txt' file not found.");
        }
    }

    /**
     * Prints the list of providers after sorting by profile.
     */
    private void displayProviders() {
        sort.provider(providerList); // Assumes providerList has been renamed as suggested earlier
        for (Provider provider : providerList) {
            System.out.println(provider);
        }
    }

    private Doctor getDocbyNPI(String npi) {
        for (int i = 0; i < providerList.size(); i++) {
            Provider provider = providerList.get(i);
            if (provider instanceof Doctor && ((Doctor) provider).getNpi().equals(npi)) {
                return (Doctor) provider; // Return the found Doctor
            }
        }
        return null; // No doctor found with that NPI
    }



    /**
     * Converts a line of text into a Provider object.
     *
     * @param line A string containing provider details formatted as per specifications.
     * @return A Provider instance if conversion is successful, or null for invalid formats.
     */
    private Provider convertToProvider(String line) {
        String[] details = line.split("  "); // Maintain the two-space delimiter from providers.txt.

        // Identify the provider category (Doctor or Technician)
        String providerCategory = details[0].trim();

        // Validate the number of fields: Doctor should have 7, Technician should have 6
        if ((providerCategory.equals("D") && details.length != 7) ||
                (providerCategory.equals("T") && details.length != 6)) {
            System.out.println("Error: Invalid provider format.");
            return null;
        }

        // Extract common attributes shared by both Doctors and Technicians
        String firstName = details[1].trim();
        String lastName = details[2].trim();
        Date dateOfBirth = convertToDate(details[3].trim());
        Location workLocation = convertToLocation(details[4].trim());

        // Determine if the Provide is a Doctor or Technician
        if (providerCategory.equals("D")) {
            Specialty doctorSpecialty = Specialty.valueOf(details[5].trim()); // Make sure it matches the enum
            String npiNumber = details[6].trim();
            return new Doctor(new Profile(firstName, lastName, dateOfBirth), workLocation, doctorSpecialty, npiNumber);
        } else if (providerCategory.equals("T")) {
            int visitRate = Integer.parseInt(details[5].trim());
            return new Technician(new Profile(firstName, lastName, dateOfBirth), workLocation, visitRate);
        }

        // Return null if the provider type is neither Doctor nor Technician
        return null;
    }

    /**
     * Converts a string to a Location enum value.
     *
     * @param locationString The string representation of the location.
     * @return The matching Location enum, or throws an exception if invalid.
     */
    public Location parseLocation(String locationString) {
        for (Location loc : Location.values()) {
            if (loc.name().equalsIgnoreCase(locationString)) {
                return loc;
            }
        }
        throw new IllegalArgumentException("Invalid Location '" + locationString + "'.");
    }

    /**
     * Converts a string to a Location enum value.
     *
     * @param locationString The string representation of the location.
     * @return The matching Location enum, or throws an exception if invalid.
     */
    public Location convertToLocation(String locationString) {
        for (Location loc : Location.values()) {
            if (loc.name().equalsIgnoreCase(locationString)) {
                return loc;
            }
        }
        throw new IllegalArgumentException("Error: Unknown location '" + locationString + "'.");
    }

    /**
     * Converts a string representation of a date into a Date object.
     *
     * @param dateStr A string in the format "MM/DD/YYYY".
     * @return A Date object corresponding to the input string, or null if the format is invalid.
     */
    private static Date convertToDate(String dateStr) {
        String[] dateTokens = dateStr.split("/");
        if (dateTokens.length != 3) {
            return null;  // Invalid date format
        }

        int month = Integer.parseInt(dateTokens[0]);
        int day = Integer.parseInt(dateTokens[1]);
        int year = Integer.parseInt(dateTokens[2]);
        return new Date(month, day, year);  // Return new Date object
    }

    /**
     * Sets up the technician rotation list by including technicians from the providers list
     * in reverse order, beginning with the last technician added.
     */
    private void initializeTechnicianRotation() {
        // Traverse the providers list from the end to the beginning
        for (int index = providerList.size() - 1; index >= 0; index--) {
            Provider currentProvider = providerList.get(index);
            // Check if the current provider is a technician
            if (currentProvider instanceof Technician) {
                technicianList.add((Technician) currentProvider);  // Add technicians in reverse order
            }
        }
    }

    /**
     * Displays the current technician rotation list, showing each technician's first and last names along with their locations.
     */
    private void printTechnicianRotation() {
        System.out.println("\nCurrent technician rotation list:");
        for (int index = 0; index < technicianList.size(); index++) {
            Technician currentTechnician = technicianList.get(index);
            System.out.print(currentTechnician.getProfile().getFname() + " " + currentTechnician.getProfile().getLname() +
                    " (" + currentTechnician.getLocation().name() + ")");
            // Add arrow between technicians except for the last one
            if (index < technicianList.size() - 1) {
                System.out.print(" --> ");
            }
        }
        System.out.println(); // Move to the next line after printing the rotation
    }


    public static void main(String[] args) {
        // Create an instance of ClinicManager
        ClinicManager clinicManager = new ClinicManager();

        // The constructor already calls loadProviderList and displayProviders,
        // so no additional method calls are needed.
    }

    // Method to run the Clinic Manager
    /**
     * Runs the main loop of the Clinic Manager, processing user commands for scheduling,
     * canceling, and managing appointments.
     */
    public void run() {
        System.out.println("Clinic Manager is running...\n");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (!scanner.hasNextLine()) {
                break; // Break the loop if there is no more input
            }

            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue; // ignores lines that are empty

            String[] tokens = input.split(",");
            String command = tokens[0].trim();

            //Execute the command
            switch (command) {
                case "D"://scheduleOfficeAppointment(tokens); break;
                case "T": //scheduleImagingAppointment(tokens); break;
                case "C": //cancelAppointment(tokens); break;
                case "R": //rescheduleAppointment(tokens); break;
                case "PO": //printOfficeAppointments() ; break;
                case "PI": //printImagingAppointments(); break;
                case "PC": //printProviderCredits();; break;
                case "PA": //printAppointmentsByDateTimeProvider(); break;
                case "PP": //printAppointmentsByPatient(); break;
                case "PL": //printAppointmentsByCountyDateTime(); break;
                case "PS": //printBillingStatement();;;break;
                case "Q":
                {
                    System.out.println("Clinic Manager is terminated.");
                    scanner.close();
                    return;
                }
                default: System.out.println("Invalid command!");
            }
        }

        scanner.close();
    }

    private static boolean validateAppointmentDate(String dateStr) {
        Date appointmentDate = convertToDate(dateStr);

        if (!appointmentDate.isValid()) {
            System.out.println("Appointment date: " + dateStr + " is not a valid calendar date.");
            return false;
        }

        if (appointmentDate.isToday() || appointmentDate.isInThePast()) {
            System.out.println("Appointment date: " + dateStr + " is today or a date before today.");
            return false;
        }

        if (appointmentDate.isWeekend()) {
            System.out.println("Appointment date: " + dateStr + " is Saturday or Sunday.");
            return false;
        }

        if (!(appointmentDate.isWithinSixMonths())) {
            System.out.println("Appointment date: " + dateStr + " is not six months within.");
            return false;
        }

        return true;
    }

    private static boolean isNumeric(String str) {
        return str != null && str.matches("\\d+");
    }

    /**
     * Parses a timeslot from a string representation.
     *
     * @param timeslotStr The string representation of the timeslot (1 to 12).
     * @return The corresponding Timeslot object, or null if the input is invalid.
     */
    private Timeslot calcTimeslot(String timeslotStr) {
        try {
            int slotNumber = Integer.parseInt(timeslotStr.trim()); // Parse the string as an integer
            if (slotNumber >= 1 && slotNumber <= Timeslot.TIME_SLOTS.length) {
                return Timeslot.TIME_SLOTS[slotNumber - 1]; // Convert slot number to zero-based index
            } else {
                return null; // Invalid slot number
            }
        } catch (NumberFormatException e) {
            return null; // If the input is not a valid number
        }
    }




}
