package projectone;

/**
 * The Provider enum represents healthcare providers, each associated with a specific location and specialty.
 * This enum allows retrieval of a provider's name, location, and specialty. It overrides the `toString()` method
 * to return a formatted string representing the provider, location, and specialty.
 *
 * Each enum constant represents a healthcare provider, such as PATEL, LIM, ZIMNES, HARPER, and so on,
 * with a specific location and specialty.
 *
 * Locations are represented by the Location enum, and specialties are represented by the Specialty enum.
 *
 * @author AparnaSrinivas
 * @author GursimarSingh
 */
public enum Provider {
    PATEL(Location.BRIDGEWATER, Specialty.FAMILY),
    LIM(Location.BRIDGEWATER, Specialty.PEDIATRICIAN),
    ZIMNES(Location.CLARK, Specialty.FAMILY),
    HARPER(Location.CLARK, Specialty.FAMILY),
    KAUR(Location.PRINCETON, Specialty.ALLERGIST),
    TAYLOR(Location.PISCATAWAY, Specialty.PEDIATRICIAN),
    RAMESH(Location.MORRISTOWN, Specialty.ALLERGIST),
    CERAVOLO(Location.EDISON, Specialty.PEDIATRICIAN);

    private final Location location;
    private final Specialty specialty;

    /**
     * Constructs a Provider enum constant with the specified location and specialty.
     *
     * @param location The location of the provider.
     * @param specialty The specialty of the provider.
     */
    Provider(Location location, Specialty specialty) {
        this.location = location;
        this.specialty = specialty;
    }

    /**
     * Returns the name of the provider.
     * This is the enum constant's name, such as "PATEL" or "LIM".
     *
     * @return The name of the provider.
     */
    public String getName(){
        return name();
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
     * Returns a string representation of the provider in the format:
     * "[ProviderName, Location, Specialty]"
     *
     * @return A string representing the provider, including the name, location, and specialty.
     */
    @Override
    public String toString() {
        return "[" + this.name() + "," + location.toString() +"," + specialty + "]";
    }

    /**
     * Returns the specialty of the provider.
     *
     * @return The specialty of the provider.
     */
    public Specialty getSpecialty(){
        return specialty;
    }

    /**
     * Main method to test the Provider enum.
     * This method prints all providers along with their location and specialty.
     *
     * @param args The command-line arguments (not used).
     */
    public static void main(String[] args) {


        System.out.println("\nProviders:");
        // Test the Provider enum
        for (Provider provider : Provider.values()) {
            System.out.println(provider); // This will invoke the toString method of Provider
        }
    }


}
