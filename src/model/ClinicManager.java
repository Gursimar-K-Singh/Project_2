package model;
import util.Date;
import util.sort;

import java.util.Calendar;
import java.util.Scanner;
import util.List; // Import the new List class
import util.CircleList;

public class ClinicManager {

    private List<Provider> providerList;
    private List<Appointment> appointmentList;
    private CircleList<Technician> technicianList;

    public ClinicManager() {
        providerList = new List<>();
        appointmentList = new List<>();
        this.technicianList = new CircleList<>();

        loadProviderList(); //load + sort providers + instantiate technician rotation
        initializeTechnicianRotation(); //load technician rotation circular list (before sorting providers)
        displayProviders(); //print providers
        printTechnicianRotation(); //print technician rotation circular list
    }

    /**
     * Reads provider details from the "providers.txt" file and adds each provider
     * to the providerList. Displays an error message if the file cannot be found.
     */
    public void loadProviderList() {
        try {
            // Load the file from the package using getResourceAsStream
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
     * Prints the list of providers after sorting by profile.
     */
    private void displayProviders() {
        sort.provider(providerList); // Assumes providerList has been renamed as suggested earlier
        for(int i = 0; i < providerList.size(); i++){
            System.out.println(providerList.get(i).toString());
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
                case "D": scheduleOfficeAppointment(tokens); break;
                case "T": scheduleImagingAppointment(tokens); break;
                case "R": rescheduleAppointment(tokens); break;
                case "C": cancelAppointment(tokens); break;
                case "PO": printOfficeAppointments() ; break;
                case "PI": printImagingAppointments(); break;
                case "PA": printAppointmentsByDateTimeProvider(); break;
                case "PP": printAppointmentsByPatient(); break;
                case "PL": printAppointmentsByCountyDateTime(); break;
                case "PS": printBillingStatement();break;
                case "PC": printProviderCredits(); break;
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

    /**
     * Validates the provided appointment date.
     * Checks if the appointment date is:
     * - A valid calendar date.
     * - Not today or in the past.
     * - Not on a weekend (Saturday or Sunday).
     * - Within six months from the current date.
     *
     * @param dateStr the appointment date as a string.
     * @return {@code true} if the date is valid; {@code false} otherwise.
     */
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

    /**
     * Parses a timeslot from a string representation.
     *
     * @param timeslotStr The string representation of the timeslot (1 to 12).
     * @return The corresponding Timeslot object, or null if the input is invalid.
     */
    private Timeslot parseTimeslot(String timeslotStr) {
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


    /**
     * Reschedules an existing office appointment based on provided command tokens.
     *
     * @param tokens An array of strings representing the command and its parameters.
     */
    private void rescheduleAppointment(String[] tokens) {
        // Check if the number of arguments is valid for the R command
        if (tokens.length != 7) {
            System.out.println("Missing data tokens.");
            return;
        }

        // Parse the appointment date and timeslots
        Date appointmentDate = convertToDate(tokens[1]);
        Timeslot oldTimeslot = parseTimeslot(tokens[2]);
        Profile patientProfile = new Profile(tokens[3].trim(), tokens[4].trim(), convertToDate(tokens[5].trim())); // patient details
        Timeslot newTimeslot = parseTimeslot(tokens[6]);

        // Find the appointment to reschedule
        Appointment appointmentToReschedule = FindAppointment(appointmentDate, oldTimeslot, patientProfile);

        if (appointmentToReschedule == null) {
            System.out.println(appointmentDate + " " + oldTimeslot + " " + patientProfile.getFname() + " " + patientProfile.getLname() + " " + patientProfile.getDob() + " does not exist.");
            return;
        }

        // Check if the appointment is an imaging appointment
        if (appointmentToReschedule instanceof Imaging) {
            System.out.println("Imaging appointments cannot be rescheduled.");
            return;
        }

        // Cast Person to Patient for conflict checking
        Patient patient = (Patient) appointmentToReschedule.getPatient();

        // Check if the patient has an existing appointment at the new timeslot
        if (hasExistingAppointment(patient, appointmentDate, newTimeslot)) {
            System.out.println(patientProfile.toString() + " has an existing appointment at " + appointmentDate + " " + newTimeslot.toString());
            return;
        }

        // Cast Person to Provider for the provider
        Provider provider = (Provider) appointmentToReschedule.getProvider();

        // Create the new appointment with the cast provider
        Appointment newAppointment = new Appointment(appointmentDate, newTimeslot, patient, provider);

        // Check if the provider is available for the new timeslot
        if (!isDoctorAvailable(provider, newAppointment)) {
            System.out.println(provider.toString() + " is not available at slot " + newTimeslot.getNumericSlot() + ".");
            return;
        }

        // Remove the old appointment and add the new one
        appointmentList.remove(appointmentToReschedule);
        appointmentList.add(newAppointment);
        System.out.println("Rescheduled to " + newAppointment.toString());
    }

    /**
     * Checks if the given doctor is available for the given appointment.
     *
     * @param provider The doctor to check.
     * @param newAppointment The new appointment to check against existing ones.
     * @return true if the doctor is available, false if they are already booked for the given timeslot.
     */
    private boolean isDoctorAvailable(Provider provider, Appointment newAppointment) {
        // Ensure the provider is a Doctor
        if (!(provider instanceof Doctor)) {
            return false; // Not a doctor, can't check availability
        }

        Doctor doctor = (Doctor) provider; // Cast provider to Doctor

        // Iterate through existing appointments to check for conflicts
        for (Appointment existingAppointment : appointmentList) {
            if (isSameDoctor(doctor, existingAppointment) && isSameDateAndTimeslot(newAppointment, existingAppointment)) {
                return false; // Doctor is busy during the new appointment
            }
        }
        return true; // Doctor is available
    }


    /**
     * Checks if the given appointment is for the same doctor.
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
     * Checks if the given appointment has the same date and timeslot as another appointment.
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
     * Searches for an appointment to reschedule based on the specified date,
     * timeslot, and patient profile.
     *
     * @param appointmentDate The date of the appointment.
     * @param timeslot The old timeslot of the appointment.
     * @param patientProfile The patient's profile.
     * @return The matching appointment if found; null otherwise.
     */
    private Appointment FindAppointment(Date appointmentDate, Timeslot timeslot, Profile patientProfile) {
        for (Appointment appointment : appointmentList) {
            if (isMatchingAppointment(appointment, appointmentDate, timeslot, patientProfile)) {
                return appointment; // Return the found appointment
            }
        }
        return null; // Return null if no matching appointment is found
    }

    /**
     * Checks if the given appointment matches the specified date, timeslot,
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
     * Determines whether a patient has an appointment scheduled for the specified date and timeslot.
     *
     * @param patient   The patient whose appointments are being checked.
     * @param date      The date of the appointment to verify.
     * @param timeslot  The timeslot of the appointment to verify.
     * @return true if the patient has a scheduled appointment at the specified date and timeslot; false otherwise.
     */
    private boolean hasExistingAppointment(Patient patient, Date date, Timeslot timeslot) {
        for (Appointment appointment : appointmentList) {
            if (isMatchingAppointment(appointment, date, timeslot, patient.getProfile())) {
                return true; // Patient has an existing appointment at the same date and timeslot
            }
        }
        return false;
    }

    /**
     * Parses the imaging service room from the string input (X-ray, ultrasound, CAT scan).
     *
     * @param imagingService the string representation of the imaging service
     * @return the corresponding Radiology enum value, or null if the service is invalid
     */
    private Radiology parseImagingService(String imagingService) {
        try {
            return Radiology.valueOf(imagingService.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // Invalid service
        }
    }

    /**
     * Cancels a scheduled appointment (office or imaging).
     *
     * @param tokens Array of strings containing appointment details
     */
    private void cancelAppointment(String[] tokens) {
        // Check if the number of arguments is valid for the C command
        if (tokens.length != 6) {
            System.out.println("Missing data tokens.");
            return;
        }

        // Parse the necessary components common to both types of appointments
        Date appointmentDate = convertToDate(tokens[1]); // date of the appointment
        Timeslot timeslot = parseTimeslot(tokens[2]); // timeslot of the appointment
        Profile patientProfile = new Profile(tokens[3].trim(), tokens[4].trim(), convertToDate(tokens[5].trim())); // patient details

        // Try to locate the appointment (whether office or imaging)
        Appointment appointmentToCancel = FindAppointment(appointmentDate, timeslot, patientProfile);

        // Format the output directly in this method
        String formattedDate = appointmentDate.toString(); // Assuming your Date class has a valid toString() method
        String formattedTime = timeslot.toString(); // Assuming Timeslot's toString gives the correct time representation
        String formattedName = patientProfile.getFname() + " " + patientProfile.getLname();
        String formattedDob = patientProfile.getDob().toString(); // Assuming Date class handles DOB formatting

        // Prepare the formatted output
        String output = formattedDate + " " + formattedTime + " " + formattedName + " " + formattedDob;

        // Now, attempt to find and cancel the appointment
        if (appointmentToCancel != null) { // If found
            appointmentList.remove(appointmentToCancel); // Remove the appointment
            System.out.println(output + " - appointment has been canceled.");
        } else {
            // If not found, notify the user
            System.out.println(output + " - appointment does not exist.");
        }
    }

    /**
     * Schedules an imaging appointment based on the provided command tokens.
     *
     * <p>This method validates the command input, extracts appointment details,
     * checks for existing appointments, and assigns a technician. If successful,
     * it adds the appointment to the schedule and displays the booking information.
     * If any validation fails, appropriate messages are shown to the user.</p>
     *
     * @param tokens An array of strings containing command input for scheduling
     *               the imaging appointment, including date, timeslot, patient
     *               information, and imaging service.
     */
    private void scheduleImagingAppointment(String[] tokens) {
        // Validate command tokens for T (Imaging appointment)
        if (tokens.length != 7 || !tokens[0].equalsIgnoreCase("T")) {
            System.out.println("Missing Date tokens");
            return;
        }

        if (!validateAppointmentDate(tokens[1])) return;
        Date scheduledDate  = convertToDate(tokens[1]);

        // Parse timeslot
        Timeslot timeslot = parseTimeslot(tokens[2]);
        if (timeslot == null) {
            System.out.println(tokens[2] + " is not a valid time slot.");
            return;
        }

        if (!checkDOB(tokens[5])) {
            return;
        }

        // Get patient information from the command line and create a Patient object
        Profile patientProfile = new Profile(tokens[3].trim(), tokens[4].trim(), convertToDate(tokens[5]));
        Patient patient = new Patient(patientProfile);

        // Check if the patient already has an appointment at this time
        if (hasExistingAppointment(patient, scheduledDate , timeslot)) {
            System.out.println(patientProfile.toString() + " has an existing appointment at the same time slot.");
            return;
        }

        // Parse imaging service (X-ray, ultrasound, or CAT scan)
        Radiology room = parseImagingService(tokens[6]);
        if (room == null) {
            System.out.println(tokens[6] + " - imaging service not provided.");
            return;
        }

        // Find the next available technician
        Technician assignedTechnician = findTechnician(timeslot, room);
        if (assignedTechnician == null) {
            System.out.println("Cannot find an available technician at all locations for " + room.name() + " at slot " + timeslot.getNumericSlot() + ".");
            return;
        }

        // Create and book the imaging appointment
        Imaging imagingAppointment = new Imaging(scheduledDate , timeslot, patient, assignedTechnician, room);
        appointmentList.add(imagingAppointment);

        // Output the correctly formatted booking information
        System.out.println(scheduledDate  + " " + timeslot + " " + patient + " " + assignedTechnician + "[" + room + "] booked.");
    }

    /**
     * Finds an available technician for the specified timeslot and imaging service.
     *
     * @param timeslot the timeslot for which an available technician is sought
     * @param room     the imaging service room required for the appointment
     * @return the available Technician, or null if none is found
     */
    private Technician findTechnician(Timeslot timeslot, Radiology room) {
        // Loop through the technician list
        for (int i = 0; i < technicianList.size(); i++) {
            Technician technician = technicianList.getNext(); // Get the next technician in the rotation

            if (isTechnAvailable(technician, timeslot, room)) {
                return technician; // Technician is available, return immediately
            }
        }

        // If no technician is found, return null with appropriate message
        //System.out.println("Cannot find an available technician at all locations for " + room.name() + " at slot " + timeslot.getNumericSlot() + ".");
        return null;
    }

    /**
     * Checks if a technician is available for a specified timeslot and imaging service room.
     *
     * @param technician the technician to check for availability
     * @param timeslot   the timeslot to check
     * @param room       the imaging service room to check
     * @return true if the technician is available; false otherwise
     */
    private boolean isTechnAvailable(Technician technician, Timeslot timeslot, Radiology room) {
        // Iterate through all appointments to see if the technician is booked for this timeslot and room
        for (Appointment appointment : appointmentList) {
            if (isTechnicianBooked(technician, appointment, timeslot) ||
                    isRoomBooked(appointment, room, timeslot, technician.getLocation())) {
                return false; // Technician or room is already booked
            }
        }
        return true; // Technician and room are both available
    }

    /**
     * Checks if the technician is booked for the specified appointment and timeslot.
     *
     * @param technician The technician to check.
     * @param appointment The appointment to check against.
     * @param timeslot The timeslot to check.
     * @return true if the technician is booked; false otherwise.
     */
    private boolean isTechnicianBooked(Technician technician, Appointment appointment, Timeslot timeslot) {
        return appointment instanceof Imaging &&
                appointment.getProvider().equals(technician) &&
                appointment.getTimeslot().equals(timeslot);
    }

    /**
     * Checks if the imaging room is booked for the specified appointment, timeslot, and location.
     *
     * @param appointment The appointment to check against.
     * @param room The imaging service room to check.
     * @param timeslot The timeslot to check.
     * @param technicianLocation The location of the technician.
     * @return true if the room is booked; false otherwise.
     */
    private boolean isRoomBooked(Appointment appointment, Radiology room, Timeslot timeslot, Location technicianLocation) {
        return appointment instanceof Imaging &&
                ((Imaging) appointment).getRoom().equals(room) &&
                appointment.getTimeslot().equals(timeslot) &&
                ((Provider) appointment.getProvider()).getLocation().equals(technicianLocation);
    }

    /**
     * Schedules an imaging appointment based on the provided command tokens.
     *
     * <p>This method validates the command input, extracts appointment details,
     * checks for existing appointments, and assigns a technician. If successful,
     * it adds the appointment to the schedule and displays the booking information.
     * If any validation fails, appropriate messages are shown to the user.</p>
     *
     * @param tokens An array of strings containing command input for scheduling
     *               the imaging appointment, including the command,
     *               appointment date, timeslot, patient's first name, last name,
     *               date of birth, and imaging service.
     */
    private void scheduleOfficeAppointment(String[] tokens) {
        // Check for D command
        if (tokens.length != 7 || !tokens[0].equalsIgnoreCase("D")) {
            System.out.println("Error: Missing or invalid data tokens.");
            return;
        }


        if (!validateAppointmentDate( tokens[1])) return;
        Date appointmentDate = convertToDate(tokens[1]);

        Timeslot timeslot = parseTimeslot(tokens[2]);
        if (timeslot == null) {
            System.out.println(tokens[2] + " is not a valid time slot.");
            return;
        }
        if (!checkDOB(tokens[5])) {
            return; // Stop if the DOB is invalid
        }

        // Get patient information from the command line and create a Patient object
        Profile patientProfile = new Profile(tokens[3].trim(), tokens[4].trim(), convertToDate(tokens[5]));
        Patient patient = new Patient(patientProfile);  // Assuming Patient has a constructor that accepts Profile

        // Get the NPI from the command
        String npi = tokens[6].trim();
        Doctor provider = findDoctorByNPI(npi); // You need to implement this method to find the doctor by NPI
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
            System.out.println("Patient dob: " + dobStr + " is not a valid calendar date.");
            return false; // Invalid date
        }

        // Check if the DOB is today
        if (dob.isToday()) {
            System.out.println("Patient dob: " + dobStr + " is today or a date after today.");
            return false; // Assuming DOB cannot be today, return false
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
     * Retrieves a doctor by their National Provider Identifier (NPI).
     * This method searches through the list of providers to find a doctor with the specified NPI.
     *
     * @param nationalProviderIdentifier The National Provider Identifier (NPI) of the doctor to be found.
     * @return The Doctor object if found; otherwise, returns null if no matching doctor exists.
     */
    private Doctor findDoctorByNPI(String nationalProviderIdentifier) {
        for (int i = 0; i < providerList.size(); i++) {
            Provider currentProvider = providerList.get(i);
            if (currentProvider instanceof Doctor && ((Doctor) currentProvider).getNpi().equals(nationalProviderIdentifier)) {
                return (Doctor) currentProvider; // Return the found Doctor
            }
        }
        return null; // No doctor found with that NPI
    }

    /**
     * Determines whether the new appointment can be scheduled without conflicts.
     *
     * @param proposedAppointment The new appointment to schedule.
     * @param assignedProvider The provider for the appointment.
     * @return true if the appointment can be scheduled; false otherwise.
     */
    private boolean isAppointmentSchedulable(Appointment proposedAppointment, Provider assignedProvider) {
        // Check for conflicts with existing appointments
        if (appointmentList.contains(proposedAppointment)) {
            System.out.println(proposedAppointment.getPatient().getProfile().toString() + " has an existing appointment at the same time slot.");
            return false; // Appointment cannot be scheduled due to conflict
        }

        // Check if the provider is available for the proposed appointment
        if (!isDoctorAvailable(assignedProvider, proposedAppointment)) {
            System.out.println(assignedProvider.toString() + " is not available at slot " + proposedAppointment.getTimeslot().getNumericSlot());
            return false; // Provider is not available
        }

        return true; // Appointment can be scheduled
    }

    /**
     * Prints a list of office appointments ordered by county, date, and time.
     * If there are no appointments, a message indicating that the schedule is empty
     * will be displayed. Appointments of type {@link Imaging} will be excluded from
     * the output.
     */
    private void printOfficeAppointments() {
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
     * Prints a list of appointments ordered by appointment date, time, and provider.
     * If there are no appointments, a message indicating that the schedule is empty
     * will be displayed.
     */
    private void printAppointmentsByDateTimeProvider() {
        if (appointmentList.isEmpty()) {
            System.out.println("Schedule calendar is empty.");
            return; // Exit the method early if there are no appointments
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
     * Displays a list of appointments sorted by patient.
     * If the schedule calendar is empty, a message will indicate that there are
     * no appointments scheduled. When appointments are present, this method sorts
     * them by patient name and presents the details in a clear format.
     * The sorting is performed using a custom sorting method where 'P' indicates
     * that the appointments should be organized by patient. After sorting, a
     * complete list of appointments is displayed, followed by a note marking
     * the end of the list.
     */
    private void printAppointmentsByPatient() {
        if (appointmentList.isEmpty()) {
            System.out.println("Schedule calendar is empty.");
            return; // Exit the method early if there are no appointments
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
     * Displays a list of appointments sorted by county, date, and time.
     * If there are no appointments scheduled, you'll see a friendly message
     * letting you know that the schedule calendar is empty. When there are
     * appointments, the method will sort them by county and show you the
     * details in an easy-to-read format.
     * The sorting is handled by our custom sorting method, where 'L' stands
     * for sorting by county. Once sorted, you'll get a complete list,
     * followed by a note that indicates the end of the list.
     */
    private void printAppointmentsByCountyDateTime() {
        if (appointmentList.isEmpty()) {
            System.out.println("Schedule calendar is empty.");
            return; // Exit the method early if there are no appointments
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
     * Prints the billing statement ordered by patient. If the appointment list is empty, it notifies the user.
     */
    private void printBillingStatement() {
        if (appointmentList.isEmpty()) {
            System.out.println("Schedule calendar is empty.");
            return; // Exit the method early if there are no appointments
        }

        System.out.println("\n** Billing statement ordered by patient **");

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
                currentPatient.addVisit(visit); // Assuming getVisit() returns the Visit object
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
                System.out.printf("(%d) %s %s [amount due: $%d.00]%n",
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
    private void printProviderCredits() {
        if (appointmentList.isEmpty()) {
            System.out.println("Schedule calendar is empty.");
            return; // Exit the method early if there are no appointments
        }

        System.out.println("\n** Expected Credit Amounts for Providers **");

        // Extract and sort unique providers
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
            System.out.printf("%s [Specialty: %s] [Expected credit: $%d.00]%n",
                    doctor.getName(), doctor.getSpecialty(), totalCharge);
        } else if (provider instanceof Technician technician) {
            System.out.printf("%s [Rate per Visit: $%d] [Expected credit: $%d.00]%n",
                    technician.getName(), technician.rate(), totalCharge);
        }
    }

    /**
     * Prints the list of imaging appointments ordered by county/date/time.
     * If the appointment list is empty, it notifies the user.
     */
    private void printImagingAppointments() {
        if (appointmentList.isEmpty()) {
            System.out.println("Schedule calendar is empty.");
            return; // Exit the method early if there are no appointments
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

}


