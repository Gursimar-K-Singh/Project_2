package model;
//@author AparnaSrinivas @author GursimarSingh
/**
 * The Timeslot class represents a specific time during the day, identified by
 * the hour and minute in 24-hour format. It provides methods for comparing timeslots
 * and converting the timeslot to a readable string format (e.g., "09:30 AM").
 *
 * This class also provides several predefined static instances representing
 * common appointment times.
 *
 * It implements the Comparable interface, allowing timeslots to be compared based on
 * chronological order (hours and minutes).
 * @author AparnaSrinivas
 * @author GursimarSingh
 */
 public class Timeslot implements Comparable<Timeslot> {
    // Private instance variables for hour and minute
    private int hour;
    private int minute;

    //static-final constants for predefined timeslots (replace SLOT1, SLOT2, etc.)
    //morning slots
    public static final Timeslot SLOT1 = new Timeslot(9, 0);   //9:00am
    public static final Timeslot SLOT2 = new Timeslot(9, 30);  //9:30am
    public static final Timeslot SLOT3 = new Timeslot(10, 0);  //10:00am
    public static final Timeslot SLOT4 = new Timeslot(10, 30); //10:30am
    public static final Timeslot SLOT5 = new Timeslot(11, 0);  //11:00am
    public static final Timeslot SLOT6 = new Timeslot(11, 30);
    //afternoon slots
    public static final Timeslot SLOT7 = new Timeslot(2, 0);   //2:00pm
    public static final Timeslot SLOT8 = new Timeslot(2, 30);  //2:30pm
    public static final Timeslot SLOT9 = new Timeslot(3, 0);  //3:00pm
    public static final Timeslot SLOT10 = new Timeslot(3, 30); //3:30pm
    public static final Timeslot SLOT11 = new Timeslot(4, 0);  //4:00pm
    public static final Timeslot SLOT12 = new Timeslot(4, 30);//4:30pm

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
     * Returns a predefined Timeslot instance based on the index provided.
     *
     * @param index The index of the timeslot (1-6).
     * @return The corresponding predefined Timeslot (SLOT1, SLOT2, etc.).
     * @throws IllegalArgumentException If the index is invalid.
     */
    public static Timeslot getTimeSlot(int idx) {

        switch (idx) {
            case 1: return SLOT1;
            case 2: return SLOT2;
            case 3: return SLOT3;
            case 4: return SLOT4;
            case 5: return SLOT5;
            case 6: return SLOT6;

            case 7: return SLOT7;
            case 8: return SLOT8;
            case 9: return SLOT9;
            case 10: return SLOT10;
            case 11: return SLOT11;
            case 12: return SLOT12;

            default: throw new IllegalArgumentException("Invalid Timeslot index");
        }
    }

    /*public static void main(String[] args) {
        System.out.println("Available Time Slots:");

        // Iterate through each Timeslot and print its string representation
        for (Timeslot slot : Timeslot.values()) {
            System.out.println(slot.toString());
        }
    }*/

}
