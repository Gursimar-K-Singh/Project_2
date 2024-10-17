package model;
import util.Date;
import util.sort;

import java.util.Calendar;
import java.util.Scanner;
import util.List;
import util.CircleList;

/**
 * Manages clinic operations and appointments.
 *
 * Commands:
 * - D: Schedule with a doctor
 * - T: Schedule with a technician
 * - R: Reschedule an appointment
 * - C: Cancel an appointment
 * - PO: Print office appointments
 * - PI: Print imaging appointments
 * - PA: Sort appointments by date
 * - PP: Sort appointments by patient
 * - PL: Sort appointments by county
 * - PS: Generate billing statement
 * - PC: Display provider credits
 * - Q: Terminate the clinic manager
 *
 * @author Gursimar Singh
 */
public class ClinicManager {

    private List<Provider> providerList;
    private List<Appointment> appointmentList;
    private CircleList<Technician> technicianList;

    /**
     * The constructor makes a provider and app
     * Loads and displays the provider and technician lists.
     */
    public ClinicManager() {
        this.technicianList = new CircleList<>();
        providerList = new List<>();
        appointmentList = new List<>();

        loadProviderList(); //fill the provider list
        createTechnicianList(); // fill the technician list
        displayProviderList(); //print providers
        displayTechnicianList(); //print technician list
    }

    /**
     * Loads provider data from a file and adds them to the provider list.
     * The provider information is read from "providers.txt".
     * Handles file-not-found errors by displaying an appropriate message.
     */
    private void loadProviderList() {
        try {
            // Load file
            Scanner fileScanner = new Scanner(getClass().getResourceAsStream("providers.txt"));

            while (fileScanner.hasNextLine()) {
                String providerData = fileScanner.nextLine();

                Provider provider = convertToProvider(providerData);
                if (provider != null) {
                    providerList.add(provider);
                }
            }

            fileScanner.close();
            System.out.println("Providers successfully loaded to the list.");

        } catch (NullPointerException e) {
            System.out.println("Error: 'providers.txt' file not found in the package.");
        }
    }


    /**
     * Displays the list of providers sorted in ascending order.
     * Each provider's details are printed to the console.
     */
    private void displayProviderList() {
        sort.provider(providerList); // Assumes providerList has been renamed as suggested earlier
        for(int i = 0; i < providerList.size(); i++){
            System.out.println(providerList.get(i).toString());
        }
    }

