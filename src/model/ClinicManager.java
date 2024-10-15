package model;


import util.Date;
import util.sort;

import java.util.Scanner;
import util.List; // Import the new List class
import util.CircleList;
import java.io.File;
import java.io.FileNotFoundException;

public class ClinicManager {

    private List<Provider> providerList;
    private List<Appointment> appointmentList;
    private CircleList<Technician> technicianList;

    // Constants for calculating a date within 6 months
    private static final int TOTAL_MONTHS_IN_YEAR = 12;
    private static final int HALF_YEAR_IN_MONTHS = 6;

    public ClinicManager() {
        providerList = new List<>();
        appointmentList = new List<>();
        this.technicianList = new CircleList<>();

        loadProviderList(); //load + sort providers + instantiate technician rotation
        //initializeTechnicianRotation(); //load technician rotation circular list (before sorting providers)
        sort.provider(providerList); //Sort providers
        displayProviders(); //print providers
        //printTechnicianRotation(); //print technician rotation circular list
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


    public static void main(String[] args) {
        // Create an instance of ClinicManager
        ClinicManager clinicManager = new ClinicManager();

        // The constructor already calls loadProviderList and displayProviders,
        // so no additional method calls are needed.
    }




}
