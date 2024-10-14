package util;

import model.Appointment;
import model.Provider;

public class sort {
    public static void appointment(List<Appointment> list, char key) {
        List<Appointment> sortedList = new List<>();

        while (!list.isEmpty()) {
            Appointment smallest = list.get(0);

            for (int i = 1; i < list.size(); i++) {
                Appointment current = list.get(i);
                int comparison = compareAppointments(current, smallest, key);

                if (comparison < 0) {
                    smallest = current; // Update the smallest appointment
                }
            }

            list.remove(smallest); // Remove the smallest appointment from the original list
            sortedList.add(smallest); // Add it to the sorted list
        }

        // Transfer sorted elements back to the original list
        while (!sortedList.isEmpty()) {
            Appointment appointment = sortedList.get(0);
            sortedList.remove(appointment);
            list.add(appointment);
        }
    }

    /**
     * Compares two appointments based on the specified sorting key.
     *
     * @param current The current appointment to compare.
     * @param smallest The smallest appointment found so far.
     * @param key The sorting key.
     * @return A negative integer, zero, or a positive integer as the current appointment
     *         is less than, equal to, or greater than the smallest appointment.
     */
    private static int compareAppointments(Appointment current, Appointment smallest, char key) {
        int comparison = 0;

        switch (key) {
            case 'A': // Sort by appointment date, then time, then provider's name
                comparison = current.getDate().compareTo(smallest.getDate());
                if (comparison == 0) { // If the dates are the same
                    comparison = current.getTimeslot().compareTo(smallest.getTimeslot());
                    if (comparison == 0) { // If the timeslots are the same
                        comparison = current.getProvider().getProfile().getLname().compareTo(
                                smallest.getProvider().getProfile().getLname());
                    }
                }
                break;
            case 'P': // Sort by patient (patient profile comparison can be adjusted as needed)
                comparison = current.getProfile().compareTo(smallest.getProfile()); // Compare by patient profile
                if (comparison == 0) { // If profiles are the same
                    comparison = compareDateAndTime(current, smallest); // Use the new method
                }
                break;
            case 'L': // Sort by county name, then date, then time
            case 'O': // Sort office appointments (PO command)
            case 'I': // Sort imaging appointments (PI command)
                comparison = ((Provider) current.getProvider()).getLocation().getCounty().compareTo(
                        ((Provider) smallest.getProvider()).getLocation().getCounty());
                if (comparison == 0) {
                    comparison = compareDateAndTime(current, smallest); // Use the new method
                }
                break;
        }

        return comparison;
    }

    private static int compareDateAndTime(Appointment current, Appointment smallest) {
        int comparison = current.getDate().compareTo(smallest.getDate());
        if (comparison == 0) {
            comparison = current.getTimeslot().compareTo(smallest.getTimeslot());
        }
        return comparison;
    }

    public static void provider(List<Provider> list) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                Provider current = list.get(j);
                Provider next = list.get(j + 1);

                // Get last names and first names
                String currentLastName = current.getProfile().getLname();
                String nextLastName = next.getProfile().getLname();
                String currentFirstName = current.getProfile().getFname();
                String nextFirstName = next.getProfile().getFname();

                // Compare last names
                if (currentLastName.compareTo(nextLastName) > 0) {
                    // Swap if current's last name is greater than next's last name
                    list.add(next); // Add next to the end of the list
                    list.remove(current); // Remove current from its position
                } else if (currentLastName.equals(nextLastName)) {
                    // If last names are equal, compare first names
                    if (currentFirstName.compareTo(nextFirstName) > 0) {
                        // Swap if current's first name is greater than next's first name
                        list.add(next);
                        list.remove(current);
                    }
                }
            }
        }

        // Display the sorted providers
        for (int i = 0; i < list.size(); i++) {
            Provider provider = list.get(i);
            System.out.println(provider.getProfile().getFname() + " " + provider.getProfile().getLname());
        }
    }
}
