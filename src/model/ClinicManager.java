package model;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Calendar;

public class ClinicManager {
    private List<Provider> providers; // List to hold all providers
    private List<Appointment> appointments; // List to hold all appointments
    private CircleList<Technician> techniciansList; // Circular list for technicians

    public ClinicManager() {
        providers = new List<>();
        appointments = new List<>();
        this.techniciansList = new CircleList<>();
        loadProviders();
        initializeTechniciansList();
        Sort.provider(providers);
        displayProviders();
        printTechniciansList();
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
                        techniciansList.add(new Technician(tokens[1], tokens[2], tokens[3], tokens[4], tokens[5], tokens[6]));
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
        //Technician nextTechnician = technicians.getNext();
        String dateStr = tokens[1].trim();
        String timeslotStr = tokens[2].trim();
        String firstName = tokens[3].trim();
        String lastName = tokens[4].trim();
        String dobStr = tokens[5].trim();
        String docNPInum = tokens[6].trim();


        if (!validateAppointmentDate(dateStr)) {
            // If the date is invalid, stop further processing
            return;
        }

        if (!isValidTimeSlot(timeslotStr)) {
            // If the timeslot is invalid, stop further processing
            return;
        }

        if (!checkDOB(dobStr)) {
            return; // Stop if the DOB is invalid
        }

        Doctor doc = findDoctor(docNPInum);
        if(doctor == null){
            System.out.println("Error: Doctor with NPI " + doctorNPI + " not found.");
            return;
        }

        /*if (!checkImagingType(imagingTypeStr)) {
            System.out.println(imagingTypeStr + " - Provider does not exist.");
            return; // Stop if the provider does not exist
        }*/

        Date appDate = parseDate(dateStr);
        Date dob = parseDate(dobStr);
        Profile patient = new Profile(firstName, lastName, dob);
        Timeslot timeslot = parseTimeslot(timeslotStr);
        Doctor NPInum = parseNPI(docNPInum);
        //write getProvider command

        //below code line MUST BE CHECKED!!!!!
        Appointment appointment = new Appointment(appDate, timeslot, patient, provider);
        if (appointments == null) {
        } else if (!appointments.isAvailable(appointment)) {
            System.out.println("The selected timeslot is not available for this provider.");
            return;} // Stop if the timeslot is not available
        if (appointments == null) {
            appointments = new List(); // Initialize appointmentList if it's null
        }

        if (appointments.contains(appointment)) {
            System.out.println("Appointment already exists.");
        } else {
            appointments.add(appointment);
            System.out.println(appointment + "booked");
        }
    }

    //helper method to find doctor by NPI#
    private Doctor findDoctor(String npi) {
        for (Provider provider : providers) {
            if (provider instanceof Doctor && ((Doctor) provider).getNpi().equals(npi)) {
                return (Doctor) provider;
            }
        }
        return null; // If no doctor is found with the given NPI
    }


    private void scheduleImagingAppointment(String[] tokens) {
        // Implementation for scheduling an imaging appointment
        if (tokens.length < 7) {
            System.out.println("Error: Missing data tokens.");
            return;
        }

        // Extract data from the tokens
        String dateStr = tokens[1];
        String timeslotStr = tokens[2];
        String firstName = tokens[3];
        String lastName = tokens[4];
        String dobStr = tokens[5];
        String imagingType = tokens[6].toLowerCase();//lower case handling
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
