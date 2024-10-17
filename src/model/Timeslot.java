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

    //constant
    private static final int SESSION_LENGTH = 30;// each session is 30 minute long
    private static final int NUMBER_OF_SESSION_FOR_SLOT = 6; // Both morning and afternoon have a total of 6 sessions
    private static final int STARTING_APPOINTMENT_HOUR_MORNING = 9; // the first session a user can schedule is at 9:00 am if user wants to schedule in the morning
    private static final int STARTING_APPOINTMENT_HOUR_AFTERNOON = 14;// // the first session a user can schedule is at 2:00p pm if user wants to schedule in the afternoon

    // Instance variables
    private final int hour;
    private final int minute;

    //  array used store all time slots
    public static final Timeslot[] TIME_SLOTS = TimeslotArray();

    /**
     * set minute and hour of timeslot object
     *
     * @param hour The hour of the timeslot.
     * @param minute The minute of the timeslot.
     */
    public Timeslot(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * checks if the hours and minutes are the same between two timeslot objects
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
     * checks which timeslot comes first
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
     * Displays the timeslot
     * if hours is greater than 12, converts back to standard time
     * e.g ( if hours = 14 systems will convert it so that it prints 2:00 pm)
     *
     * @return String of the timeslot
     */
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
     * Retrieves the numeric representation of this timeslot.
     * The first morning slot (9:00 AM) corresponds to slot 1, while the last afternoon slot (4:30 PM) corresponds to slot 12.
     *
     * @return the numeric value of the timeslot (ranging from 1 to 12)
     */
    public int getSlot() {
        for (int i = 0; i < TIME_SLOTS.length; i++) {
            if (this.equals(TIME_SLOTS[i])) {
                return i + 1;
            }
        }
        return -1;  // Invalid slot
    }

    /**
     * create array
     * Array description:
     * - 6 session in morning
     * - 6 session in afternoong
     * - each seasong has a length of 30 mintues
     * @return an array containing Timeslot objects
     */
    private static Timeslot[] TimeslotArray() {
        Timeslot[] timeslots = new Timeslot[NUMBER_OF_SESSION_FOR_SLOT * 2]; // Total of 12 slots (6 morning + 6 afternoon)

        int hour = STARTING_APPOINTMENT_HOUR_MORNING;
        int minute = 0;
        for (int i = 0; i < NUMBER_OF_SESSION_FOR_SLOT; i++) {
            timeslots[i] = new Timeslot(hour, minute);
            minute += SESSION_LENGTH;
            if (minute == 60) {
                minute = 0;
                hour++;
            }
        }

        hour = STARTING_APPOINTMENT_HOUR_AFTERNOON;
        minute = 0;
        for (int i = NUMBER_OF_SESSION_FOR_SLOT; i < NUMBER_OF_SESSION_FOR_SLOT * 2; i++) {
            timeslots[i] = new Timeslot(hour, minute);
            minute += SESSION_LENGTH;
            if (minute == 60) {
                minute = 0;
                hour++;
            }
        }

        return timeslots;
    }

}
