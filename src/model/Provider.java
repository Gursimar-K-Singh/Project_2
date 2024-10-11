package model;

/**
 * The Provider enum represents healthcare providers, each associated with a specific location and specialty.
 * This enum allows retrieval of a provider's name, location, and specialty. It overrides the `toString()` method
 * to return a formatted string representing the provider, location, and specialty.
 * Each enum constant represents a healthcare provider, such as PATEL, LIM, ZIMNES, HARPER, and so on,
 * with a specific location and specialty.
 * Locations are represented by the Location enum, and specialties are represented by the Specialty enum.
 *
 * @author AparnaSrinivas
 * @author GursimarSingh
 */
public abstract class Provider extends Person {

    private final Location location;

    /**
     * Constructs a Provider object with the specified location.
     *
     * @param profile The profile of the provider (inherited from Person)
     * @param location The location of the provider.
     */
    public Provider(Profile profile, Location location) {
        super(profile); // Call the constructor of the Person class
        this.location = location;
    }

    /**
     * Returns the name of the provider.
     * This is the enum constant's name, such as "PATEL" or "LIM".
     *
     * @return The name of the provider.
     */
    public String getName() {
        return getProfile().getFname() + " " + getProfile().getLname(); // Assuming the profile contains first and last name
    }


    /**
     * Returns the location of the provider.
     *
     * @return The location of the provider.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Abstract method to get the provider's charging rate per visit.
     *
     * @return The charging rate for seeing patients.
     */
    public abstract int rate(); // Subclasses must implement this method

     /**
     * Returns a string representation of the provider.
     *
     * @return A string representing the provider, including the name and location.
     */
    @Override
    public String toString() {
        return "[" + getName() + ", " + location + "]";
    }




}
