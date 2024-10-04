package projectone;

import java.util.Objects;
//@author AparnaSrinivas @author GursimarSingh

public class Appointment implements Comparable<Appointment> {
    private Date date;
    private Timeslot timeslot;
    private Profile patient;
    private Provider provider;

    /**
     * Constructor to create an Appointment object.
     *
     * @param date The date of the appointment.
     * @param timeslot The timeslot of the appointment.
     * @param patient The profile of the patient.
     * @param provider The provider associated with the appointment.
     */

    public Appointment(Date date, Timeslot timeslot, Profile patient, Provider provider) {
        this.date = date;
        this.timeslot = timeslot;
        this.patient = patient;
        this.provider = provider;
    }

    /**
     * Gets the date of the appointment.
     *
     * @return The date of the appointment.
     */
    public Date getDate(){
        return date;
    }

    /**
     * Gets the timeslot of the appointment.
     *
     * @return The timeslot of the appointment.
     */
    public Timeslot getTimeslot() {
        return timeslot;
    }

    /**
     * Gets the patient's profile.
     *
     * @return The patient's profile.
     */
    public Profile getProfile() {
        return patient;
    }

    /**
     * Gets the appointment's provider.
     *
     * @return The appointment's provider.
     */
    public Provider getProvider(){
        return provider;
    }

    /**
     * Method compares current appointment to another appointment to check for equality.
     * Two appointments are equal if they have the same date, timeslot, and patient.
     *
     * @param obj Patient object to compare to current appointment.
     * @return True if the two appointments are equal, False otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }

        if(!(obj instanceof Appointment)){
            return false;
        }


        Appointment other = (Appointment) obj;

        return this.date.equals(other.date) &&
                this.timeslot.equals(other.timeslot) &&
                this.patient.equals(other.patient);
    }

    /**
     * Compares current appointment with another appointment.
     * Appointments first compared by date, then by timeslot, and then by patient profile.
     *
     * @param other other appointment to be compared to.
     * @return a negative integer, zero, or a positive integer as the current appointment is
     *         less than, equal to, or greater respectively than the appointment being compared to.
     */
    @Override
    public int compareTo(Appointment other) {
        int dateComparison = this.date.compareTo(other.date);

        // If dates are the same, compare timeslots
        if (dateComparison != 0) {
            return dateComparison;
        }

        int timeslotComparsion = this.timeslot.compareTo(other.timeslot);

        if(timeslotComparsion != 0){
            return timeslotComparsion;
        }

        return this.patient.compareTo(other.patient);
    }

    //10/30/2024 10:45 AM John Doe 12/13/1989 [PATEL, BRIDGEWATER, Somerset 08807, FAMILY]
    /**
     *  Method would a string representation of the appointment, including the date, timeslot, patient,
     * and provider details.
     *
     * @return A string representing the appointment in the format:
     *         "10/30/2024 10:45 AM John Doe 12/13/1989 [PATEL, BRIDGEWATER, Somerset 08807, FAMILY]"
     */
    @Override
    public String toString() {
        return this.date.toString() + " " + this.timeslot.toString() + " "+ this.patient.toString() + " " + this.provider.toString();
    }
}