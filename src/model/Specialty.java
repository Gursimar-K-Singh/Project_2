package model;

/**
 * Represents medical specialties with associated charges.
 *
 * Specialties include:
 * - FAMILY: 250
 * - PEDIATRICIAN: 300
 * - ALLERGIST: 350
 *
 * Provides methods to retrieve the charge and a string representation
 * of the specialty name.
 *
 * @author Gursimar Singh
 */
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



}
