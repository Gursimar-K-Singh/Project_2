package model;

/**
 * The Patient class represents a patient in the medical record system.
 * It contains the patient's profile and a linked list of visits.
 * The class provides methods for adding visits, calculating total charges,
 * and comparing patients based on their profiles.
 * This class also implements the Comparable interface, allowing comparison between patients
 * based on their profiles. The total charge for a patient is calculated based on the
 * specialty of the providers in their visits.
 *
 * @author AparnaSrinivas
 * @author GursimarSingh
 */
public class Patient extends Person {
    private Visit visits;

    /**
     * Constructs a Patient object with the specified profile.
     * Initializes the visit list as empty.
     *
     * @param profile The profile of the patient.
     */
    public Patient(Profile profile) {
        super(profile);
        this.visits = null;//empty list
    }

    /**
     * Returns the profile of the patient.
     *
     * @return The patient's profile.
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Compares this patient to another patient based on their profiles.
     *
     * @param obj The object to compare with this patient.
     * @return true if the profiles are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(obj instanceof Patient){
            Patient patient = (Patient) obj;
            return profile.equals(patient.profile);
        }
        return false;

    }

    /**
     * Adds a new visit to the patient's visit list.
     * If the list is empty, the visit is set as the head.
     * Otherwise, the new visit is added to the end of the list.
     *
     * @param visit The visit to add to the patient's visit list.
     */
    public void addVisit(Visit visit) {
        if (visits == null) {
            visits = visit; // If there are no visits yet, set it as the head
        } else {
            Visit current = visits;
            // Traverse to the end of the list
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(visit); // Add the new visit at the end
        }
    }

    /**
     * Calculates the total charge for the patient based on their visits.
     * Each visit contributes to the total charge, determined by the provider's specialty.
     *
     * @return The total charge for all visits.
     */
    public int charge() {
        int total = 0;
        Visit current = visits; // Start traversing from the first visit

        while (current != null) {
            Provider provider = (Provider) current.getAppointment().getProvider();

            // Check the type of provider and calculate the charge based on it
            if (provider instanceof Doctor doctor) {
                total += doctor.rate(); // Get the charge based on the specialty of the doctor
            } else if (provider instanceof Technician technician) {
                total += technician.rate(); // Get the charge based on the technician's rate per visit
            }

            current = current.getNext(); // Move to the next visit
        }

        return total;
    }


    /**
     * Returns a string representation of the patient, using the profile's toString method.
     * @return A string representing the patient's profile.
     */
    @Override
    public String toString() {
        return profile.toString();//profile class's toString()
    }

    public int compareTo(Person other) {
        return super.compareTo(other);
    }

}

