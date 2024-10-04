package projectone;

//@author AparnaSrinivas @author GursimarSingh
public enum Specialty {
    FAMILY(250),
    PEDIATRICIAN(300),
    ALLERGIST(350);

    private final int charge;

    Specialty(int charge) {
        this.charge = charge;
    }

    public int getCharge() {
        return charge;
    }

    public String toString() {
        return name();
    }


    public static void main(String[] args) {
        // Test each specialty and print the amount due
        for (Specialty specialty : Specialty.values()) {
            System.out.println(specialty.toString());
        }
    }


}
