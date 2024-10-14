package model;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Calendar;
import util.List;

public class ClinicManager {
    private List<Provider> providers; // List to hold all providers
    private List<Appointment> appointments; // List to hold all appointments
    private CircleList<Technician> techniciansList; // Circular list for technicians

    public ClinicManager() {
        providers = new List<>();
        appointments = new List<>();
        this.techniciansList = new CircleList<>();
        loadProviders();
        //initializeTechniciansList();
        //Sort.provider(providers);
        displayProviders();
        //printTechniciansList();
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
                    switch (tokens[0]) {
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

    //double check method!!!
    private Provider parseProvider(String inLine){
        String[] tokens = inLine.split(",");
        if(tokens.length < 5){
            System.out.println("Invalid provider data: " + inLine);
            return null;
        }
        String firstName = tokens[0].trim();
        String lastName = tokens[1].trim();
        String dobStr = tokens[2].trim();
        String npi = tokens[3].trim();
        String providerType = tokens[4].trim().toLowerCase();

        Date dob = parseDate(dobStr);

        if (dob == null) {
            System.out.println("Invalid date for provider: " + inLine);
            return null;
        }
        Profile profile = new Profile(firstName, lastName, dob);
        if (providerType.equals("doctor")) {
            // If this is a Doctor, additional tokens may include specialty information
            String specialtyStr = tokens[5].trim();  // Example: "Cardiology"
            Specialty specialty = Specialty.valueOf(specialtyStr.toUpperCase());  // Assuming enum for specialties
            //return new Doctor(profile, specialty, npi);  // Create and return Doctor object

        } else if (providerType.equals("technician")) {
            // If this is a Technician, additional tokens may include rate per visit
            int ratePerVisit = Integer.parseInt(tokens[5].trim());  // Example: "150"
            //return new Technician(profile, ratePerVisit);  // Create and return Technician object

        } else {
            System.out.println("Unknown provider type: " + providerType);
            return null;
        }

    }


    //Displays sorted providers
    private void displayProviders() {
        System.out.println("List of providers:");
        for (Provider provider : providers) {
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
        if (tokens.length < 7) {
            System.out.println("Error: Missing data tokens.");
            return;
        }
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
        if (doctor == null) {
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
        String NPInum = parseNPI(docNPInum);

        Patient patientSchedule = new Patient(patient);

        //below code line MUST BE CHECKED!!!!! - esp 'provider' parameter
        Appointment appointment = new Appointment(appDate, timeslot, patientSchedule, findDoctor(NPInum));
        if (appointments == null) {
        } else if (!appointments.isAvailable(appointment)) {
            System.out.println("The selected timeslot is not available for this provider.");
            return;
        } // Stop if the timeslot is not available
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
        for (int i = 0; i < providers.size(); i++) {
            Provider provider = providers.get(i);
            if (provider instanceof Doctor && ((Doctor) provider).getNpi().equals(npi)) {
                return (Doctor) provider; // Return the found Doctor
            }
        }
        return null; // No doctor found with that NPI
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

        if (!isValidDate(dateStr)) {
            System.out.println("Error: Invalid date: " + dateStr);
            return;
        }

        // Validate timeslot (must be between 1 and 12)
        if (!isValidTimeslot(timeslotStr)) {
            System.out.println("Error: Invalid timeslot: " + timeslotStr);
            return;
        }
        int timeslot = Integer.parseInt(timeslotStr); // Convert timeslot to integer

        // Validate date of birth
        if (!isValidDate(dobStr)) {
            System.out.println("Error: Invalid date of birth: " + dobStr);
            return;
        }

        // Validate imaging service
        Radiology roomType = getRadiologyType(imagingType);
        if (roomType == null) {
            System.out.println("Error: Invalid imaging service: " + imagingServiceStr);
            return;
        }

        // Get the next available technician for the imaging service
        Technician nextTechnician = techniciansList.getNext();
        if (nextTechnician == null) {
            System.out.println("Error: No available technicians.");
            return;
        }

        // Check if the imaging room is available at the requested timeslot
        if (!isRoomAvailable(roomType, timeslot, dateStr)) {
            System.out.println("Error: Imaging room is not available at the requested timeslot.");
            return;
        }

        Date appDate = parseDate(dateStr);
        Date dob = parseDate(dobStr);
        Profile patient = new Profile(firstName, lastName, dob);
        Timeslot timeslotImg = parseTimeslot(timeslotStr);
        Imaging ImagingService = parseImagingType(imagingType);

        // Create a new Patient object
        Patient patientImg = new Patient(patient);

        // Schedule the imaging appointment
        Imaging imagingAppointment = new Imaging(appDate, timeslotImg, patientImg, nextTechnician, roomType);
        appointments.add(imagingAppointment);
        System.out.println("Imaging appointment scheduled successfully for " + firstName + " " + lastName + " for " + imagingServiceStr + ".");
    }


    private void cancelAppointment(String[] tokens) {
        // Implementation for canceling an appointment
        if (tokens.length != 7) {
            System.out.println("Invalid command.");
            return;
        }

        // Extract values from input tokens
        String dateStr = tokens[1].trim();
        String timeslotStr = tokens[2].trim();
        String firstName = tokens[3].trim();
        String lastName = tokens[4].trim();
        String dobStr = tokens[5].trim();
        String providerLastName = tokens[6].toUpperCase().trim();

        // Convert strings to necessary objects (date, timeslot, profile)
        Date appDate = parseDate(dateStr);
        Date dob = parseDate(dobStr);
        Profile patient = new Profile(firstName,lastName,dob);
        Timeslot timeslot = parseTimeslot(timeslotStr);;
        Provider provider = parseProvider(providerLastName);

        Patient patientCancel = new Patient(patient);

        Appointment appointment = new Appointment(appDate,timeslot,patientCancel,provider);

        // Create the appointment object to search for

        // Check if the appointmentList exists and has appointments
        if (appointmentList == null) {
            System.out.println("No appointments exist to cancel.");
            return;
        }

        if (appointmentList.contains(appointment)) {
            System.out.println(appointment + " has been canceled.");
            appointmentList.remove(appointment);
        } else {
            System.out.println(appointment + " does not exist.");
        }
    }

    private void rescheduleAppointment(String[] tokens) {
        // Implementation for rescheduling an appointment
        if (tokens.length != 7) {
            System.out.println("Invalid command.");
            return;
        }

        String dateStr = tokens[1].trim();
        String oldTimeslotStr = tokens[2].trim();
        String firstName = tokens[3].trim();
        String lastName = tokens[4].trim();
        String dobStr = tokens[5].trim();
        String newTimeslotStr = tokens[6].trim();

        // Convert strings to necessary objects (date, timeslot, profile)

        if (!isValidTimeSlot(newTimeslotStr)) {
            return;
        }

        if (appointmentList == null) {
            System.out.println("No appointments exist to reschedule.");
            return;
        }

        Date appDate = parseDate(dateStr);
        Timeslot oldTimeslot = Timeslot.valueOf("SLOT" + oldTimeslotStr);
        Timeslot newTimeslot = Timeslot.valueOf("SLOT" + newTimeslotStr);
        Date dob = parseDate(dobStr);
        Profile patient = new Profile(firstName, lastName, dob);
        Appointment oldAppointment = new Appointment(appDate, oldTimeslot, patient, null);

        if (!appointments.contains(oldAppointment)) {
            System.out.println(appDate.toString() + " " + oldTimeslot.toString() + " " + firstName + " " + lastName + " " + dob.toString() + " does not exist.");
            return;
        }

        Appointment existingAppointment = findExistingAppointment(oldAppointment);
        if (existingAppointment == null) {
            System.out.println(appDate.toString() + " " + oldTimeslot.toString() + " " + firstName + " " + lastName + " " + dob.toString() + " does not exist.");
            return;
        }
        Provider provider = existingAppointment.getProvider(); // Assuming getProvider() exists
        Appointment newAppointment = new Appointment(appDate, newTimeslot, patient, existingAppointment.getProvider());

        if (!appointments.isAvailable(newAppointment)) {
            System.out.println(provider + " is not available at " + newTimeslot +".");
            return;
        }

        handleAppointmentAddition(newAppointment);
        appointments.remove(oldAppointment);
        appointments.add(newAppointment);
        System.out.println("Rescheduled to "+ newAppointment.toString() );
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