    /**
     * Creates a list of technicians from the provider list,
     * adding technicians in reverse order.
     */
    private void createTechnicianList() {
        // Traverse providers list
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
    private void displayTechnicianList() {
        System.out.println("\nRotation list for the technicians.\n:");
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

    /**
     * Converts a line of text into a Provider object.
     *
     * @param line A string containing provider details formatted as per specifications.
     * @return A Provider instance if conversion is successful, or null for invalid formats.
     */
    private Provider convertToProvider(String line) {
        String[] details = line.split("  "); // Maintain the two-space delimiter from providers.txt.

        // check if Doctor or Tech
        String providerCategory = details[0].trim();

        // check length of details. If D, the details.length = y and if T then details.length = 6
        if ((providerCategory.equals("D") && details.length != 7) ||
                (providerCategory.equals("T") && details.length != 6)) {
            System.out.println("Error: Invalid provider format.");
            return null;
        }

        // get first name, last name, dob, and location
        String firstName = details[1].trim();
        String lastName = details[2].trim();
        Date dateOfBirth = convertToDate(details[3].trim());
        Location workLocation = convertToLocation(details[4].trim());

        // check if doctor or tech
        if (providerCategory.equals("D")) {
            Specialty doctorSpecialty = Specialty.valueOf(details[5].trim()); // Make sure it matches the enum
            String npiNumber = details[6].trim();
            return new Doctor(new Profile(firstName, lastName, dateOfBirth), workLocation, doctorSpecialty, npiNumber);
        } else if (providerCategory.equals("T")) {
            int visitRate = Integer.parseInt(details[5].trim());
            return new Technician(new Profile(firstName, lastName, dateOfBirth), workLocation, visitRate);
        }

        // return null if neither
        return null;
    }

    /**
     * Runs the Clinic Manager application, processes user input, and executes commands.
     */
    public void run() {
        System.out.println("Clinic Manager is running...\n");
        Scanner sc = new Scanner(System.in);

        boolean running = true;

        while (running) {
            // Check if there is another line to read
            if (!sc.hasNextLine()) {
                break; // Break the loop if no input is available
            }

            String userInput = sc.nextLine().trim();
            if (userInput.isEmpty()) continue; // Ignore empty lines

            String[] tokens = userInput.split(",");
            String command = tokens[0].trim();

            // Execute the command using a separate method
            running = executeCommand(command, tokens); // Update the running status based on command execution
        }


        sc.close();
    }

    /**
     * Executes  specified command with its associated arguments for managing clinic appointments.
     * @return False if program is terminated
     */
    private boolean executeCommand(String command, String[] tokens) {
        switch (command) {
            case "D":
                scheduleWithADoctor(tokens);
                break;
            case "T":
                scheduleWithATech(tokens);
                break;
            case "R":
                reschedule(tokens);
                break;
            case "C":
                cancel(tokens);
                break;
            case "PO":
                printOnlyOfficeAppointments();
                break;
            case "PI":
                printOnlyImagingAppointments();
                break;
            case "PA":
                DateSort();
                break;
            case "PP":
                patientSort();
                break;
            case "PL":
                countySort();
                break;
            case "PS":
                billingStatement();
                break;
            case "PC":
                providerCredits();
                break;
            case "Q":
                System.out.println("Clinic Manager terminated.\n");
                return false; // Stop the loop
            default:
                System.out.println("Invalid command!");
        }
        return true; // Continue running
    }


    /**
     * converts a string to a date object
     *
     * @param dateStr A string that has a format of  "MM/DD/YYYY".
     * @return A Date object or null if the format is invalid.
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
     * checks if appointment is valid based on these criteria
     * - A valid calendar date.
     * - Not today or in the past.
     * - Not on a weekend (Saturday or Sunday).
     * - Within six months from the current date.
     *
     * @param dateStr the appointment date as a string.
     * @return true if the date is valid and false if not.
     */
    private static boolean validateAppointmentDate(String dateStr) {
        Date appointmentDate = convertToDate(dateStr);

        if (!appointmentDate.isValid()) {
            System.out.println("Appointment date: " + dateStr + " is not a valid calendar date");
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
            System.out.println("Appointment date: " + dateStr + " is not within six months.");
            return false;
        }

        return true;
    }

    /**
     * makes a Timeslot object from a String
     *
     * @param timeslotStr The string representation of the timeslot (1 to 12).
     * @return  Timeslot object or null if the input is invalid.
     */
    private Timeslot convertToTimeslot(String timeslotStr) {
        int slotIndex;
        try {
            slotIndex = Integer.parseInt(timeslotStr.trim()); // Parse the string as an integer
            if (slotIndex >= 1 && slotIndex <= Timeslot.SLOTS.length) {
                return Timeslot.SLOTS[slotIndex - 1]; // Convert slot number to zero-based index
            } else {
                return null; // Invalid slot number
            }
        } catch (NumberFormatException e) {
            return null; // If the input is not a valid number
        }
    }

    /**
     * makes a string into a Location enum value.
     *
     * @param locationString The string representation of the location.
     * @return The matching Location enum, or throws an exception if invalid.
     */
    private Location convertToLocation(String locationString) {
        for (Location loc : Location.values()) {
            if (loc.name().equalsIgnoreCase(locationString)) {
                return loc;
            }
        }
        throw new IllegalArgumentException("Error: Unknown location '" + locationString + "'.");
    }


    /**
     * Reschedules an existing office appointment based on provided command tokens.
     *
     * @param tokens An array of strings representing the command and its parameters.
     */
    private void reschedule(String[] tokens) {
        // checking length of tokens
        if (!isValidRescheduleInput(tokens)) {
            return;
        }

        //convert from string to proper date types
        Date appointmentDate = convertToDate(tokens[1]);
        Timeslot oldTimeslot = convertToTimeslot(tokens[2]);
        Profile patientProfile = new Profile(tokens[3].trim(), tokens[4].trim(), convertToDate(tokens[5].trim()));
        Timeslot newTimeslot = convertToTimeslot(tokens[6]);

        // rescheduling appointment
        attemptReschedule(appointmentDate, oldTimeslot, patientProfile, newTimeslot);
    }
    /**
     * Checks if the provided token array has exactly 7 elements.
     * Prints an error message if the count is invalid.
     *
     * @param tokens the command tokens
     * @return true if valid; false otherwise
     */
    private boolean isValidRescheduleInput(String[] tokens) {
        //checks token length
        if (tokens.length != 7) {
            System.out.println("Missing data tokens");
            return false;
        }
        return true;
    }

    /**
     * Attempts to reschedule an existing appointment based on the provided details.
     * Validates the appointment's existence, checks for conflicts, and ensures provider availability
     * before replacing the old appointment with a new one.
     *
     * @param appointmentDate the date of the appointment to reschedule
     * @param oldTimeslot the current timeslot of the appointment
     * @param patientProfile the profile of the patient associated with the appointment
     * @param newTimeslot the desired new timeslot for the appointment
     */
    private void attemptReschedule(Date appointmentDate, Timeslot oldTimeslot, Profile patientProfile, Timeslot newTimeslot) {
        // Find the appointment to reschedule
        Appointment rescheduleAppointment = FindAppointment(appointmentDate, oldTimeslot, patientProfile);

        if (rescheduleAppointment == null) {
            System.out.println(appointmentDate + " " + oldTimeslot + " " + patientProfile.getFname() + " " + patientProfile.getLname() + " " + patientProfile.getDob() + " does not exist.");
            return;
        }

        // Check if appointment is an imaging appointment
        if (rescheduleAppointment instanceof Imaging) {
            System.out.println("Imaging appointments cannot be rescheduled.");
            return;
        }

        Patient patient = (Patient) rescheduleAppointment.getPatient();

        // Checking for conflicts
        if (checkIfAppointmentExists(patient, appointmentDate, newTimeslot)) {
            System.out.println(patientProfile.toString() + " has an existing appointment at " + appointmentDate + " " + newTimeslot.toString());
            return;
        }

        Provider provider = (Provider) rescheduleAppointment.getProvider();
        Appointment newAppointment = new Appointment(appointmentDate, newTimeslot, patient, provider);

        // checking provider avaliability
        if (!isDocAvailable(provider, newAppointment)) {
            System.out.println(provider.toString() + " is not available at slot " + newTimeslot.getSlot() + ".");
            return;
        }

        // replace old with new
        appointmentList.remove(rescheduleAppointment);
        appointmentList.add(newAppointment);
        System.out.println("Rescheduled to " + newAppointment.toString());
    }

    /**
     * Checks doctor's availability for the given appointment.
     *
     * @param provider The doctor to check.
     * @param newAppointment The new appointment to check against existing ones.
     * @return true if the doctor is available and false if they are already booked
     */
    private boolean isDocAvailable(Provider provider, Appointment newAppointment) {
        // Ensure the provider is a Doctor
        if (!(provider instanceof Doctor)) {
            return false; // Not a doctor
        }

        Doctor doctor = (Doctor) provider; // Cast provider to Doctor

        // Iterate through existing appointments to check for conflicts
        for (Appointment existingAppointment : appointmentList) {
            if (isSameDoctor(doctor, existingAppointment) && isSameDateAndTimeslot(newAppointment, existingAppointment)) {
                return false; // Doctor is busy
            }
        }
        return true; // Doctor is available
    }


    /**
     * Checks if doctors are the same for a given appointment
     *
     * @param doctor The doctor to compare against.
     * @param appointment The appointment to check.
     * @return true if both refer to the same doctor, false otherwise.
     */
    private boolean isSameDoctor(Doctor doctor, Appointment appointment) {
        return appointment.getProvider() instanceof Doctor &&
                ((Doctor) appointment.getProvider()).getNpi().equals(doctor.getNpi());
    }

    /**
     * checks if the date and timeslot are the same for two appointments
     *
     * @param newAppointment The new appointment to check.
     * @param existingAppointment The existing appointment to compare with.
     * @return true if both appointments have the same date and timeslot, false otherwise.
     */
    private boolean isSameDateAndTimeslot(Appointment newAppointment, Appointment existingAppointment) {
        return newAppointment.getDate().equals(existingAppointment.getDate()) &&
                newAppointment.getTimeslot().equals(existingAppointment.getTimeslot());
    }

    /**
     * finds an appointment based on Date, time and patient profile
     *
     * @param appointmentDate The date of the appointment.
     * @param timeslot The old timeslot of the appointment.
     * @param patientProfile The patient's profile.
     * @return The matching appointment if found; null otherwise.
     */
    private Appointment FindAppointment(Date appointmentDate, Timeslot timeslot, Profile patientProfile) {
        for (Appointment appointment : appointmentList) {
            if (isMatchingAppointment(appointment, appointmentDate, timeslot, patientProfile)) {
                return appointment; // found appointment
            }
        }
        return null; // no matching appointment
    }

    /**
     * checks if the appointments match
     * and patient profile.
     *
     * @param appointment The appointment to check.
     * @param appointmentDate The date to match.
     * @param timeslot The timeslot to match.
     * @param patientProfile The patient's profile to match.
     * @return true if the appointment matches; false otherwise.
     */
    private boolean isMatchingAppointment(Appointment appointment, Date appointmentDate, Timeslot timeslot, Profile patientProfile) {
        Profile appointmentProfile = appointment.getPatient().getProfile();
        return appointmentProfile.getFname().equalsIgnoreCase(patientProfile.getFname()) &&
                appointmentProfile.getLname().equalsIgnoreCase(patientProfile.getLname()) &&
                appointment.getDate().equals(appointmentDate) &&
                appointment.getTimeslot().equals(timeslot);
    }

    /**
     * checks if the patient has an existing appointment already
     *
     * @param patient   The patient whose appointments are being checked.
     * @param date      The date of the appointment to verify.
     * @param timeslot  The timeslot of the appointment to verify.
     * @return true if the patient has a scheduled appointment at the specified date and timeslot; false otherwise.
     */
    private boolean checkIfAppointmentExists(Patient patient, Date date, Timeslot timeslot) {
        for (Appointment appointment : appointmentList) {
            if (isMatchingAppointment(appointment, date, timeslot, patient.getProfile())) {
                return true; //appointment exists
            }
        }
        return false; // appointment does not exists
    }

    /**
     * converts string to an imaging room.
     *
     * @param imagingService the string representation of the imaging service
     * @return the corresponding Radiology enum value, or null if the service is invalid
     */
    private Radiology convertToImagingService(String imagingService) {
        try {
            return Radiology.valueOf(imagingService.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // Invalid service
        }
    }

    /**
     * Cancels an appointment (office or imaging).
     *
     * @param tokens Array of strings containing appointment details
     */
    private void cancel(String[] tokens) {
        if (tokens.length != 6) {
            System.out.println("Missing data tokens.");
            return;
        }

        Date schuduledDate = convertToDate(tokens[1]);
        Timeslot timeslot = convertToTimeslot(tokens[2]);
        Profile patientProfile = new Profile(tokens[3].trim(), tokens[4].trim(), convertToDate(tokens[5].trim()));
        Appointment cancelAppointment = FindAppointment(schuduledDate, timeslot, patientProfile);

        // Attempt to find and cancel the appointment
        if (cancelAppointment != null) { // If found
            appointmentList.remove(cancelAppointment); // Remove the appointment
            System.out.println(schuduledDate + " " + timeslot + " " +patientProfile.toString() + " - appointment has been canceled.");
        } else {
            // If not found, notify the user
            System.out.println(schuduledDate + " " + timeslot + " " +patientProfile.toString() + " - appointment does not exist.");
        }
    }

    /**
     * Schedules an imaging appointment
     *
     * @param tokens The command tokens containing appointment details.
     */
    private void scheduleWithATech(String[] tokens) {
        // Validate command tokens for T (Imaging appointment)
        if (!isValidCommand(tokens)) {
            return;
        }

        Date scheduledDate = getScheduledDate(tokens[1]);
        if (scheduledDate == null) return;

        Timeslot timeslot = getTimeslot(tokens[2]);
        if (timeslot == null) return;

        if (!checkDOB(tokens[5])) {
            return;
        }

        Patient patient = createPatient(tokens);
        if (checkIfAppointmentExists(patient, scheduledDate, timeslot)) {
            System.out.println(patient.getProfile() + " has an existing appointment at the same time slot.");
            return;
        }

        Radiology room = getImagingService(tokens[6]);
        if (room == null) return;

        Technician tech = findAvailableTechnician(timeslot, room);
        if (tech == null) return;

        bookImagingAppointment(scheduledDate, timeslot, patient, tech, room);
    }

    /**
     * check token length and if using inputs "T" for technican.
     *
     * @param tokens The command tokens.
     * @return True if the command is valid and false otherwise.
     */
    private boolean isValidCommand(String[] tokens) {
        if (tokens.length != 7 || !tokens[0].equalsIgnoreCase("T")) {
            System.out.println("Missing data tokens");
            return false;
        }
        return true;
    }

    /**
     * gets a date from a string
     *
     * @param dateString The date string t
     * @return  Date object or null if invalid.
     */
    private Date getScheduledDate(String dateString) {
        if (!validateAppointmentDate(dateString)) {
            return null;
        }
        return convertToDate(dateString);
    }

    /**
     * find timeslot from a string
     *
     * @param timeslotString The timeslot string
     * @return Timeslot object or null if invalid.
     */
    private Timeslot getTimeslot(String timeslotString) {
        Timeslot timeslot = convertToTimeslot(timeslotString);
        if (timeslot == null) {
            System.out.println(timeslotString + " is not a valid time slot.");
        }
        return timeslot;
    }

    /**
     * creates a patient object based on user input
     *
     * @param tokens The command tokens containing patient information.
     * @return A Patient object
     */
    private Patient createPatient(String[] tokens) {
        Profile patientProfile = new Profile(tokens[3].trim(), tokens[4].trim(), convertToDate(tokens[5]));
        return new Patient(patientProfile);
    }

    /**
     * finds imaging services from a string
     *
     * @param serviceString The service string
     * @return Radiology object or null if invalid.
     */
    private Radiology getImagingService(String serviceString) {
        Radiology room = convertToImagingService(serviceString);
        if (room == null) {
            System.out.println(serviceString + " - imaging service not provided.");
        }
        return room;
    }
    /**
     * see if technician's availability for a given timeslot
     *
     * @param timeslot  requested timeslot.
     * @param room  imaging service being requested.
     * @return The available Technician or null if technician not found.
     */
    private Technician findAvailableTechnician(Timeslot timeslot, Radiology room) {
        Technician tech = findATech(timeslot, room);
        if (tech == null) {
            System.out.println("Cannot find an available technician at all locations for " + room.name() + " at slot " + timeslot.getSlot() + ".");
        }
        return tech;
    }

    /**
     * Books the imaging appointment and adds it to the appointment list.
     *
     * @param scheduledDate The date of the appointment.
     * @param timeslot     The timeslot for the appointment.
     * @param patient      The patient for the appointment.
     * @param tech         The technician assigned to the appointment.
     * @param room         The imaging service for the appointment.
     */
    private void bookImagingAppointment(Date scheduledDate, Timeslot timeslot, Patient patient, Technician tech, Radiology room) {
        Imaging imaging = new Imaging(scheduledDate, timeslot, patient, tech, room);
        appointmentList.add(imaging);
        // Output booking information
        System.out.println(scheduledDate + " " + timeslot + " " + patient + " " + tech + "[" + room + "] booked.");
    }

    /**
     * Finds a technician for a given timeslot and imaging service.
     *
     * @param timeslot the timeslot user requests
     * @param room     the imaging service room user requests
     * @return  a Technician or null if none is found
     */
    private Technician findATech(Timeslot timeslot, Radiology room) {
        for (int i = 0; i < technicianList.size(); i++) {
            Technician tech = technicianList.getNext(); // Get the next technician in the rotation

            if (isTechnAvailable(tech, timeslot, room)) {
                return tech; // Technician is available, return immediately
            }
        }

        // If no technician is found return null
        return null;
    }

    /**
     *see if a technician is open at a given timeslot and toom
     * @param technician  technician to check for availability
     * @param timeslot   the timeslot user requested
     * @param room       the imaging service user requested
     * @return true if the technician is free and false otherwise
     */
    private boolean isTechnAvailable(Technician technician, Timeslot timeslot, Radiology room) {
        // Iterate through all appointments to see if the technician is booked for this timeslot and room
        for (Appointment appointment : appointmentList) {
            if (isTechnicianBooked(technician, appointment, timeslot) ||
                    isRoomAvailable(appointment, room, timeslot, technician.getLocation())) {
                return false; // Technician or room is already booked
            }
        }
        return true; // Technician and room are both available
    }

    /**
     * check if the user's requested technician is busy at the time and room they requested
     *
     * @param technician The technician user requested
     * @param appointment The appointment to check
     * @param timeslot The timeslot user requested
     * @return true if the technician is busy and false otherwise.
     */
    private boolean isTechnicianBooked(Technician technician, Appointment appointment, Timeslot timeslot) {
        return appointment instanceof Imaging &&
                appointment.getProvider().equals(technician) &&
                appointment.getTimeslot().equals(timeslot);
    }

    /**
     * see if the imaging room that the user requested is taken
     *
     * @param appointment The appointment to check against.
     * @param room The imaging service room to compare.
     * @param timeslot The timeslot to compare.
     * @param technicianLocation The location of the technician.
     * @return true if the room is taken and false if not.
     */
    private boolean isRoomAvailable(Appointment appointment, Radiology room, Timeslot timeslot, Location technicianLocation) {
        return appointment instanceof Imaging &&
                ((Imaging) appointment).getRoom().equals(room) &&
                appointment.getTimeslot().equals(timeslot) &&
                ((Provider) appointment.getProvider()).getLocation().equals(technicianLocation);
    }

    /**
     * Schedules an appointment with a doctor using the provided command tokens.
     *
     * Validates input, converts tokens to relevant types, creates a patient,
     * retrieves the doctor by NPI, and adds the appointment to the list if valid.
     *
     * @param tokens An array of strings containing the command data for scheduling.
     */
    private void scheduleWithADoctor(String[] tokens) {
        // Check for D command
        if (tokens.length != 7 || !tokens[0].equalsIgnoreCase("D")) {
            System.out.println("Missing data tokens.");
            return;
        }

        if (!validateAppointmentDate( tokens[1])) return;
        Date appointmentDate = convertToDate(tokens[1]);

        Timeslot timeslot = convertToTimeslot(tokens[2]);
        if (timeslot == null) {
            System.out.println(tokens[2] + " is not a valid time slot.");
            return;
        }
        if (!checkDOB(tokens[5])) {
            return; // Stop if the DOB is invalid
        }

        // Get patient information from the command line and create a Patient object
        Profile patientProfile = new Profile(tokens[3].trim(), tokens[4].trim(), convertToDate(tokens[5]));
        Patient patient = new Patient(patientProfile);

        // Get the NPI from the command
        String npi = tokens[6].trim();
        Doctor provider = findDoctorThroughNPI(npi); // You need to implement this method to find the doctor by NPI
        if (provider == null) {
            System.out.println(npi + " - provider doesn't exist.");
            return;
        }

        // Pass the Patient and Doctor (both are Person types) to the Appointment constructor
        Appointment newAppointment = new Appointment(appointmentDate, timeslot, patient, provider);

        if (!isAppointmentSchedulable(newAppointment, provider)) return;

        appointmentList.add(newAppointment);
        System.out.println(newAppointment.toString() + " booked.");
    }

    /**
     * Validates the provided date of birth (DOB).
     *
     * Checks if the DOB is:
     * - A valid date.
     * - Not today's date.
     * - Not a future date.
     *
     * @param dobStr the date of birth as a string.
     * @return {@code true} if valid; {@code false} otherwise.
     */
    private static boolean checkDOB(String dobStr) {
        Date dob = convertToDate(dobStr);

        // Check if the date is null, meaning it couldn't be parsed correctly
        if (dob == null || !dob.isValid()) {
            System.out.println("Patient dob: " + dobStr + " is not a valid calendar date");
            return false; // Invalid date
        }

        // Check if the DOB is today
        if (dob.isToday()) {
            System.out.println("Patient dob: " + dobStr + " is today or a date after today.");
            return false;
        }

        // Check if the DOB is in the future
        Calendar today = Calendar.getInstance();
        Date todayDate = new Date(today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH), today.get(Calendar.YEAR));
        if (dob.compareTo(todayDate) > 0) {
            System.out.println("Patient dob: " + dobStr + " is today or a date after today.");
            return false;
        }

        return true; // DOB is valid
    }

    /**
     * Finds a doctor through their NPI.
     *
     * @param nationalProviderIdentifier The doctor's NPI.
     * @return The Doctor object if found; otherwise, null.
     */
    private Doctor findDoctorThroughNPI(String nationalProviderIdentifier) {
        for (int i = 0; i < providerList.size(); i++) {
            Provider currentProvider = providerList.get(i);
            if (currentProvider instanceof Doctor && ((Doctor) currentProvider).getNpi().equals(nationalProviderIdentifier)) {
                return (Doctor) currentProvider; // Return found Doctor
            }
        }
        return null; // No doctor found
    }

    /**
     * Determines if a proposed appointment can be scheduled.
     *
     * Checks for conflicts with existing appointments and verifies the provider's availability.
     *
     * @param proposedAppointment The appointment to be scheduled.
     * @param assignedProvider The provider assigned to the appointment.
     * @return true if the appointment can be scheduled; false otherwise.
     */
    private boolean isAppointmentSchedulable(Appointment proposedAppointment, Provider assignedProvider) {
        // Check for conflicts with existing appointments
        if (appointmentList.contains(proposedAppointment)) {
            System.out.println(proposedAppointment.getPatient().getProfile().toString() + " has an existing appointment at the same time slot.");
            return false; // Appointment cannot be scheduled due to conflict
        }

        // Check if the provider is available for the proposed appointment
        if (!isDocAvailable(assignedProvider, proposedAppointment)) {
            System.out.println(assignedProvider.toString() + " is not available at slot " + proposedAppointment.getTimeslot().getSlot());
            return false; // Provider is not available
        }

        return true; // Appointment can be scheduled
    }

    /**
     * Prints all office appointments sorted by county, date, and time.
     *
     * If the schedule is empty, a message is displayed indicating that there are no appointments.
     * Only non-imaging appointments are printed.
     */
    private void printOnlyOfficeAppointments() {
        if (appointmentList.isEmpty()) {
            System.out.println("Schedule calendar is empty.");
            return; // Exit the method early if there are no appointments
        } else {
            System.out.println();
            sort.appointment(appointmentList, 'O');
            System.out.println("** List of office appointments ordered by county/date/time.");
            for (int i = 0; i < appointmentList.size(); i++) {
                Appointment appointment = appointmentList.get(i);
                if (!(appointment instanceof Imaging)) {
                    System.out.println(appointment);
                }
            }
            System.out.println("** end of list **");
        }
    }

    /**
     * prints appointment list by date, time and provider
     * if empty, will display "Schedule calendar is empty."
     */
    private void DateSort() {
        if (appointmentList.isEmpty()) {
            System.out.println("Schedule calendar is empty.");
            return; // Exits method
        } else {
            System.out.println();
            sort.appointment(appointmentList, 'A' );
            System.out.println("** List of appointments, ordered by date/time/provider.");
            for (int i = 0; i < appointmentList.size(); i++) {
                System.out.println(appointmentList.get(i));
            }
            System.out.println("** end of list **");
        }
    }
    /**
     * Sorts and prints the list of appointments by patient name/date/time.
     *
     * If the appointment list is empty, a message is displayed indicating that there are no appointments.
     * The sorted list is printed to the console.
     */
    private void patientSort() {
        if (appointmentList.isEmpty()) {
            System.out.println("Schedule calendar is empty.");
            return; // Exits method
        } else {
            sort.appointment(appointmentList, 'P');
            System.out.println();
            System.out.println("** List of appointments, ordered by patient.");
            for (int i = 0; i < appointmentList.size(); i++) {
                System.out.println(appointmentList.get(i));
            }
            System.out.println("** end of list **");
        }
    }

    /**
     * Sorts and prints the list of appointments by county/date/time.
     *
     * If the appointment list is empty, a message is displayed indicating that there are no appointments.
     * The sorted list is printed to the console in order of county, date, and time.
     */
    private void countySort() {
        if (appointmentList.isEmpty()) {
            System.out.println("Schedule calendar is empty.");
            return; // Exits method
        } else {
            sort.appointment(appointmentList, 'L');
            System.out.println();
            System.out.println("** List of appointments, ordered by county/date/time.");
            for (int i = 0; i < appointmentList.size(); i++) {
                System.out.println(appointmentList.get(i));
            }
            System.out.println("** end of list **");
        }
    }

    /**
     * Prints the list of imaging appointments ordered by county/date/time.
     * If the appointment list is empty, it notifies the user.
     */
    private void printOnlyImagingAppointments() {
        if (appointmentList.isEmpty()) {
            System.out.println("Schedule calendar is empty.");
            return; // Exists method
        } else {
            System.out.println();
            sort.appointment(appointmentList,'I');
            System.out.println("** List of radiology appointments ordered by county/date/time.");
            for (int i = 0; i < appointmentList.size(); i++) {
                Appointment appointment = appointmentList.get(i);
                if (appointment instanceof Imaging) {
                    System.out.println(appointment);
                }
            }
            System.out.println("** end of list **");
        }
    }

    /**
     * Prints the billing statement ordered by patient. If the appointment list is empty, it notifies the user.
     */
    private void billingStatement() {
        if (appointmentList.isEmpty()) {
            System.out.println("Schedule calendar is empty.");
            return; // Exits method
        }

        System.out.println("\n** Billing statement ordered by patient. **");

        // Sort the appointmentList by patient (using the key 'P' for patient sorting)
        sort.appointment(appointmentList, 'P');

        // Array to track printed patients
        String[] printedNames = new String[appointmentList.size()];
        int printedCount = 0;

        // Process the appointments
        processAppointments(printedNames, printedCount);

        clearAppointmentList();
        System.out.println("** end of list **");
    }

    /**
     * Clears the appointment list.
     */
    private void clearAppointmentList() {
        while (!appointmentList.isEmpty()) {
            Appointment appointment = appointmentList.get(0);
            appointmentList.remove(appointment);
        }
    }

    /**
     * Adds all visits associated with a patient to the current patient object.
     * @param currentPatient the patient object to which visits are added
     * @param patient the original patient whose visits are to be added
     * @param index the starting index for iteration
     */
    private void addPatientVisits(Patient currentPatient, Patient patient, int index) {
        for (int k = index; k < appointmentList.size(); k++) {
            Appointment visitAppointment = appointmentList.get(k);
            if (visitAppointment.getPatient().equals(patient)) {
                Visit visit = new Visit(visitAppointment);
                currentPatient.addVisit(visit);
            } else {
                break; // Stop when reaching a different patient
            }
        }
    }

    /**
     * Processes the appointments to generate billing statements.
     *
     * @param printedNames An array to track unique patient identifiers that have been printed.
     * @param printedCount A count of how many unique patients have been printed.
     */
    private void processAppointments(String[] printedNames, int printedCount) {
        // Iterate through the sorted appointmentList
        for (int i = 0; i < appointmentList.size(); i++) {
            Appointment appointment = appointmentList.get(i);
            Patient patient = (Patient) appointment.getPatient();

            // Ensure patient and profile are not null
            if (patient == null || patient.getProfile() == null) {
                continue; // Skip if patient or profile is not available
            }

            String fullName = patient.getProfile().getFname() + " " + patient.getProfile().getLname();
            String dob = patient.getProfile().getDob().toString(); // Format this as necessary

            // Create a unique identifier for the patient using their full name and DOB
            String uniqueIdentifier = fullName + " " + dob;

            // Check if this unique identifier has already been printed
            boolean alreadyPrinted = false;
            for (int j = 0; j < printedCount; j++) {
                if (printedNames[j].equals(uniqueIdentifier)) {
                    alreadyPrinted = true;
                    break;
                }
            }

            // If not printed yet, calculate and print the billing statement
            if (!alreadyPrinted) {
                // Create a new Patient object for this patient (optional, depends on your logic)
                Patient currentPatient = new Patient(patient.getProfile());
                addPatientVisits(currentPatient, patient, i);

                // Calculate the total charges for this patient
                int totalCharge = currentPatient.charge(); // Calculate the total charges from visits

                // Print the billing statement for the patient
                System.out.printf("(%d) %s %s [due: $%d.00]%n",
                        printedCount + 1, fullName, dob, totalCharge);

                // Add to the array to avoid duplicates
                printedNames[printedCount++] = uniqueIdentifier;
            }
        }
    }

    /**
     * Prints the expected credit amounts for providers based on the appointments.
     * If the appointment list is empty, it notifies the user.
     */
    private void providerCredits() {
        if (appointmentList.isEmpty()) {
            System.out.println("Schedule calendar is empty.");
            return; // Exit the method early if there are no appointments
        }

        System.out.println("\n** Credit amount ordered by provider. **");
        List<Provider> uniqueProviders = getUniqueProviders();

        // Track printed providers
        String[] printedProviders = new String[uniqueProviders.size()];
        int printedCount = 0;

        // Iterate through the sorted uniqueProviders
        for (Provider provider : uniqueProviders) {
            if (provider.getProfile() == null) {
                continue; // Skip if provider profile is not available
            }

            String uniqueIdentifier = getUniqueIdentifier(provider);

            // Check if this unique identifier has already been printed
            if (!hasBeenPrinted(printedProviders, printedCount, uniqueIdentifier)) {
                int totalCharge = calculateTotalCharge(provider);

                printProviderCredit(provider, totalCharge);
                printedProviders[printedCount++] = uniqueIdentifier; // Avoid duplicates
            }
        }

        System.out.println("** end of list **");
    }

    /**
     * Extracts unique providers from the appointment list and sorts them by last name.
     *
     * @return A list of unique providers.
     */    private List<Provider> getUniqueProviders() {
        List<Provider> uniqueProviders = new List<>();
        for (int i = 0; i < appointmentList.size(); i++) {
            Appointment appointment = appointmentList.get(i);
            Provider provider = (Provider) appointment.getProvider();
            if (provider != null && !uniqueProviders.contains(provider)) {
                uniqueProviders.add(provider);
            }
        }
        sort.provider(uniqueProviders); // Sort the list by last name
        return uniqueProviders;
    }

    /**
     * Creates a unique identifier for the provider based on their type.
     *
     * @param provider The provider for which to create the unique identifier.
     * @return A string representing the unique identifier for the provider.
     */
    private String getUniqueIdentifier(Provider provider) {
        if (provider instanceof Doctor doctor) {
            return doctor.getName() + " " + doctor.getSpecialty(); // For Doctor
        } else if (provider instanceof Technician technician) {
            return technician.getName(); // For Technician
        }
        return " ";
    }

    /**
     * Checks if a provider has already been printed in the billing statement.
     *
     * @param printedProviders An array of printed provider names.
     * @param printedCount The count of providers that have already been printed.
     * @param uniqueIdentifier The unique identifier of the provider to check.
     * @return true if the provider has already been printed; false otherwise.
     */
    private boolean hasBeenPrinted(String[] printedProviders, int printedCount, String uniqueIdentifier) {
        for (int j = 0; j < printedCount; j++) {
            if (printedProviders[j].equals(uniqueIdentifier)) {
                return true; // Already printed
            }
        }
        return false; // Not printed yet
    }

    /**
     * Calculates the total charge for a provider based on their appointments.
     *
     * @param provider The provider for whom to calculate the total charge.
     * @return The total charge for the provider.
     */
    private int calculateTotalCharge(Provider provider) {
        int totalCharge = 0;
        for (int k = 0; k < appointmentList.size(); k++) {
            Appointment visitAppointment = appointmentList.get(k);
            if (visitAppointment.getProvider().equals(provider)) {
                if (provider instanceof Doctor doctor) {
                    totalCharge += doctor.rate(); // Rate for Doctor
                } else if (provider instanceof Technician technician) {
                    totalCharge += technician.rate(); // Rate for Technician
                }
            }
        }
        return totalCharge;
    }

    /**
     * Prints the credit details for a provider based on their total charges.
     *
     * @param provider The provider whose credit details are to be printed.
     * @param totalCharge The total amount due to the provider.
     */
    private void printProviderCredit(Provider provider, int totalCharge) {
        if (provider instanceof Doctor doctor) {
            System.out.printf("%s %s [credit amount: $%d.00]%n",
                    doctor.getName(), doctor.getDob(), totalCharge);
        } else if (provider instanceof Technician technician) {
            System.out.printf("%s %s [credit amount: $%d.00]%n",
                    technician.getName(), technician.getDob(), totalCharge);
        }
    }

}


