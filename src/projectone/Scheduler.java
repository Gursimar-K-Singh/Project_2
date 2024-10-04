package projectone;//author @AparnaSrinivas author @GursimarSingh
import java.util.Scanner;
import java.util.Calendar;
import java.text.DecimalFormat;

import java.util.ServiceLoader;

public class Scheduler {
    public static List appointmentList;
    private Scanner scanner;
    public static MedicalRecord medicalRecord;

    public Scheduler() {
         appointmentList = new List();
         medicalRecord = new MedicalRecord(10);;
    }


    public void run() {
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        System.out.print("Scheduler is running.");

        while (running) {
            if (!sc.hasNextLine()) {
                break;
            }

            String input = sc.nextLine().trim();
            if (input.isEmpty()) continue; // Ignore empty lines

            String[] tokens = input.split(",");
            String theCommand = tokens[0];

            switch (theCommand) {
                case "S": // works
                    scheduleAppointment(tokens);
                    break;
                case "C": // does not work
                    cancelAppointment(tokens);
                    break;
                case "R": // does not work
                    rescheduleAppointment(tokens);
                    break;
                case "PA":
                    if(appointmentList.getSize() == 0){
                        System.out.print("The schedule calendar is empty.\n");
                        break;
                    }
                    System.out.print("\n ** Appointments ordered by date/time/provider ** \n");
                    appointmentList.printByAppointment();
                    System.out.print("** end of list **\n");
                    break;
                case "PP":

                    if(appointmentList.getSize() == 0){
                        System.out.print("The schedule calendar is empty.\n");
                        break;
                    }
                    System.out.print("\n ** Appointments ordered by patient/date/time **\n");
                    appointmentList.printByPatient();
                    System.out.print("** end of list **\n");
                    break;
                case "PL": // works
                    if(appointmentList.getSize() == 0){
                        System.out.print("The schedule calendar is empty.\n");
                        break;
                    }
                    System.out.print("\n ** Appointments ordered by county/date/time **\n");
                    appointmentList.printByLocation();
                    System.out.print("** end of list **\n");
                    break;
                case "PS": //
                    printBillingStatements();
                    break;
                case "Q":
                    System.out.println("Scheduler is terminated.");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid command.");
            }
        }

        sc.close();
    }


