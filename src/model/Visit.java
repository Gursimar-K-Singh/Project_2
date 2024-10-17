package model;

/**
 * Represents a visit containing an appointment and a reference to the next visit.
 *
 * This class is used to create linked structures of visits for appointment management.
 * It provides constructors to initialize the visit with an appointment and optionally
 * link to the next visit. Getters and setters are available to access and modify
 * the appointment and next visit.
 *
 * @author Gursimar Singh
 */
public class Visit {
    private Appointment appointment;
    private Visit next;

    public Visit(Appointment appointment) {
        this.appointment = appointment;
        this.next = null;
    }


    public Appointment getAppointment() {
        return appointment;
    }

    public Visit getNext() {
        return next;
    }

    public void setNext(Visit next) {
        this.next = next;
    }


}
