package model;

//@author AparnaSrinivas @author GursimarSingh
public class Visit {
    private Appointment appointment;
    private Visit next;

    public Visit(Appointment appointment) {
        this.appointment = appointment;
        this.next = null;
    }

    public Visit(Appointment appointment, Visit next){
        this.appointment = appointment;
        this.next = next;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Visit getNext() {
        return next;
    }

    public void setNext(Visit next) {
        this.next = next;
    }


}
