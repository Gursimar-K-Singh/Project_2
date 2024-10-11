package model;

public class Imaging extends Appointment {

    private final Radiology room; // The imaging room for the appointment (e.g., X-ray, ultrasound, CAT scan)

    /**
     * Constructs an Imaging appointment with the specified details.
     *
     * @param date The date of the appointment.
     * @param timeslot The timeslot for the appointment.
     * @param patient The patient associated with the appointment.
     * @param provider The provider conducting the imaging procedure.
     * @param room The imaging room for the appointment.
     */
    public Imaging(Date date, Timeslot timeslot, Person patient, Person provider, Radiology room) {
        super(date, timeslot, patient, provider); // Call the Appointment constructor
        this.room = room; // Initialize the imaging room
    }

    /**
     * Returns the imaging room for this appointment.
     *
     * @return The imaging room.
     */
    public Radiology getRoom() {
        return room;
    }

    /**
     * Returns a string representation of the Imaging appointment, including details about the room.
     *
     * @return A string representing the imaging appointment.
     */
    @Override
    public String toString() {
        return super.toString() + ", Imaging Room: " + room;
    }
}
