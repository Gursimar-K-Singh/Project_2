package model;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Calendar;
//util package required for Scanner
import util.List;
import util.Date;


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
            return new Doctor(profile, specialty, npi);  // Create and return Doctor object

        } else if (providerType.equals("technician")) {
            // If this is a Technician, additional tokens may include rate per visit
            int ratePerVisit = Integer.parseInt(tokens[5].trim());  // Example: "150"
            return new Technician(profile, ratePerVisit);  // Create and return Technician object

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

    private void initializeTechniciansList() {
        //to empty the existing list, if any
        techniciansList.clear();
        try {
            // Assuming that technicians are stored in a file named "providers.txt"
            File file = new File("providers.txt");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String inLine = scanner.nextLine().trim();

                // Parse each line to create a Technician object
                Provider provider = parseProvider(inLine);

                // Check if the parsed provider is a Technician and add it to the list
                if (provider instanceof Technician) {
                    techniciansList.add((Technician) provider);
                }
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("Error: Provider file not found.");
        }

        // Optionally, print the technicians list for debugging
        System.out.println("Technicians initialized: ");
        printTechniciansList();
    }

    private void printTechniciansList() {
        if (techniciansList.isEmpty()) {
            System.out.println("No technicians available.");
            return;}
        System.out.println("List of Technicians:");
        for (Technician technician : techniciansList) {
            System.out.println(technician);  // Assuming Technician class overrides toString() for proper output
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

    //Methods for handling appointments
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
        System.out.println("Imaging appointment scheduled successfully for " + firstName + " " + lastName + " for " + ImagingService + ".");
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
        if (appointments == null) {
            System.out.println("No appointments exist to cancel.");
            return;
        }

        if (appointments.contains(appointment)) {
            System.out.println(appointment + " has been canceled.");
            appointments.remove(appointment);
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

        if (appointments == null) {
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

    private Appointment findAppointmentforRescheduling(Date appDate, Timeslot timeslot, Profile patient) {
        if (appointments == null || appointments.isEmpty()) {
            return null;  // No appointments to search through
        }
        // Iterate over all appointments in the appointmentList
        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            // Compare the date, timeslot, and patient profile for a match
            if (appointment.getDate().equals(appDate) &&
                    appointment.getTimeslot().equals(timeslot) &&
                    appointment.getPatient().getProfile().equals(patient)) {
                return appointment;  // Return the matching appointment
            }
        }
        // If no appointment matches, return null
        return null;
    }

    private Appointment findCancellingAppointment(Date appDate, Timeslot timeslot, Profile patient) {
        if (appointments == null || appointments.isEmpty()) {
            System.out.println("No appointments to search for cancellation.");
            return null;
        }
        // Iterate through the appointmentList to find a matching appointment
        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            // Checks if the appointment's date, timeslot, and patient profile match
            if (appointment.getDate().equals(appDate) &&
                    appointment.getTimeslot().equals(timeslot) &&
                    appointment.getPatient().getProfile().equals(patient)) {
                return appointment;  // Return the appointment if found
            }
        }
        // If no match is found, return null
        return null;
    }

    private Technician findTechnicianAvailable(Timeslot timeslot, Radiology room) {
        if (techniciansList == null || techniciansList.isEmpty()) {
            System.out.println("No technicians available.");
            return null;
        }
        // Iterate over the circular list of technicians
        for (int i = 0; i < techniciansList.size(); i++) {
            Technician technician = techniciansList.get(i);
            // Check if the technician is available at the given timeslot and room
            if (technician.isAvailable(timeslot) && technician.hasRoom(room)) {
                return technician;  // Return the first available technician
            }
        }
        // If no available technician is found, return null
        return null;
    }

    private boolean TechnicianAvailability(Technician technician, Timeslot timeslot, Radiology room) {
        // Check if the technician is available during the specified timeslot
        if (!technician.isAvailable(timeslot)) {
            return false;  // Technician is not available at this timeslot
        }
        // Check if the technician is assigned to the given radiology room
        if (!technician.hasRoom(room)) {
            return false;  // Technician is not assigned to the specified room
        }
        // If both conditions are met, the technician is available
        return true;
    }

    private boolean hasExistingAppointment(Patient patient, Date date, Timeslot timeslot) {
        if (appointments == null || appointments.isEmpty()) {
            return false;  // No appointments exist
        }

        // Iterate over the appointmentList to find a matching appointment
        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            // Check if the appointment's date, timeslot, and patient match
            if (appointment.getDate().equals(date) &&
                    appointment.getTimeslot().equals(timeslot) &&
                    appointment.getPatient().equals(patient)) {
                return true;  // An existing appointment was found
            }
        }
        return false;
    }

    private Radiology parseImagingService(String serviceStr) {
        if (serviceStr == null || serviceStr.trim().isEmpty()) {
            System.out.println("Invalid imaging service.");
            return null;
        }
        // Convert serviceStr to lowercase and trim it to avoid case or space issues
        serviceStr = serviceStr.trim().toLowerCase();
        // Match the string to the corresponding Radiology service
        switch(serviceStr){
            case "xray":
                return Radiology.XRAY;
            case "ultrasound":
                return Radiology.ULTRASOUND;
            case "catscan":
            case "ctscan": //"ctscan"
                return Radiology.CATSCAN;
            default:
                System.out.println("Unknown imaging service: " + serviceStr);
                return null;  // Return null if the service doesn't match any known type
        }
    }

    //Validation,print, parse methods
    private void displayApppointmentsScheduled() {
        if (appointments == null || appointments.isEmpty()) {
            System.out.println("No appointments have been scheduled.");
            return;
        }

        System.out.println("Scheduled Appointments:");

        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            System.out.println(appointment);  // Assuming the Appointment class has a proper toString() implementation
        }
    }

    //need to fix this!!!
    private static Timeslot parseTimeslot(String timeslotStr) {
        switch (timeslotStr.trim()) {
            case "1":
                return Timeslot.SLOT1;
            case "2":
                return Timeslot.SLOT2;
            case "3":
                return Timeslot.SLOT3;
            case "4":
                return Timeslot.SLOT4;
            case "5":
                return Timeslot.SLOT5;
            case "6":
                return Timeslot.SLOT6;
            default:
                return null;
        }
    }

    private boolean scheduleAppPossibility(Appointment newAppointment, Provider provider) {

    }

    private static Provider parseProvider(String providerStr) {
        try {
            return Provider.valueOf(providerStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static Date parseDate(String dateStr) {
        String[] dateTokens = dateStr.split("/");
        if (dateTokens.length != 3) {
            return null;  // Invalid date format
        }

        int month = Integer.parseInt(dateTokens[0]);
        int day = Integer.parseInt(dateTokens[1]);
        int year = Integer.parseInt(dateTokens[2]);
        return new Date(month, day, year);  // Return new Date object

    }

    private static boolean validateAppointmentDate(String dateStr) {
        Date appointmentDate = parseDate(dateStr);

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

    private static boolean checkDOB(String dobStr) {
        Date dob = parseDate(dobStr);

        // Check if the date is null, meaning it couldn't be parsed correctly
        if (dob == null || !dob.isValid()) {
            System.out.println("Patient dob: " + dobStr + " is not a valid calendar date.");
            return false; // Invalid date
        }

        // Check if the DOB is today
        if (dob.isToday()) {
            System.out.println(dobStr + " is today or a date after today.");
            return false; // Assuming DOB cannot be today, return false
        }

        // Check if the DOB is in the future
        Calendar today = Calendar.getInstance();
        Date todayDate = new Date(today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH), today.get(Calendar.YEAR));
        if (dob.compareTo(todayDate) > 0) {
            System.out.println(dobStr + " is today or a date after today.");
            return false;
        }

        return true; // DOB is valid
    }

    private static boolean isNumeric(String str) {
        return str != null && str.matches("\\d+");
    }

    private static boolean isValidTimeSlot(String timeslotStr) {
        // Check if the timeslot string is numeric
        if (!isNumeric(timeslotStr)) {
            System.out.println(timeslotStr + " is not a valid time slot.");
            return false; // Invalid format
        }

        int timeslot = Integer.parseInt(timeslotStr);
        // Check if the timeslot is within the valid range
        if (timeslot < 1 || timeslot > 6) {
            System.out.println(timeslot + " is not a valid time slot.");
            return false; // Out of range
        }
        return true; // Valid timeslot
    }








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

    //Main
    public static void main(String[] args){
        new ClinicManager().run();
    }

}

