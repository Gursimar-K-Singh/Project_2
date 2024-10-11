package model;

/**
 * The Doctor class represents a specific type of healthcare provider.
 * It extends the Provider class and encapsulates the rate per visit based on specialty,
 * along with a unique National Provider Identification (NPI) number.
 *
 * @author YourName
 */

public class Doctor extends Provider {
    private final Specialty specialty; // Encapsulates the rate per visit based on specialty
    private final String npi;


    /**
     * Constructs a Doctor object with the specified details.
     *
     * @param profile The profile of the doctor.
     * @param location The location of the doctor.
     * @param specialty The specialty of the doctor.
     * @param npi The National Provider Identification number.
     */
    public Doctor(Profile profile, Location location, Specialty specialty, String npi) {
        super(profile, location); // Call the Provider constructor
        this.specialty = specialty; // Initialize specialty
        this.npi = npi; // Initialize NPI
    }

    /**
     * Returns the specialty of the doctor.
     *
     * @return The specialty of the doctor.
     */
    public Specialty getSpecialty() {
        return specialty;
    }

    /**
     * Returns the National Provider Identification number of the doctor.
     *
     * @return The NPI of the doctor.
     */
    public String getNpi() {
        return npi;
    }

    /**
     * Returns the charge per visit for this doctor.
     * This implementation is based on the specialty of the doctor.
     *
     * @return The charge per visit.
     */
    @Override
    public int rate() {
        // Example rates based on specialty
        return switch (specialty) {
            case FAMILY -> 250;
            case PEDIATRICIAN -> 300;
            case ALLERGIST -> 350;
            default -> 0;
        };
    }


    /**
     * Returns a string representation of the Doctor object, including their details.
     *
     * @return A string representing the doctor, including name, NPI, and specialty.
     */
    @Override
    public String toString() {
        return super.toString() + ", NPI: " + npi + ", Specialty: " + specialty;
    }

}
