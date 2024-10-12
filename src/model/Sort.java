package model;

public class Sort {
    public static <E extends Comparable<E>> void sort(List<E> list) {
        int n = list.size();

        // Create a temporary list to hold sorted elements
        List<E> sortedList = new List<>();

        while (!list.isEmpty()) {
            E smallest = list.get(0); // Assume the first element is the smallest

            // Find the smallest element in the current list
            for (int i = 1; i < list.size(); i++) {
                if (list.get(i).compareTo(smallest) < 0) {
                    smallest = list.get(i);
                }
            }

            // Remove the smallest element from the original list
            list.remove(smallest);

            // Add it to the sorted list
            sortedList.add(smallest);
        }

        // Transfer sorted elements back to the original list
        while (!sortedList.isEmpty()) {
            E element = sortedList.get(0); // Get the first element
            sortedList.remove(element); // Remove from sorted list
            list.add(element); // Add to the original list
        }
    }

    public static void appointment(List<Appointment> list, char key) {
        int n = list.size();
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
                comparison = current.getProfile().compareTo(smallest.getProfile());
                if (comparison == 0) {
                    comparison = current.compareTo(smallest);
                }
                break;
            case 'L': // Sort by county name, then date, then time
                comparison = current.getProfile().compareTo(smallest.getProfile());
                if (comparison == 0) { // If profiles are the same
                    comparison = current.getDate().compareTo(smallest.getDate());
                    if (comparison == 0) { // If appointment dates are the same
                        comparison = current.getTimeslot().compareTo(smallest.getTimeslot());
                    }
                }
                break;
            case 'O': // Sort office appointments (PO command)
            case 'I': // Sort imaging appointments (PI command)
                comparison = ((Provider) current.getProvider()).getLocation().getCounty().compareTo(
                        ((Provider) smallest.getProvider()).getLocation().getCounty());
                if (comparison == 0) {
                    comparison = current.getDate().compareTo(smallest.getDate());
                    if (comparison == 0) {
                        comparison = current.getTimeslot().compareTo(smallest.getTimeslot());
                    }
                }
                break;
        }

        return comparison;
    }



    public static void main(String[] args) {


    }
}