    private static void scheduleAppointment(String[] tokens){
        if (tokens.length != 7) {
            System.out.println("Invalid Command");
            return;
        }

        String dateStr = tokens[1].trim();
        String timeslotStr = tokens[2].trim();
        String firstName = tokens[3].trim();
        String lastName = tokens[4].trim();
        String dobStr = tokens[5].trim();
        String providerLastName = tokens[6].toUpperCase().trim();


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

        if (!checkProvider(providerLastName)) {
            System.out.println(providerLastName + " - Provider does not exist.");
            return; // Stop if the provider does not exist
        }

        Date appDate = parseDate(dateStr);
        Date dob = parseDate(dobStr);
        Profile patient = new Profile(firstName,lastName,dob);
        Timeslot timeslot = parseTimeslot(timeslotStr);;
        Provider provider = parseProvider(providerLastName);

        Appointment appointment = new Appointment(appDate,timeslot,patient,provider);

        if(appointmentList == null){

        }else if ( !appointmentList.isAvailable(appointment)) {
            System.out.println("The selected timeslot is not available for this provider.");
            return; // Stop if the timeslot is not available
        }

        if(appointmentList == null) {
            appointmentList = new List(); // Initialize appointmentList if it's null
        }

        if (appointmentList.contains(appointment)) {
            System.out.println("Appointment already exists.");
        } else {
            appointmentList.add(appointment);
            System.out.println(appointment + "booked");
        }

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

    private static boolean checkProvider(String providerName) {
        // Iterate through all providers in the Provider enum
        for (Provider provider : Provider.values()) {
            // Compare the provider's name with the input providerName (case-insensitive)
            if (provider.name().equalsIgnoreCase(providerName)) {
                return true; // Provider exists
            }
        }
        return false; // Provider does not exist
    }



    // creates a date
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

    private static void cancelAppointment(String[] tokens) {
        // Check for the correct number of tokens
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

        Appointment appointment = new Appointment(appDate,timeslot,patient,provider);

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

    private static Provider parseProvider(String providerStr) {
        try {
            return Provider.valueOf(providerStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static Appointment createOldAppointment(String dateStr, String oldTimeslotStr, String firstName, String lastName, String dobStr) {
        Date appDate = parseDate(dateStr);
        Timeslot oldTimeslot = Timeslot.valueOf("SLOT" + oldTimeslotStr);
        Date dob = parseDate(dobStr);
        Profile patient = new Profile(firstName, lastName, dob);
        return new Appointment(appDate, oldTimeslot, patient, null);
    }

    private static Appointment findExistingAppointment(Appointment oldAppointment) {
        for (int i = 0; i < appointmentList.getSize(); i++) {
            Appointment appointment = appointmentList.get(i);
            if (appointment.equals(oldAppointment)) {
                return appointment; // Return the found appointment
            }
        }
        return null; // Return null if no matching appointment is found
    }

    private static void rescheduleAppointment(String[] tokens) {
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

        if (!appointmentList.contains(oldAppointment)) {
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

        if (!appointmentList.isAvailable(newAppointment)) {
            System.out.println(provider + " is not available at " + newTimeslot +".");
            return;
        }

        handleAppointmentAddition(newAppointment);
        appointmentList.remove(oldAppointment);
        appointmentList.add(newAppointment);
        System.out.println("Rescheduled to "+ newAppointment.toString() );

    }



    private Patient findOrCreatePatient(Profile profile) {
        // Search for existing patient
        for (int i = 0; i < medicalRecord.getSize(); i++) {
            Patient patient = medicalRecord.getPatientAtIndex(i);
            if (patient.getProfile().equals(profile)) {
                return patient;
            }
        }
        // If not found, create new patient
        Patient newPatient = new Patient(profile);
        medicalRecord.add(newPatient);
        return newPatient;
    }

    private static void handleAppointmentAddition(Appointment newAppointment) {
        if (appointmentList == null) {
            appointmentList.add(newAppointment);
            System.out.println(newAppointment.toString() + " booked.");
            return;
        } else if (appointmentList.contains(newAppointment)) {
            System.out.println("Appointment already exists.");
            return;
        }
    }

    private void printBillingStatements() {
        System.out.println("\n** Billing statement ordered by patient **");
        moveAppointmentsToMedicalRecord();

        DecimalFormat df = new DecimalFormat("#,##0.00");
        bubbleSortPatientsByName();

        String[] printedNames = new String[medicalRecord.getSize()];
        int printedCount = 0;

        // Print billing statements
        for (int i = 0; i < medicalRecord.getSize(); i++) {
            Patient patient = medicalRecord.getPatientAtIndex(i);
            String fullName = patient.getProfile().getFname() + " " + patient.getProfile().getLname();
            String dob = patient.getProfile().getDob().toString(); // Assuming getDob() returns a String

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

            if (!alreadyPrinted) {
                System.out.println("(" + (printedCount + 1) + ") " + fullName + " " + dob +
                        " [amount due: $" + df.format(patient.charge()) + "]");
                printedNames[printedCount++] = uniqueIdentifier; // Add to the array to avoid duplicates
            }
        }

        System.out.println("** end of list **");
    }

    private void moveAppointmentsToMedicalRecord() {
        while (appointmentList.getSize() != 0) {
            Appointment appointment = appointmentList.removeFirst();
            Patient patient = findOrCreatePatient(appointment.getProfile());
            Visit visit = new Visit(appointment);
            patient.addVisit(visit);
            medicalRecord.add(patient);
        }
    }

    private void bubbleSortPatientsByName() {
        int size = medicalRecord.getSize();
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - 1 - i; j++) {
                Patient patient1 = medicalRecord.getPatientAtIndex(j);
                Patient patient2 = medicalRecord.getPatientAtIndex(j + 1);
                String fullName1 = patient1.getProfile().getLname() + ", " + patient1.getProfile().getFname();
                String fullName2 = patient2.getProfile().getLname() + ", " + patient2.getProfile().getFname();

                // Swap if they are in the wrong order
                if (fullName1.compareTo(fullName2) > 0) {
                    // Swap patients in the medical record
                    medicalRecord.swap(j, j + 1);
                }
            }
        }
    }

}

