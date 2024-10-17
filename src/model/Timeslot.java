package model;
/**
 * The Timeslot class represents a specific time during the day, identified by
 * the hour and minute in 24-hour format. It provides methods for comparing timeslots
 * and converting the timeslot to a readable string format (e.g., "9:30 AM").
 * This class also provides several predefined static instances representing
 * common appointment times.
 * It implements the Comparable interface, allowing timeslots to be compared based on
 * chronological order (hours and minutes).
 *
 * @author GursimarSingh
 */
 public class Timeslot implements Comparable<Timeslot> {

    // Constants for the start times and slot durations
    private static final int MORNING_START_HOUR = 9;
    private static final int AFTERNOON_START_HOUR = 14; // 2:00 PM in 24-hour format
    private static final int SLOT_DURATION_MINUTES = 30;
    private static final int SLOTS_PER_SESSION = 6; // Number of slots per session (morning or afternoon)

    // Instance variables for hour and minute
    private final int hour;
    private final int minute;

    // Static array to store all time slots
    public static final Timeslot[] TIME_SLOTS = createTimeslots();

    /**
     * Constructor to initialize the hour and minute of the timeslot.
     *
     * @param hour The hour of the timeslot.
     * @param minute The minute of the timeslot.
     */
    public Timeslot(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * Generates 12 timeslots: 6 in the morning and 6 in the afternoon, each spaced 30 minutes apart.
     *
     * @return an array containing Timeslot objects
     */
    private static Timeslot[] createTimeslots() {
        Timeslot[] slots = new Timeslot[SLOTS_PER_SESSION * 2]; // Total of 12 slots (6 morning + 6 afternoon)

        // Create morning slots (9:00 AM to 11:30 AM)
        int hour = MORNING_START_HOUR;
        int minute = 0;
        for (int i = 0; i < SLOTS_PER_SESSION; i++) {
            slots[i] = new Timeslot(hour, minute);
            minute += SLOT_DURATION_MINUTES;
            if (minute == 60) {
                minute = 0;
                hour++;
            }
        }

        // Create afternoon slots (2:00 PM to 4:30 PM)
        hour = AFTERNOON_START_HOUR;
        minute = 0;
        for (int i = SLOTS_PER_SESSION; i < SLOTS_PER_SESSION * 2; i++) {
            slots[i] = new Timeslot(hour, minute);
            minute += SLOT_DURATION_MINUTES;
            if (minute == 60) {
                minute = 0;
                hour++;
            }
        }

        return slots;
    }


    @Override
    public String toString(){
        //defining time period (morning-AM/afternoon-PM) - if hour is < 12 --> AM, if hour > 12 --> PM
        String pd = (hour < 12) ? "AM"  : "PM";

        //converting hour from 24-hr format to 12-hr format
        //for midnight or 12 in afternoon --> displays 12; for any other value --> coversion from 24-hr to 12-hr
        int hrFormat = (hour == 0 || hour == 12) ? 12 : hour % 12;

        //formatting final return string
        return String.format("%d:%02d %s", hrFormat, minute, pd);
    }

    /**
     * Compares this Timeslot to another Timeslot based on time (hour and minute).
     *
     * @param otherTime The other Timeslot to compare.
     * @return A negative integer, zero, or a positive integer as this Timeslot
     *         is less than, equal to, or greater than the specified Timeslot.
     */
    @Override
    public int compareTo(Timeslot otherTime){

        if(this.hour != otherTime.hour){
            return (this.hour - otherTime.hour);
        }

        return this.minute - otherTime.minute;
    }

    /**
     * Checks if two Timeslots are equal by comparing their hour and minute values.
     *
     * @param obj The object to compare with this Timeslot.
     * @return true if the timeslots are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj){
        //if the two timeslots are equal - then return true
        if(this == obj){
            return true;
        }

        if(obj == null || (getClass() != obj.getClass())){
            return false;
        }
        //casting obj to a Timeslot object
        Timeslot correct_timeslot = (Timeslot) obj;

        //compare the hour & minute values
        return ((hour == correct_timeslot.hour) && (minute == correct_timeslot.minute));
    }

    /**
     * Retrieves the numeric representation of this timeslot.
     * The first morning slot (9:00 AM) corresponds to slot 1, while the last afternoon slot (4:30 PM) corresponds to slot 12.
     *
     * @return the numeric value of the timeslot (ranging from 1 to 12)
     */
    public int getNumericSlot() {
        for (int i = 0; i < TIME_SLOTS.length; i++) {
            if (this.equals(TIME_SLOTS[i])) {
                return i + 1;
            }
        }
        return -1;  // Invalid slot
    }

    //delete
    public static void main(String[] args) {
        // Print all timeslots
        System.out.println("All available timeslots:");
        for (Timeslot slot : Timeslot.TIME_SLOTS) {
            System.out.println(slot);
        }

        // Create a few Timeslot instances
        Timeslot morningSlot1 = new Timeslot(9, 0);   // 9:00 AM
        Timeslot morningSlot2 = new Timeslot(9, 30);   // 9:30 AM
        Timeslot afternoonSlot1 = new Timeslot(14, 0); // 2:00 PM
        Timeslot afternoonSlot2 = new Timeslot(14, 30); // 2:30 PM
        Timeslot afternoonSlot3 = new Timeslot(14, 30); // 2:30 PM (for equality test)

        // Print the timeslot string representations
        System.out.println("\nIndividual Timeslots:");
        System.out.println("Timeslot 1: " + morningSlot1);
        System.out.println("Timeslot 2: " + morningSlot2);
        System.out.println("Timeslot 3: " + afternoonSlot1);
        System.out.println("Timeslot 4: " + afternoonSlot2);

        // Compare the timeslots
        System.out.println("\nComparing Timeslots:");
        System.out.println("Comparing Timeslot 1 and Timeslot 2: " + morningSlot1.compareTo(morningSlot2));
        System.out.println("Comparing Timeslot 3 and Timeslot 4: " + afternoonSlot1.compareTo(afternoonSlot2));
        System.out.println("Comparing Timeslot 2 and Timeslot 1: " + morningSlot2.compareTo(morningSlot1));

        // Check equality of timeslots
        System.out.println("\nEquality Checks:");
        System.out.println("Timeslot 2 equals Timeslot 1: " + morningSlot2.equals(morningSlot1));
        System.out.println("Timeslot 2 equals Timeslot 3: " + morningSlot2.equals(afternoonSlot1));
        System.out.println("Timeslot 2 equals Timeslot 4: " + afternoonSlot2.equals(afternoonSlot3));

        // Get numeric slots
        System.out.println("\nNumeric Slot Values:");
        System.out.println("Numeric value of Timeslot 1 (9:00 AM): " + morningSlot1.getNumericSlot());
        System.out.println("Numeric value of Timeslot 4 (2:30 PM): " + afternoonSlot2.getNumericSlot());
    }

}
