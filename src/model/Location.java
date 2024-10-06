package model;

/**
 * The Location enum represents various locations where providers operate.
 * Each location is associated with a specific county and zip code.
 * The enum provides methods to retrieve the county and zip code for each location.
 * It overrides the toString() method to return a formatted string representation of the location.
 *
 * Enum constants represent locations such as BRIDGEWATER, EDISON, PISCATAWAY, PRINCETON, MORRISTOWN, and CLARK.
 * Each constant holds information about the county and zip code of the location.
 *
 * @author AparnaSrinivas
 * @author GursimarSingh
 */
public enum Location {
    BRIDGEWATER("Somerset County", "08807"),
    EDISON("Middlesex County", "08817"),
    PISCATAWAY("Middlesex County", "08854"),
    PRINCETON("Mercer County", "08542"),
    MORRISTOWN("Morris County", "07960"),
    CLARK("Union County", "07066");

    private final String county;
    private final String zip;

    /**
     * Constructor for the Location enum.
     *
     * @param county The county in which the location is situated.
     * @param zip The zip code of the location.
     */
    Location(String county, String zip) {
        this.county = county;
        this.zip = zip;
    }

    /**
     * Returns a string representation of the location.
     * The string includes the location name, county, and zip code.
     *
     * @return A formatted string representation of the location.
     */
    @Override
    public String toString() {
        return name() + ","+county + " " + zip;
    }

    /**
     * Returns the county of the location.
     *
     * @return The county as a String.
     */
    public String getCounty(){
        return county;
    }

    /**
     * Returns the zip code of the location.
     *
     * @return The zip code as a String.
     */
    public String getZip(){
        return zip;
    }


}