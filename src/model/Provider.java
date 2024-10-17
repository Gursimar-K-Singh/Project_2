package model;

/**
 The Provider class represents a healthcare provider in the clinic system.
 * It extends the Person class, inheriting the provider's personal information, and associates
 * each provider with a specific location where they practice. It is an aobstract class as it
 * contains the abstract method rate().
 *
 *The class provides methods to retrieve the provider's name, location, and other details,
 * and overrides the {@code toString()} method to return a formatted string representing the provider.
 *
 * Example subclasses of Provider could include Doctor and Technician, each with their own
 * specific attributes and behaviors. :)
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

    public String getDob() {
        return getProfile().getDob().toString(); // Assuming the profile contains first and last name
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
        return "[" + getName() + " " + getDob() + ", "+ location + "]";
    }




}
