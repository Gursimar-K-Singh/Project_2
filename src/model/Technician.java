package model;

/**
 * The Technician class represents a specific type of healthcare provider.
 * It extends the Provider class and encapsulates the charging rate per visit.
 *
 * @author YourName
 */
public class Technician extends Provider {

    private final int ratePerVisit; // Charging rate per visit for the technician

    /**
     * Constructs a Technician object with the specified details.
     *
     * @param profile The profile of the technician.
     * @param location The location of the technician.
     * @param ratePerVisit The charging rate for each visit.
     */
    public Technician(Profile profile, Location location, int ratePerVisit) {
        super(profile, location); // Call the Provider constructor
        this.ratePerVisit = ratePerVisit; // Initialize the charging rate
    }

    /**
     * Returns the charge per visit for this technician.
     *
     * @return The technician's charging rate per visit.
     */
    @Override
    public int rate() {
        return ratePerVisit; // Return the technician's charge per visit
    }

    /**
     * Returns a string representation of the Technician object, including their details.
     *
     * @return A string representing the technician, including name, location, and rate per visit.
     */
    @Override
    public String toString() {
        return super.toString() + ", Rate per Visit: $" + ratePerVisit;
    }
}
