package projectone;

//@author AparnaSrinivas @author GursimarSingh
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

    Provider(Location location, Specialty specialty) {
        this.location = location;
        this.specialty = specialty;
    }

    public String getName(){
        return name();
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "[" + this.name() + "," + location.toString() +"," + specialty + "]";
    }

    public Specialty getSpecialty(){
        return specialty;
    }

    public static void main(String[] args) {


        System.out.println("\nProviders:");
        // Test the Provider enum
        for (Provider provider : Provider.values()) {
            System.out.println(provider); // This will invoke the toString method of Provider
        }
    }


}
