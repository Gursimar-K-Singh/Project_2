package projectone;

//List.java
//@author AparnaSrinivas @authorGursimarSingh

public class List {
    private static final int INITIAL_CAPACITY = 10;
    private static final int NOT_FOUND = -1;
    private Appointment[] appointments;
    private int size;

    // Constructor
    public List() {
        appointments = new Appointment[INITIAL_CAPACITY];
        this.size = 0;
    }

    public int getSize() {
        return size;
    }

    public Appointment get(int index) {
        if (index < 0 || index >= size) {
            return null; // or throw an exception if you prefer
        }
        return appointments[index];
    }

    private int find(Appointment appointment) {

        for (int i = 0; i < size; i++) {

            if (appointments[i].equals(appointment)) {
                return i;
            }
        }

        return NOT_FOUND;
    }

    public boolean isAvailable(Appointment appointment) {
        for (int i = 0; i < size; i++) {
            // Check if the date, timeslot, and provider are the same
            if (appointments[i].getDate().equals(appointment.getDate()) &&
                    appointments[i].getTimeslot() == appointment.getTimeslot() &&
                    appointments[i].getProvider() == appointment.getProvider()) {
                return false; // The provider is not available at the specified timeslot
            }
        }
        return true; // The provider is available at the specified timeslot
    }

    private void grow() {
        Appointment[] newAppointments = new Appointment[appointments.length + 4];
        for (int i = 0; i < size; i++) {
            newAppointments[i] = appointments[i];
        }
        appointments = newAppointments;
    }

    public boolean contains(Appointment appointment) {

        return find(appointment) != NOT_FOUND;

    }

    public void add(Appointment appointment) {
        if (size == appointments.length) {
            grow();
        }
        appointments[size] = appointment;
        size++;
    }

    public void remove(Appointment appointment) {
        int index = find(appointment);
        if (index != NOT_FOUND) {
            for (int i = index; i < size - 1; i++) {
                appointments[i] = appointments[i + 1];
            }
            appointments[size - 1] = null;
            size--;
        }
    }

    public Appointment removeFirst() {
        if (size == 0) {
            System.out.println("List is empty, nothing to remove.");
            return null;
        }
        Appointment removedAppointment = appointments[0];

        // Shift all elements one position to the left
        for (int i = 0; i < size - 1; i++) {
            appointments[i] = appointments[i + 1];
        }

        // Nullify the last element, as it has been shifted
        appointments[size - 1] = null;
        size--;
        return removedAppointment;
    }



    public void printByPatient() {
        // creating sorting algorithm
        profileSort();
        for (int i = 0; i < size; i++) {
            System.out.println(appointments[i].toString());
        }

    }

    public void printByLocation() {
        // create sorting algorithm
        CountySort();
        for (int i = 0; i < size; i++) {
            System.out.println(appointments[i].toString());
        }

    }

    public void printByAppointment() {
        // create sorting algorithm
        AptDateSort();
        for (int i = 0; i < size; i++) {
            System.out.println(appointments[i].toString());
        }

    }


    private void profileSort() {
        int new_len = size - 1;
        for (int i = 0; i < new_len; i++) {
            for (int j = 0; j < new_len - i; j++) {
                int lastNameComparison = appointments[j].getProfile().getLname().compareTo(appointments[j + 1].getProfile().getLname());

                // If first names are the same, compare last names
                if (lastNameComparison == 0) {
                    // Compare last names
                    int firstNameComparison = appointments[j].getProfile().getFname().compareTo(appointments[j + 1].getProfile().getFname());
                    if (firstNameComparison > 0) {
                        // Swap if the last name of the first appointment is greater
                        Appointment current = appointments[j];
                        appointments[j] = appointments[j + 1];
                        appointments[j + 1] = current;
                    }
                } else if (lastNameComparison > 0) {
                    // Swap if the first name of the first appointment is greater
                    Appointment current = appointments[j];
                    appointments[j] = appointments[j + 1];
                    appointments[j + 1] = current;
                }
            }
        }
    }

    private void AptDateSort() {
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - 1 - i; j++) {
                // Compare dates
                int dateComparison = appointments[j].getDate().compareTo(appointments[j + 1].getDate());
                // Compare timeslots if dates are equal
                int timeslotComparison = appointments[j].getTimeslot().compareTo(appointments[j + 1].getTimeslot());
                // Compare provider's last name if both dates and timeslots are equal
                int providerComparison = appointments[j].getProvider().getName().compareTo(appointments[j + 1].getProvider().getName());

                if (dateComparison > 0 ||
                        (dateComparison == 0 && timeslotComparison > 0) ||
                        (dateComparison == 0 && timeslotComparison == 0 && providerComparison > 0)) {
                    // Swap appointments
                    Appointment temp = appointments[j];
                    appointments[j] = appointments[j + 1];
                    appointments[j + 1] = temp;
                }
            }
        }
    }

    private void CountySort() {
        int n = size; // size refers to the number of appointments in the list
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - 1 - i; j++) {
                int countyComparison = appointments[j].getProvider().getLocation().getCounty().compareTo(appointments[j + 1].getProvider().getLocation().getCounty());
                int dateComparison = appointments[j].getDate().compareTo(appointments[j + 1].getDate());
                int timeslotComparison = appointments[j].getTimeslot().compareTo(appointments[j + 1].getTimeslot());

                if (countyComparison > 0 ||
                        (countyComparison == 0 && dateComparison > 0) ||
                        (countyComparison == 0 && dateComparison == 0 && timeslotComparison > 0)) {

                    // Swap appointments
                    Appointment temp = appointments[j];
                    appointments[j] = appointments[j + 1];
                    appointments[j + 1] = temp;
                }
            }
        }
    }

}